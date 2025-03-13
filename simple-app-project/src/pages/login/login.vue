<template>
  <view class="h-[100vh] flex flex-col justify-center items-center">
    <view class="w-60 flex flex-row justify-center items-center">
        <view class="text-gray-600 text-sm w-16">手机号:</view>
        <view><uv-input placeholder="手机号" border="surround" v-model="phone" ></uv-input></view>
    </view>
    <view class="w-60 pt-2 flex flex-row justify-center items-center">
        <view class="text-gray-600 text-sm w-16">密码:</view>
        <view><uv-input password placeholder="密码" border="surround" v-model="password" ></uv-input></view>
    </view>
    <view class="w-60 flex flex-row justify-center items-center pt-2">
        <view class="w-full"><uv-button type="primary" size="" @click="login">登录</uv-button></view>
    </view>
    <view class="w-60 flex flex-row justify-center items-center pt-2">
        <view class="w-full"><uv-button type="primary" size="" @click="testLogin">体验登录</uv-button></view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { request } from '@/utils/Request';
import { ref } from 'vue';
import {md5} from "js-md5";
import type { R } from '@/types/R';
import { useLoginStore } from './loginStore';
import { storeToRefs } from 'pinia';
import { onMounted, onUnmounted } from "vue";
import { useCustomerStore } from '@/pages/my/customerStore';

let loginStore = useLoginStore()
let {aToken} = storeToRefs(loginStore)

let phone = ref<string>("")
let password = ref<string>("")

const login = async()=>{
    let r:R<any> = await request.post('/customerLogin/appLoginIn',{
        phone: phone.value,
        password: md5(password.value)
    })
    if(r.code == "success"){
        uni.showToast({
            title: "登录成功",
            duration: 1000,
            position: 'bottom',
            icon: 'success'
          });
        uni.reLaunch({ url: '/pages/home/home' })
        aToken.value = r.data.aToken
    }
}

const testLogin = async()=>{
    let r:R<any> = await request.post('/customerLogin/appLoginIn',{
        phone: "13111111111",
        password: md5("123456")
    })
    if(r.code == "success"){
        uni.showToast({
            title: "登录成功",
            duration: 1000,
            position: 'bottom',
            icon: 'success'
          });
        uni.reLaunch({ url: '/pages/home/home' })
        aToken.value = r.data.aToken
    }
}

let customerStore = useCustomerStore()
let {isAppLogin} = customerStore

onMounted(async()=>{
    console.log("login onMounted")
    if(await isAppLogin()){
        uni.redirectTo({ url: '/pages/home/home' })
    }
})

</script>

<style></style>