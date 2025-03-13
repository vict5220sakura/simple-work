<template>
  <view class="h-14 max-h-14 min-h-14 flex flex-row items-center justify-around bg-[#e5e5e5]">
    <view class="tabContent" @click="toHome">
      <view>
        <image class="w-4 h-4"
        :src="currentPage=='pages/home/home'?'/static/images/home_active.png':'/static/images/home.png'"></image>
      </view>
      <view>首页</view>
    </view>
    <view class="tabContent" @click="toMy">
      <view>
        <image class="w-4 h-4"
        :src="currentPage=='pages/my/my'?'/static/images/my_active.png':'/static/images/my.png'"></image>
      </view>
      <view>我的</view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';


let currentPage = ref<string>()

const toHome = ()=>{
  uni.redirectTo({
    url:'/pages/home/home'
  })
}
const toMy = ()=>{
  uni.redirectTo({
    url:'/pages/my/my'
  })
}

onMounted(()=>{
  let pages = getCurrentPages()
  let page = pages[0]
  currentPage.value = page.route
})
</script>

<style scoped>
  .tabContent{
    @apply w-full flex flex-col items-center
  }
  /* .tabContent:active{
    @apply w-full flex flex-col items-center bg-gray-400
  } */
</style>
