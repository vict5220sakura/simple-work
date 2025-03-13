<template>
    <div>testPageTag</div>
    
    <div id="tagFatherDiv" ref="tagFatherDiv">
        <el-tooltip 
                    placement="top" 
            v-for="tag in tags" 
            :key="tag.id"
            :content="tag.name"
            :visible="tag.showTooltip && tag.tooltipVisible && !dragFlag"
        >
            <span  
                @mouseenter="mouseenterTag"
                @mouseleave="mouseleaveTag" 
                :title="tag.name"
                :class='"tag  " 
                    + "  "
                    + (tag.choosed ? " bg-sky-300 " : " bg-sky-100 ")
                    + (tag.choosed ? " text-white " : "text-blue-400 ")'
                draggable="true" 
                @click="chooseTag(tag)">
                {{tag.tagName}}
                <i  @mouseenter="mouseenterI"
                    @mouseleave="mouseleaveI" 
                    @click="handleClose(tag)"
                    :class='"rounded-full el-icon el-tag__close " + (dragFlag?" pointer-events-none":"")'>
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1024 1024">
                        <path fill="currentColor" d="M764.288 214.592 512 466.88 259.712 214.592a31.936 31.936 0 0 0-45.12 45.12L466.752 512 214.528 764.224a31.936 31.936 0 1 0 45.12 45.184L512 557.184l252.288 252.288a31.936 31.936 0 0 0 45.12-45.12L557.12 512.064l252.288-252.352a31.936 31.936 0 1 0-45.12-45.184z"></path></svg></i>
            </span>
        </el-tooltip>
        <!-- <el-tag
            v-for="tag in tags"
            :key="tag.id"
            :id="tag.id"
            :closable="tags.length > 1" 
            size="default"
            :disable-transitions="false" 
            draggable="true"
            
            @close="handleClose(tag)"
            :class='"mr-2 cursor-pointer " 
                + (tag.choosed ? " bg-sky-300 " : "")
                + (tag.choosed ? " text-white " : "")'
            @click="chooseTag(tag)"
            
        >
        {{tag.tagName}}
        </el-tag> -->
    </div>
    
    
        
    <div ref="tagFatherDiv" >

            <!-- <TransitionGroup
                tag="span"
            > -->
                <el-tooltip 
                    placement="top" 
                    v-for="tag in tags" 
                    :key="tag.id"
                    :content="tag.name"
                    :visible="tag.showTooltip && tag.tooltipVisible && !dragFlag"
                >
                    <span  
                        @mouseenter="mouseenterTag"
                        @mouseleave="mouseleaveTag" 
                        :title="tag.name"
                        :class='"tag  " 
                            + "  "
                            + (tag.choosed ? " bg-sky-300 " : " bg-sky-100 ")
                            + (tag.choosed ? " text-white " : "text-blue-400 ")'
                        draggable="true" 
                        @click="chooseTag(tag)">
                        {{tag.tagName}}
                        <i  @mouseenter="mouseenterI"
                            @mouseleave="mouseleaveI" 
                            @click="handleClose(tag)"
                            :class='"rounded-full el-icon el-tag__close " + (dragFlag?" pointer-events-none":"")'>
                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1024 1024">
                                <path fill="currentColor" d="M764.288 214.592 512 466.88 259.712 214.592a31.936 31.936 0 0 0-45.12 45.12L466.752 512 214.528 764.224a31.936 31.936 0 1 0 45.12 45.184L512 557.184l252.288 252.288a31.936 31.936 0 0 0 45.12-45.12L557.12 512.064l252.288-252.352a31.936 31.936 0 1 0-45.12-45.184z"></path></svg></i>
                    </span>
                </el-tooltip>
            <!-- </TransitionGroup> -->
    </div>
    <el-button @click="add">添加</el-button>

</template>

<script setup lang="ts" name="testPageTag">
import {ref, getCurrentInstance, onMounted, computed} from 'vue'
import {type TagType} from '@/components/headtag/TagType'
import {randomId} from '@/utils/IdUtils'
import {getLength, indexLength} from '@/utils/StrUtils'
import {Flip, FlipList} from "@/utils/FlipUtil"
const instance = getCurrentInstance();
    
