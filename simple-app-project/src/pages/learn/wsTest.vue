<template>
  <view class="wsTest">wsTest</view>
  <view class="h-[500px] overflow-scroll flex flex-col">
      <view v-for="item in messageList">{{item}}</view>
  </view>
  <button @click="testws">测试</button>
  <button @click="clean">清空</button>
  <button @click="testReopen">测试主动断开链接</button>
</template>

<script setup lang="ts">
import { type R } from '@/types/R';
import { request } from '@/utils/Request';
import { useWsServiceHooks } from '@/utils/websocket/WsServiceHooks';
import { ref } from 'vue';

let messageList = ref<string[]>([])

useWsServiceHooks("appTestMessage", async()=>{
    let r:R<number[]> = await request.post("/test/appUnread")
    for(let i of r.data){
        messageList.value.push(String(i))
    }
})

const testws = ()=>{
   request.post("/test/appTestws")
}

const clean = ()=>{
    messageList.value = []
}

const testReopen = ()=>{
    request.post("/test/apptestwsClose")
}

const testReopen2 = ()=>{
    
}

</script>

<style scoped></style>