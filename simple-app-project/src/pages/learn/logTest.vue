<template>
  <view>
    <button @click="logTest">日志测试</button>
  </view>
</template>

<script setup lang="ts">
import { log } from "@/utils/AppLog";
const logTest = ()=>{
  log('logTest')
}
</script>

<style scoped></style>