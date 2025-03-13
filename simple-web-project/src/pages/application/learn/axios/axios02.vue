<template>
    <div>axios02</div>
    <el-button @click="testController">test接口请求</el-button>
    <el-button @click="loginTest">loginTest</el-button>
    
</template>

<script setup lang="ts" name="axios02">
import {request} from '@/utils/Request'
import { onMounted } from 'vue'

let testController = async()=>{
    let r = await request.post("/test/test")
    console.log("r:", r)
    
}

let loginTest = async()=>{
    await request.post("/bUserLogin/loginTest")
}

onMounted(async()=>{
    await request.post("/bUserLogin/loginTest")
})

</script>

<style scoped></style>
@/utils/Request