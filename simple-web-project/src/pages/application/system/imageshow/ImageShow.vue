<template>
    <div class="m-4">
        <div class="mb-4">图片查看</div>
        <div class="mb-4">
            <el-input clearable v-model="imageUrl" class="w-80" placeholder="请输入图片地址"></el-input>
        </div>
        <el-image :src="imageUrl"></el-image>
    </div>
</template>

<script setup lang="ts" name="ImageShow">
import {useImageShowStore} from '@/pages/application/system/imageshow/ImageShowStore'
import {storeToRefs} from 'pinia'

let imageShowStore = useImageShowStore();
let {
    imageUrl
} = storeToRefs(imageShowStore);
</script>

<style scoped></style>
@/store/application/pages/system/ImageShowStore@/pages/application/pages/system/ImageShowStore@/pages/application/system/imageshow/ImageShowStore