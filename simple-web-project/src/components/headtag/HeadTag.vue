<template>
    <el-scrollbar ref="headTagScrollbarRef" @scroll="scroll">
        <div class="flex flex-row" ref="headTagsRef">
            <el-tooltip 
                placement="top" 
                v-for="tag in tags" 
                :key="tag.id"
                :content="tag.name"
                :visible="tag.showTooltip && tag.tooltipVisible && !dragFlag"
            >
                <span  
                    v-permission="tag.permission"
                    @mouseenter="mouseenterTag"
                    @mouseleave="mouseleaveTag" 
                    :title="tag.name"
                    :class='"tag  " 
                        + "  "
                        + (tag.choosed ? " bg-sky-300 " : " bg-sky-100 ")
                        + (tag.choosed ? " text-white " : "text-blue-400 ")'
                    draggable="true" 
                    @click.stop="chooseTag(tag)">
                    {{tag.tagName}}
                    <i  v-if="tags.length > 1"
                        @mouseenter="mouseenterI"
                        @mouseleave="mouseleaveI" 
                        @click.stop="handleClose(tag)"
                        :class='"rounded-full el-icon el-tag__close " + (dragFlag?" ":"")'>
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1024 1024">
                            <path fill="currentColor" d="M764.288 214.592 512 466.88 259.712 214.592a31.936 31.936 0 0 0-45.12 45.12L466.752 512 214.528 764.224a31.936 31.936 0 1 0 45.12 45.184L512 557.184l252.288 252.288a31.936 31.936 0 0 0 45.12-45.12L557.12 512.064l252.288-252.352a31.936 31.936 0 1 0-45.12-45.184z"></path></svg></i>
                </span>
            </el-tooltip>
        </div>
    </el-scrollbar>
</template>

<script setup lang="ts" name="HeadTag">
import { ref, onMounted, onUpdated, getCurrentInstance, defineComponent, useTemplateRef, defineProps } from 'vue'
import {type TagType} from '@/components/headtag/TagType'
import {randomId} from '@/utils/IdUtils'
import { useRouter } from 'vue-router'
import { defaultPath, MenuConfig, findbyPath } from '@/router/MenuConfig'
import {emitter} from '@/utils/Emitter'
import {type Menu} from "@/components/menu/MenuTypes"
import { useTagBarStore } from '@/components/headtag/TagBarStore'
import { storeToRefs } from 'pinia';
import {getLength, indexLength} from '@/utils/StrUtils'
import {Flip, FlipList} from "@/utils/FlipUtil"
import { useApplicationStore } from '@/pages/application/applicationStore'

// 路由
const router = useRouter()
    
// 页面引用
const headTagsRef = useTemplateRef<any>("headTagsRef")
const headTagScrollbarRef = useTemplateRef<any>("headTagScrollbarRef")

// 存储
let tagBarStore = useTagBarStore()
let {initTags, initTag} = tagBarStore
let { tags, setTagLeftInitActionPath } = storeToRefs(tagBarStore)
let { activeMenuIndex } = storeToRefs(useApplicationStore())

// 初始化tagName
initTags()

// 临时记录滚动条位置
let scrollLeft = ref<number>(0)

// 监听鼠标滑轮事件
onMounted(() => {
    try {
        (headTagsRef.value! as any).addEventListener('wheel', (event: any) => {
            headTagScrollbarRef.value!.handleScroll()
            scrollLeft.value += event.deltaY;
            headTagScrollbarRef.value!.setScrollLeft(scrollLeft.value)
        });
    } catch (err) {
        console.error(err)
    }
})

// 滑动事件
const scroll = (e: any) => {
    scrollLeft.value = e.scrollLeft
}


// 关闭
const handleClose = (tag: TagType) => {
    let index = tags.value.indexOf(tag)
    tags.value.splice(index, 1)
    if (tag.choosed == true) {
        // 当前选中进行关闭, 打开左侧tag
        if (tags.value.length == 0) {
            // 打开首页
            router.push({
                path: defaultPath
            })
        } else {
            if (index - 1 < 0) {
                let tag = tags.value[index]
                chooseTag(tag)

            } else {
                let tag = tags.value[index - 1]
                chooseTag(tag)
            }
        }
    }
}
// 记录页面点击tag
let inChooseTag:TagType|null = null
// 选择
const chooseTag = (tag: TagType) => {
    for (let tag of tags.value) {
        tag.choosed = false
    }
    tag.choosed = true
    activeMenuIndex.value = tag.path
    router.push({
        path: tag.path
    })
    inChooseTag = tag;
}

