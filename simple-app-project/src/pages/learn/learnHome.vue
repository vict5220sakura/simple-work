<template>
  <view>
    <button @click="jumpTo('/pages/learn/learnWebView')">learnWebView</button>
    <button @click="jumpTo('/pages/learn/learnUvUi')">learnUvUi</button>
    <button @click="jumpTo('/pages/learn/learnPageFullHeight')">全屏测试</button>
    <button @click="jumpTo('/pages/learn/learnEnv')">多环境测试</button>
    <button @click="jumpTo('/pages/learn/learnRequest')">网络请求测试</button>
    <button @click="jumpTo('/pages/learn/wsTest')">ws测试</button>
    <button @click="jumpTo('/pages/learn/logTest')">log测试</button>
  </view>
</template>

<script setup lang="ts">
const jumpTo = (path:string)=>{
  uni.navigateTo({
    url:path
  })
}
</script>

<style scoped></style>