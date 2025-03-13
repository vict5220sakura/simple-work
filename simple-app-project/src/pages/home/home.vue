<template>
  <view class="status_bar">
    <!-- 这里是状态栏 -->
  </view>

  <view class="bgImg mybody">
    <rich-text :nodes="data"></rich-text>
  </view>
  <!-- <view calss="h-full w-full">
    <uv-image 
    :fade="true" duration="450" height="1024px" width="516"
    src="http://file.vict5220.top/alWEOy5aFS__2025_03_13__14_41_07.jpeg?attname=Designer.jpeg"></uv-image>

  </view> -->
  <view class="fixed bottom-0 w-full"><tabBar></tabBar></view>
</template>

<script setup lang="ts">
import tabBar from "@/components/tabBar.vue"
import type { KeyValue } from "@/types/KeyValueType";
import { keyValueGetByKey } from "@/utils/KeyValueUtils";
import { onMounted, ref } from "vue";

let data = ref<any>();
onMounted(async()=>{
  let keyValue:KeyValue = await keyValueGetByKey("app_home");
  data.value = keyValue.value1
})

</script>

<style scoped>
.mybody{
  height: calc(100vh - 3.5rem - var(--status-bar-height))
}
.bgImg{
  background-image: url("http://file.vict5220.top/alWEOy5aFS__2025_03_13__14_41_07.jpeg?attname=Designer.jpeg");
  background-repeat: no-repeat;
  background-position: center;
  background-size: cover;
  background-attachment: fixed;
}
</style>
