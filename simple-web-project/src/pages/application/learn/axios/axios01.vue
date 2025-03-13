<template>
    <div>axios01</div>
    <el-button @click="testController">test接口请求</el-button>
    
</template>

<script setup lang="ts" name="axios01">
import axios from 'axios'

let testController = async()=>{
    let r = await axios.post("http://localhost:30001/simple-server/test/test")
    console.log("r:", r.data.data)
}

</script>

<style scoped></style>
