<template>
    测试上传
    <QiniuUpload @getUrl="getUrl">
        <el-button>测试上传</el-button>
    </QiniuUpload>
    <img :src="url">
</template>

<script setup lang="ts" name="testUpload">
import QiniuUpload from "@/components/utils/QiniuUpload.vue";
import {ref} from "vue"

let url = ref<string>()

function getUrl(urlDto:string){
    console.log(urlDto)
    url.value = urlDto
}

</script>

<style scoped></style>
