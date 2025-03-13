<template>
  <div class="m-4 flex flex-row justify-start items-end">
    <div>
      <QiniuUpload @getUrl="uploadImage">
        <el-button v-permission="permissionList.imagesAdd" size="small" type="default">
          上传图片<el-icon class="el-icon--right">
            <Upload />
          </el-icon></el-button>
      </QiniuUpload>
    </div>
    <div class="ml-4">
      <el-button type="default" size="small" @click="willUploadSvg">上传SVG代码</el-button>
    </div>
    <div class="ml-4">
      <el-button type="default" size="small" @click="willUploadBase64">上传BASE64代码</el-button>
    </div>
  </div>

  <InnerDialog v-model="uploadSvgDialogVisible" :width="800">
    <template #header>
      上传SVG代码
    </template>
    <template #default>
      <div class="m-4">
        <div>
          <div class="flex flex-row justify-start items-center m-4">
            <div class="w-20 required">SVG代码: </div>
            <el-input class="w-[30rem]" v-model="uploadSvgCode" placeholder="请输入SVG代码" />
          </div>
        </div>
      </div>
    </template>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="uploadSvgCancel">取消</el-button>
        <el-button type="primary" @click="uploadSvgSave">保存</el-button>
      </div>
    </template>
  </InnerDialog>

  <InnerDialog v-model="uploadBase64DialogVisible" :width="800">
    <template #header>
      上传BASE64代码
    </template>
    <template #default>
      <div class="m-4">
        <div>
          <div class="flex flex-row justify-start items-center m-4">
            <div class="w-32 required">BASE64代码: </div>
            <el-input class="w-[30rem]" v-model="uploadBase64Code" placeholder="请输入BASE64代码" />
          </div>
        </div>
      </div>
    </template>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="uploadBase64Cancel">取消</el-button>
        <el-button type="primary" @click="uploadBase64Save">保存</el-button>
      </div>
    </template>
  </InnerDialog>

  <!-- 查询项 -->
  <div class="m-4 flex felx-row justify-start items-center" v-permission="permissionList.images">
    <span class="w-20">
      图片名称:
    </span>
    <el-input clearable v-model="selectAttname" class="w-36 mr-4"></el-input>
    <span class="w-12">类型:</span>
    <el-select clearable class="w-36" v-model="selectImageType">
      <el-option :value="ImageType.url">url</el-option>
      <el-option :value="ImageType.svg">svg</el-option>
      <el-option :value="ImageType.base64">base64</el-option>
    </el-select>
    
  </div>
  <div class="m-4 flex felx-row justify-between items-center" v-permission="permissionList.images">
    <div class="flex felx-row justify-start items-center">
      <el-button type="" @click="reset">重置</el-button>
      <el-button type="primary" @click="select">查询</el-button>
    </div>
    <div class="flex flex-row justify-start items-center">
      <div v-if="batchMoveFlag" class="mr-4">
        <el-button @click="batchMoveSave" type="primary">保存顺序</el-button>
      </div>
      <div v-if="batchMoveFlag" class="mr-4">
        <el-button @click="batchMoveCancel" type="default">取消移动</el-button>
      </div>
      <div v-if="!batchDeleteFlag && !batchMoveFlag" class="mr-4" v-permission="permissionList.imagesUpdate">
        <el-button @click="willBatchMove">批量移动</el-button>
      </div>
      <div v-if="batchDeleteFlag" class="mr-4 flex flex-row justify-start items-center">
        <el-checkbox label="全选" v-model="checkAllFlag" @change="checkAll"></el-checkbox>
      </div>
      <div v-if="batchDeleteFlag" class="mr-4">
        <el-button @click="batchDelete" type="danger">删除</el-button>
      </div>
      <div v-if="batchDeleteFlag" class="mr-4">
        <el-button @click="batchDeleteCancel" type="default">取消</el-button>
      </div>
      <div v-if="!batchDeleteFlag && !batchMoveFlag" class="mr-4" v-permission="permissionList.imagesDelete">
        <el-button @click="willBatchDelete">批量删除</el-button>
      </div>
      <div class="flex flex-row justify-start items-center">
        <span class="mr-2">预览大小:</span>
        <el-radio-group v-model="imageSize">
          <el-radio-button label="小" value="small" />
          <el-radio-button label="中" value="middle" />
          <el-radio-button label="大" value="large" />
        </el-radio-group>
      </div>
    </div>

  </div>

  <!-- 列表 -->
  <div class="m-4" v-loading="tableLoading" v-permission="permissionList.images">
    <el-scrollbar class="h-[calc(100vh-20rem)]">
      <div ref="dragRef">
        <div v-for="item,index in pageInfo?.list" :key="item.id" :imageId="item.id" :draggable="batchMoveFlag?true:false">
          <div :class="['select-none', 'imgCard', 'relative', 
          getImageSize().cardW, getImageSize().cardH, 
          'flex', 'flex-col', 'justify-center', 'items-center', 
          'float-start', 'm-2', 'border-[1px]']" shadow="hover">
            
            <!-- 删除按钮 -->
            <div v-if="imageSize != 'small' && !batchDeleteFlag && !batchMoveFlag" @click="deleteImage(item.id)" v-permission="permissionList.imagesDelete"
              class="cursor-pointer imgMain absolute hidden z-[60] top-0 right-0 bg-slate-500 hover:bg-slate-400 rounded-bl-lg p-1">
              <svg class="w-6 h-6" t="1741068999823" viewBox="0 0 1024 1024" version="1.1"
                xmlns="http://www.w3.org/2000/svg" p-id="2329">
                <path class=""
                  d="M202.666667 256h-42.666667a32 32 0 0 1 0-64h704a32 32 0 0 1 0 64H266.666667v565.333333a53.333333 53.333333 0 0 0 53.333333 53.333334h384a53.333333 53.333333 0 0 0 53.333333-53.333334V352a32 32 0 0 1 64 0v469.333333c0 64.8-52.533333 117.333333-117.333333 117.333334H320c-64.8 0-117.333333-52.533333-117.333333-117.333334V256z m224-106.666667a32 32 0 0 1 0-64h170.666666a32 32 0 0 1 0 64H426.666667z m-32 288a32 32 0 0 1 64 0v256a32 32 0 0 1-64 0V437.333333z m170.666666 0a32 32 0 0 1 64 0v256a32 32 0 0 1-64 0V437.333333z"
                  fill="#ffffff" p-id="2330">
                </path>
              </svg>
            </div>

            <!-- 图片主体 -->
            <el-image draggable="false" v-if="item.imageType == ImageType.url" 
            fit="contain" :preview-src-list="batchMoveFlag?[]:[item.url]" :src="item.url" :hide-on-click-modal="true"
            :class="[getImageSize().imageW, getImageSize().imageH, 'rounded-lg']"></el-image>
            <div v-if="item.imageType == ImageType.svg" 
            :class="[getImageSize().imageW, getImageSize().imageH, 'rounded-lg', 
            'flex', 'flex-row', 'justify-center', 'items-center']"
            >
              <div v-html="item.svgCode"></div>
            </div>
            <el-image draggable="false" v-if="item.imageType == ImageType.base64" 
            fit="contain" :preview-src-list="batchMoveFlag?[]:[item.url]" :src="item.base64Code" :hide-on-click-modal="true"
            :class="[getImageSize().imageW, getImageSize().imageH, 'rounded-lg']"></el-image>

            <!-- 名称 -->
            <div v-if="imageSize != 'small'">
              <!-- <el-tooltip effect="dark" :content="item.attname" placement="bottom"> -->
                <div :class="[getImageSize().imageW, 'overflow-hidden', 'whitespace-nowrap', 'text-ellipsis']">
                  <span :class="['select-none', 'text-xs']">{{ item.attname }}</span>
                </div>
              <!-- </el-tooltip> -->
            </div>

            <!-- 修改名称 -->
            <div v-if="imageSize != 'small' && !batchDeleteFlag 
              && item.imageType == ImageType.url && !batchMoveFlag" 
            @click="willUpdateAttname(item)" v-permission="permissionList.imagesUpdate"
            class="cursor-pointer imgMain hidden absolute z-50 bottom-0 right-0 
          bg-blue-400 hover:bg-blue-300 rounded-tl-lg pl-1 pt-1 pr-1">
              <div class="flex flex-row">
                <span class="text-[10px] text-white">修改名称</span>
              </div>
            </div>

            <!-- 复制链接 -->
            <div v-if="!batchDeleteFlag && !batchMoveFlag" class="cursor-pointer w-full imgMain hidden 
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

            <!-- 向上移动 -->
            <div v-if="imageSize != 'small' && !batchDeleteFlag && !batchMoveFlag" v-permission="permissionList.imagesMove"
              class="cursor-pointer imgMain hidden absolute z-50 h-full top-0 left-0">
              <div class="flex flex-col justify-center h-full items-center">
                <div class="w-4 h-4 bg-white hover:bg-gray-300 rounded-sm" @click="moveUp(item.id)">
                  <svg t="1741080058089" class="icon " viewBox="0 0 1024 1024" version="1.1"
                    xmlns="http://www.w3.org/2000/svg" p-id="5098">
                    <path
                      d="M80 384h752.512L586.496 144.064l44.672-45.824 298.944 291.584a33.92 33.92 0 0 1-23.68 58.24H80V384z m864 256H193.28l243.84 230.4-43.904 46.528-298.88-282.368A33.92 33.92 0 0 1 117.76 576H944v64z"
                      p-id="5099" fill="#000000"></path>
                  </svg>
                </div>
              </div>
            </div>
            <!-- 向下移动 -->
            <div v-if="imageSize != 'small' && !batchDeleteFlag && !batchMoveFlag" v-permission="permissionList.imagesMove"
              class="cursor-pointer imgMain hidden absolute z-50 h-full top-0 right-0">
              <div class="flex flex-col justify-center h-full items-center ">
                <div class="w-4 h-4 bg-white hover:bg-gray-300 rounded-sm" @click="moveDown(item.id)">
                  <svg t="1741080058089" class="icon " viewBox="0 0 1024 1024" version="1.1"
                    xmlns="http://www.w3.org/2000/svg" p-id="5098">
                    <path
                      d="M80 384h752.512L586.496 144.064l44.672-45.824 298.944 291.584a33.92 33.92 0 0 1-23.68 58.24H80V384z m864 256H193.28l243.84 230.4-43.904 46.528-298.88-282.368A33.92 33.92 0 0 1 117.76 576H944v64z"
                      p-id="5099" fill="#000000"></path>
                  </svg>
                </div>
              </div>
            </div>

            <!-- 批量删除选中 -->
            <div v-if="batchDeleteFlag" 
            class="cursor-pointer absolute z-0 bottom-0 left-0 h-full w-full"
            :class="[batchDeleteFlag?'z-[80]':'z-0']"
            >
              <div @click="deleteCheck(item)" class="flex flex-col justify-center w-full h-full items-start">
                <el-checkbox size="large" v-model="item.isCheck"></el-checkbox>
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
  <!-- 分页 -->
  <div class="m-4 flex flex-row justify-end clear-start" v-permission="permissionList.images">
    <el-pagination 
    background 
    v-model:current-page="pageRequest.pageNum" 
    v-model:page-size="pageRequest.pageSize"
    layout="total, sizes, prev, pager, next, jumper" 
    :page-sizes="[10,20,50,100,500,1000,2000]"
    :page-count="Number(pageInfo?.totalPage)"
    :total="Number(pageInfo?.total)" 
    @size-change="select" 
    @current-change="select" 
    prev-text="上一页" next-text="下一页">
    </el-pagination>
  </div>

  <InnerDialog v-model="updateAttnameDialogVisible" :width="800">
    <template #header>
      修改名称
    </template>
    <template #default>
      <div class="m-4">
        <div>
          <div class="flex flex-row justify-start items-center m-4">
            <div class="w-20 required">图片名称: </div>
            <el-input class="w-[30rem]" v-model="updateAttname" placeholder="请输入图片名称" />
          </div>
        </div>
      </div>
    </template>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="updateAttnameCancel">取消</el-button>
        <el-button type="primary" @click="updateAttnameSave">保存</el-button>
      </div>
    </template>
  </InnerDialog>