const tags = ref<TagType[]>([
    {
        id: "tag0001",
        name: 'tag11123124124123123',
        choosed: false,
        path: "/application/testPageKeepScroll",
        permission: ""
    },
    {
        id: "tag0002",
        name: '你好好好tag222',
        choosed: false,
        path: "/application/testMenus",
        permission: ""
    },
    {
        id: "tag0003",
        name: 'tag33好3你好好',
        choosed: false,
        path: "/application/testPageTag",
        permission: ""
    },
    {
        id: "tag0004",
        name: 'tag444',
        choosed: false,
        path: "/application/testPageTag",
        permission: ""
    }
])

function add(){
    let tag = {
        id: randomId(),
        name: 'tag444',
        choosed: false,
        path: "/application/testPageTag",
        permission: ""
    }
    tags.value.push(tag)
    tagMap[tag.id] = tag
    init(tag)
}

// 初始化tag
const tagMap:any = {}
for(let tag of tags.value){
    tagMap[tag.id] = tag
}

function init(tag:TagType){
    if(getLength(tag.name) > 12){
        tag.tagName = tag.name.substring(0, indexLength(tag.name, 12)) + "..."
        tag.showTooltip = true
    }else{
        tag.tagName = tag.name
        tag.showTooltip = false
    }
    tag.tooltipVisible = false
}

// 初始化tagName
for(let tag of tags.value){
    init(tag)
}


const handleClose = (tag: TagType) => {
    tags.value.splice(tags.value.indexOf(tag), 1)
}

const chooseTag = (tag: TagType)=>{
    for(let tag of tags.value){
        tag.choosed = false
    }
    tag.choosed = true
}



let sourceElement:any = null
const tagFatherDiv = ref<any>(null)
let inFlag:boolean = false
let dragFlag: boolean = false
onMounted(()=>{
    tagFatherDiv.value!.ondragstart = (e:any)=>{
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

    }
    tagFatherDiv.value.ondragover = (e:any)=>{
        e.preventDefault()
    }
    tagFatherDiv.value.ondragenter = (e:any)=>{
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
        if(targetElement === sourceElement || targetElement === tagFatherDiv.value){
            return
        }
        

        let children = [...tagFatherDiv.value.children]
        let sourceIndex = children.indexOf(sourceElement)
        let targetIndex = children.indexOf(targetElement)

        if(sourceIndex > targetIndex){
            console.log("向上拖动")
            let sourceTag = tags.value[sourceIndex]

            children.splice(sourceIndex, 1)
            
            let flipList = new FlipList(children, 0.5)

            tagFatherDiv.value.insertBefore(sourceElement, targetElement)
            tags.value.splice(sourceIndex, 1)
            tags.value.splice(targetIndex, 0, sourceTag)
            
            // instance?.proxy?.$forceUpdate();
            flipList.invert()
            // setTimeout(()=>{
                
                
            //     // instance?.proxy?.$forceUpdate();
            // }, 0)
            
        }else{
            console.log("向下拖动")

            children.splice(sourceIndex, 1)

            let flipList = new FlipList(children, 0.5)
            
            let sourceTag = tags.value[sourceIndex]
            tagFatherDiv.value.insertBefore(sourceElement, targetElement.nextElementSibling)
            tags.value.splice(targetIndex + 1, 0, sourceTag)
            tags.value.splice(sourceIndex, 1)
            
            flipList.invert()
        }

        targetElement.children[0].classList.remove("text-white")
        targetElement.children[0].classList.remove("bg-[#409eff]")
        sourceElement.children[0].classList.remove("text-white")
        sourceElement.children[0].classList.remove("bg-[#409eff]")
    }

    tagFatherDiv.value!.ondragend = (e:any)=>{
        inFlag = false;
        dragFlag = false
        e.target.classList.remove("moving")
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
    let children = [...tagFatherDiv.value.children]
    let targetIndex = children.indexOf(targetElement)
    let targetTag = tags.value[targetIndex]
    targetTag.tooltipVisible= true
}

let mouseleaveTag = (e:any)=>{
    let targetElement = e.target
    let children = [...tagFatherDiv.value.children]
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
