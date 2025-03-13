<template>
  <div>拖拽</div>
  <div class="flex flex-row justify-start items-center">
    <span class="mr-2">预览大小:</span>
    <el-radio-group v-model="imageSize">
      <el-radio-button label="小" value="small" />
      <el-radio-button label="中" value="middle" />
      <el-radio-button label="大" value="large" />
    </el-radio-group>
  </div>
  <!-- 列表 -->
  <div class="m-4">
    <el-scrollbar class="h-[calc(100vh-20rem)]">
      <div ref="dragRef">
        <div v-for="item,index in pageInfo?.list" :key="item.id" 
        draggable="true" 
        >
          <div :class="['select-none', 'imgCard', 'relative', getImageSize().cardW, getImageSize().cardH,
            'flex', 'flex-row','justify-center', 'items-center', 'float-start', 'm-2']" shadow="hover">
            
            <!-- 图片主体 -->
            <el-image draggable="false" v-if="item.imageType == ImageType.url" 
            fit="contain" :preview-src-list="[]" :src="item.url"
            :class="[getImageSize().imageW, getImageSize().imageH, 'rounded-lg']"></el-image>
            
            <div v-if="item.imageType == ImageType.svg" 
            :class="[getImageSize().imageW, getImageSize().imageH, 'rounded-lg', 
            'flex', 'flex-row', 'justify-center', 'items-center']"
            >
              <div v-html="item.svgCode"></div>
            </div>
            <el-image draggable="false" v-if="item.imageType == ImageType.base64" 
            fit="contain" :preview-src-list="[]" :src="item.base64Code"
            :class="[getImageSize().imageW, getImageSize().imageH, 'rounded-lg']"></el-image>

            <!-- 名称 -->
            <div v-if="imageSize != 'small'">
              <!-- <el-tooltip effect="dark" :content="item.attname" placement="bottom"> -->
                <div :class="[getImageSize().imageW, 'overflow-hidden', 'whitespace-nowrap', 'text-ellipsis']">
                  <span :class="['select-none', 'text-xs']">{{ item.attname }}</span>
                </div>
              <!-- </el-tooltip> -->
            </div>


            <!-- 复制链接 -->
            <div class="cursor-pointer w-full imgMain hidden 
            absolute z-50 left-0"
            :class="[imageSize == 'small'?'bottom-0':'top-0']"
            >
              <div class="w-full flex justify-center items-center">
                <el-button v-if="item.imageType == ImageType.url" class="hover:underline" 
                @click="copyImage(item.url)" type="primary" link>复制链接</el-button>
                <el-button v-if="item.imageType == ImageType.svg" class="hover:underline" 
                @click="copyImage(item.svgCode)" type="primary" link>复制代码</el-button>
                <el-button v-if="item.imageType == ImageType.base64" class="hover:underline" 
                @click="copyImage(item.base64Code)" type="primary" link>复制代码</el-button>
              </div>
            </div>
            <!-- 图片类型 -->
            <div   class="absolute z-50 top-0 left-0">
              <div class="flex flex-row justify-start items-start m-0 p-0">
                <el-tag size="small" class="m-0 p-0" :type="getImageTagType(item.imageType)">{{item.imageType}}</el-tag>
              </div>
            </div>

          </div>
        </div>
      </div>
    </el-scrollbar>
  </div>
</template>

<script setup lang="ts" name="learnDrag">
import type { PageInfo } from '@/types/PageInfo';
import { request } from '@/utils/Request';
import {ImageSizeInstant, ImageType, type Image, type ImageSize} from "@/pages/application/system/images/ImageTypes"
import { ref, useTemplateRef } from 'vue';
import type { R } from '@/types/R';
import useClipboard from "vue-clipboard3";
import { ElMessage } from 'element-plus';
import { onMounted } from 'vue';
import {Flip, FlipList} from "@/utils/FlipUtil"

// 页面引用
const dragRef = useTemplateRef<any>("dragRef")

