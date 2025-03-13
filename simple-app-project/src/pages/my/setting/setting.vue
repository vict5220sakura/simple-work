<template>
  <uv-button @click="loginOut">登出</uv-button>
</template>

<script setup lang="ts">
import { useLoginStore } from "@/pages/login/loginStore"
import { storeToRefs } from "pinia"


let loginStore = useLoginStore()
let {aToken} = storeToRefs(loginStore)

const loginOut = ()=>{
  aToken.value = undefined
  uni.reLaunch({url: "/pages/login/login"})
}

</script>

<style scoped></style>