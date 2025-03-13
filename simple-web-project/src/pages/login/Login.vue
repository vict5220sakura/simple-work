<template>
    <div class="flex flex-col justify-center items-center w-full h-full fixed">
        <div>
            <el-input name="bUserCode" class="mb-2" type="text" placeholder="请输入账号" 
                v-model="bUserCode" @keyup.enter="login"></el-input>
            <el-input name="password" class="mb-2" type="password" placeholder="请输入密码" 
                v-model="password" @keyup.enter="login"></el-input>
        </div>
        <div>
            <el-button type="primary" @click="login">登录</el-button>
            <el-button type="primary" @click="loginTest">体验登录</el-button>
        </div>
    </div>
    <div v-html="loginPageValue" class="max-w-full max-h-full overflow-hidden"></div>
    <div class="fixed bottom-0 max-w-full min-w-full w-full max-h-12 flex justify-center items-center">
        <el-link class="text-lg bg-gray-200" href="https://beian.miit.gov.cn/#/Integrated/index" target="_blank">蒙ICP备2021005294号</el-link>
    </div>
</template>


<script setup lang="ts" name="login">

import {useLoginHooks} from "./LoginHooks"

let {
    bUserCode,
    password,
    login,
    loginPageValue,
    loginTest,
    ICPValue
} = useLoginHooks()

</script>


<style scoped>

</style>@/store/bUser/LoginStore./LoginHooks