// 初始化路由监听, 当渲染还未完成时触发
if(setTagLeftInitActionPath.value != null && setTagLeftInitActionPath.value != undefined){
    let path = setTagLeftInitActionPath.value
    if(path){
        let item: Menu = findbyPath(path as string)
        for (let tag of tags.value) {
            if (tag.path == item.path) {
                // 滑动到这个地方
                if(tag == inChooseTag){
                    
                }else{
                    setTimeout(()=>{
                        if(!headTagsRef.value){
                            return;
                        }
                        let children = [...headTagsRef.value.children]
                        let tagIndex = tags.value.indexOf(tag)
                        let tagElementIndex = children[tagIndex]
                        headTagScrollbarRef.value.setScrollLeft(tagElementIndex.offsetLeft)
                        inChooseTag = null
                    },0)
                }
                break
            }
        }
    }
    setTagLeftInitActionPath.value = null
}
// 路由监听
emitter.on("setTagLeft", (path) => {
    let item: Menu = findbyPath(path as string)
    for (let tag of tags.value) {
        if (tag.path == item.path) {
            // 滑动到这个地方
            if(tag == inChooseTag){
                
            }else{
                
                setTimeout(()=>{
                    if(!headTagsRef.value){
                        return;
                    }
                    let children = [...headTagsRef.value.children]
                    let tagIndex = tags.value.indexOf(tag)
                    let tagElementIndex = children[tagIndex]
                    headTagScrollbarRef.value!.setScrollLeft(tagElementIndex.offsetLeft)
                    inChooseTag = null
                },0)
            }
            return;
        }
    }
})

// tag拖拽
let sourceElement:any = null
let inFlag:boolean = false // 拖动进入标记
let dragFlag: boolean = false // 拖动标记
onMounted(()=>{
    headTagsRef.value!.ondragstart = (e:any)=>{
        dragFlag = true
        let targetElement = e.target
        if(e.target.tagName.toUpperCase() === "SPAN"){
            
        }else if(e.target.tagName.toUpperCase() === "PATH"){
            targetElement = e.target.parentElement.parentElement.parentElement
        }else if(e.target.tagName.toUpperCase() === "SVG"){
            targetElement = e.target.parentElement.parentElement
        }else if(e.target.tagName.toUpperCase() === "I"){
            targetElement = e.target.parentElement
        }

        setTimeout(()=>{
            targetElement.classList.add("moving")
            e.target.classList.add("moving")
        },0)
        sourceElement = targetElement

        targetElement.children[0].classList.remove("text-white")
        targetElement.children[0].classList.remove("bg-[#409eff]")

        let children = [...headTagsRef.value.children]
        children.forEach((element:any)=>{
            element.children[0].classList.add("pointer-events-none")
        })

    }
    headTagsRef.value!.ondragover = (e:any)=>{
        e.preventDefault()
    }
    headTagsRef.value!.ondragenter = (e:any)=>{
        inFlag = true;
        let targetElement = e.target
        if(e.target.tagName.toUpperCase() === "SPAN"){
        }else if(e.target.tagName.toUpperCase() === "PATH"){
            targetElement = e.target.parentElement.parentElement.parentElement
        }else if(e.target.tagName.toUpperCase() === "SVG"){
            targetElement = e.target.parentElement.parentElement
        }else if(e.target.tagName.toUpperCase() === "I"){
            targetElement = e.target.parentElement
        }
        
        e.preventDefault()
        if(targetElement === sourceElement || targetElement === headTagsRef.value){
            return
        }
        

        let children = [...headTagsRef.value.children]
        let sourceIndex = children.indexOf(sourceElement)
        let targetIndex = children.indexOf(targetElement)


        if(sourceIndex > targetIndex){
            
            let sourceTag = tags.value[sourceIndex]
            if(!sourceTag || (targetIndex == null || targetIndex == undefined || targetIndex < 0)){
                return;
            }

            // 动画{
            let flipList;
            if(import.meta.env.VITE_TAG_ANIMITION === "on"){
                children.splice(sourceIndex, 1)
                flipList = new FlipList(children, 0.5)
                headTagsRef.value.insertBefore(sourceElement, targetElement)
            }
            // 动画}
            
            tags.value.splice(sourceIndex, 1)
            tags.value.splice(targetIndex, 0, sourceTag)

            // 动画{
            if(import.meta.env.VITE_TAG_ANIMITION === "on"){
                flipList!.invert()
            }
            // 动画}

        }else{
            let sourceTag = tags.value[sourceIndex]
            if(!sourceTag || !targetIndex){
                return;
            }

            // 动画{
            let flipList;
            if(import.meta.env.VITE_TAG_ANIMITION === "on"){
                children.splice(sourceIndex, 1)
                flipList = new FlipList(children, 0.5)
                headTagsRef.value.insertBefore(sourceElement, targetElement.nextElementSibling)
            }
            // 动画}

            tags.value.splice(targetIndex + 1, 0, sourceTag)
            tags.value.splice(sourceIndex, 1)

            // 动画{
            if(import.meta.env.VITE_TAG_ANIMITION === "on"){
                flipList!.invert()
            }
            // 动画}
        }

        targetElement.children[0].classList.remove("text-white")
        targetElement.children[0].classList.remove("bg-[#409eff]")
        sourceElement.children[0].classList.remove("text-white")
        sourceElement.children[0].classList.remove("bg-[#409eff]")
    }

    headTagsRef.value!.ondragend = (e:any)=>{
        inFlag = false;
        dragFlag = false
        e.target.classList.remove("moving")
        let children = [...headTagsRef.value.children]
        children.forEach((element:any)=>{
            element.children[0].classList.remove("pointer-events-none")
        })
    }

})

