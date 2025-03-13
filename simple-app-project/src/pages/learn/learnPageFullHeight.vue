<template>
  <view class="status_bar">
    <!-- 这里是状态栏 -->
  </view>
  <scroll-view scroll-y class="mybody">
    <view class="">
      <view class="">learnPageFullHeight</view>
      <view class="" v-for="(item,index) in 100">1</view>
      <uv-input placeholder="请输入内容" border="surround" v-model="value"></uv-input>
      <view class="">底部</view>
    </view>
  </scroll-view>
  
  <view class="fixed bottom-0 w-full"><tabBar></tabBar></view>
</template>

<script setup lang="ts">
import tabBar from "@/components/tabBar.vue"
import { ref } from "vue";

let value = ref()

</script>

<style scoped>
.mybody{
  height: calc(100vh - 3.5rem - var(--status-bar-height))
}
</style>