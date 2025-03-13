<template>
    <div class="h-[500px] overflow-scroll flex flex-col">
        <span v-for="item in messageList">{{item}}</span>
    </div>
    <el-button @click="testws">测试</el-button>
    <el-button @click="clean">清空</el-button>
    <el-button @click="testReopen">测试主动断开链接</el-button>
</template>

<script setup lang="ts" name="testWs">
import { ref} from "vue";
import { request } from "@/utils/Request";
import type { R } from "@/types/R";
import {useWsServiceHooks} from "@/utils/websocket/WsServiceHooks"

useWsServiceHooks("testMessage", async()=>{
    let r:R<number[]> = await request.post("/test/unread")
    for(let i of r.data){
        messageList.value.push(String(i))
    }
})

let messageList = ref<string[]>([])

const testws = ()=>{
   request.post("/test/testws")
}

const clean = ()=>{
    messageList.value = []
}

const testReopen = ()=>{
    request.post("/test/testwsClose")
}

</script>

<style scoped></style>