// 响应参数
let pageInfo = ref<PageInfo<Image>>()
const select = async ()=>{
    let r:R<PageInfo<Image>> = await request.post("/image/select", {
        pageNum: 1,
        pageSize: 500
    })
    if(r.code === "success"){
        pageInfo.value = r.data
    }
}

onMounted(()=>{
  select()
})

const getImageTagType = (imageType: ImageType)=>{
    if(imageType == ImageType.url){
        return "primary";
    }else if(imageType == ImageType.svg){
        return "warning";
    }else if(imageType == ImageType.base64){
        return "danger";
    }
}
const getImageSize = ():ImageSize=>{
  switch (imageSize.value) {
    case "small":
      return ImageSizeInstant.small
    case "middle":
      return ImageSizeInstant.middle
    case "large":
      return ImageSizeInstant.large
    default:
      return ImageSizeInstant.small
  }
}

let imageSize = ref<string>("small")

const { toClipboard } = useClipboard();

const copyImage = async(data:string)=>{
    await toClipboard(data);  //实现复制
    ElMessage.success("复制成功")
}

/** 获取顶层div */
const findDragElement = (target: any) => {
  try{
    if(target.getAttribute("draggable")=='true'){
      return target;
    }else{
      return findDragElement(target.parentElement)
    }
  }catch(err){
    return undefined
  }
}

// onMounted(()=>{
//   for(let item of dragRef.value.children){
//     item.children[0].classList.add("canMove")
//   }
// })

let sourceElement:any = null
let targetElement:any = null
onMounted(() => {
  dragRef.value!.ondragstart = (e: any) => {
    let targetElement = findDragElement(e.target)
    
    setTimeout(() => {
      targetElement.children[0].classList.add("moving")
      // e.target.classList.add("moving")
    }, 0)
    sourceElement = targetElement
  }
  dragRef.value!.ondragover = (e: any) => {
    // e.preventDefault()
  }
  dragRef.value!.ondragenter = (e: any) => {
    // e.preventDefault()
    let findTargetElement = findDragElement(e.target)
    console.log("findTargetElement", findTargetElement)
    if(targetElement == findTargetElement){
      return
    }
    targetElement = findTargetElement
    if (targetElement === sourceElement) {
      return
    }

    let children = [...dragRef.value.children]

    let sourceIndex = children.indexOf(sourceElement)
    let targetIndex = children.indexOf(targetElement)


    if (sourceIndex > targetIndex) {

      let sourceTag = pageInfo.value?.list[sourceIndex]
      if (!sourceTag || (targetIndex == null || targetIndex == undefined || targetIndex < 0)) {
        return;
      }

      children.splice(sourceIndex, 1)
      // 动画{
      let list:any[] = []
      children.forEach(d=>{
        list.push(d.children[0])
      })
      let flipList = new FlipList(list, 0.5)
      // 动画}
      dragRef.value.insertBefore(sourceElement, targetElement)

      // 动画{
      flipList!.invert()
      // 动画}

    } else {
      let sourceTag = pageInfo.value?.list[sourceIndex]
      if (!sourceTag || !targetIndex) {
        return;
      }

      // 动画{
      children.splice(sourceIndex, 1)
      let list:any[] = []
      children.forEach(d=>{
        list.push(d.children[0])
      })
      let flipList = new FlipList(list, 0.5)
      dragRef.value.insertBefore(sourceElement, targetElement.nextElementSibling)
      // 动画}

      // 动画{
      flipList!.invert()
      // 动画}
    }
  }

  dragRef.value!.ondragend = (e: any) => {
    let targetElement = findDragElement(e.target)
    targetElement.children[0].classList.remove("moving")
    // let children = [...dragRef.value.children]
    // children.forEach((element: any) => {
    //   element.children[0].classList.remove("pointer-events-none")
    // })
  }
})

</script>

<style scoped>
.moving{
  /* background: transparent;
  color: transparent; */
  border: 1px dashed #ccc;
}
.moving > *{
  opacity: 0.0;
  /* background: transparent;
  color: transparent; */
  /* border: 1px dashed #ccc; */
}
/* .canMove {
  pointer-events: none;
}
.canMove * {
  pointer-events: none;
} */
</style>