</template>

<script setup lang="ts" name="KeyValue">
import InnerDialog from '@/components/utils/InnerDialog.vue'
import QiniuUpload from "@/components/utils/QiniuUpload.vue";
import { useImagesHooks } from './ImagesHooks';
import {ImageType} from "./ImageTypes"

let {
  permissionList,

  uploadImage,
  uploadSvgDialogVisible,
  uploadBase64DialogVisible,
  willUploadSvg,
  willUploadBase64,
  uploadSvgCancel,
  uploadBase64Cancel,
  uploadSvgCode,
  uploadBase64Code,
  uploadSvgSave,
  uploadBase64Save,

  selectAttname,
  selectImageType,

  pageRequest,
  select,
  pageInfo,
  reset,
  tableLoading,
  imageSize,
  getImageSize,

  deleteImage,
  batchDelete,
  batchDeleteFlag,
  willBatchDelete,
  batchDeleteCancel,

  copyImage,

  willUpdateAttname,
  updateAttnameDialogVisible,
  updateAttname,
  updateAttnameCancel,
  updateAttnameSave,

  moveDown,
  moveUp,
  change,

  checkAll,
  checkAllFlag,
  deleteCheck,

  getImageTagType,

  batchMoveFlag,
  batchMoveSave,
  willBatchMove,
  batchMoveCancel,
} = useImagesHooks();
</script>

<style scoped>
.imgCard:hover .imgMain {
  display: block;
}
.moving{
  border: 1px dashed #ccc;
}
.moving > *{
  opacity: 0.0;
}
</style>