let mouseenterI = (e:any)=> {
    if(inFlag){
        return;
    }
    e.target.classList.add("text-white")
    e.target.classList.add("bg-[#409eff]")
}

let mouseleaveI = (e:any)=>{
    e.target.classList.remove("text-white")
    e.target.classList.remove("bg-[#409eff]")
}
let mouseenterTag = (e:any)=> {
    let targetElement = e.target
    let children = [...headTagsRef.value.children]
    let targetIndex = children.indexOf(targetElement)
    let targetTag = tags.value[targetIndex]
    targetTag.tooltipVisible= true
}

let mouseleaveTag = (e:any)=>{
    let targetElement = e.target
    let children = [...headTagsRef.value.children]
    let targetIndex = children.indexOf(targetElement)
    let targetTag = tags.value[targetIndex]
    targetTag.tooltipVisible= false
}
</script>

<style scoped>
    .tag.moving{
        background: transparent;
        color: transparent;
        border: 1px dashed #ccc;
    }
    
    .tag{
        width: 120px;
        min-width: 120px;
        max-width: 120px;
        cursor: pointer;
        display: inline-flex;

        align-items: center;
        justify-content: space-between;
        
        border-image-outset: 0;
        border-image-repeat: stretch;
        border-image-slice: 100%;
        border-image-source: none;
        border-image-width: 1;

        border-bottom-color: rgb(217, 236, 255);
        border-bottom-style: none;
        border-bottom-width: 0px;
        border-bottom-left-radius: 4px;
        border-bottom-right-radius: 4px;
        
        border-left-color: rgb(217, 236, 255);
        border-left-style: solid;
        border-left-width: 0.666667px;
        
        border-right-color: rgb(217, 236, 255);
        border-right-style: solid;
        border-right-width: 0.666667px;
        
        border-top-color: rgb(217, 236, 255);
        border-top-style: solid;
        border-top-width: 0.666667px;
        border-top-left-radius: 4px;
        border-top-right-radius: 4px;
        box-sizing: border-box;
        color-scheme: light;
        

        font-family: ui-sans-serif, system-ui, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", "Noto Color Emoji";
        font-feature-settings: normal;
        font-size: 12px;
        font-variation-settings: normal;
        height: 24px;
        
        line-height: 12px;
        margin-right: 8px;
        padding-bottom: 0px;
        padding-left: 9px;
        padding-right: 5px;
        padding-top: 0px;
        tab-size: 4px;
        text-size-adjust: 100%;
        text-wrap: nowrap;
        user-select: none;
        vertical-align: middle;
        white-space-collapse: collapse;
        -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
        -webkit-user-drag: element;
    }

    .tag-text{
        width: 60px;
        margin: 2px;
    }
</style>
@/pages/application/applicationStore@/utils/Emitter@/router/MenuConfig