<template>
    <div><el-input type="text" class="w-32" v-model="textVal"/></div>
    <div><el-button @click="testOpen">新开窗口测试</el-button></div>
    <div><el-button @click="testOpen2">新开窗口测试2</el-button></div>
    <div><el-button @click="testOpen3">新开窗口测试3 自定义窗口大小</el-button></div>
    <div><el-button @click="testOpen4">新开窗口测试4 title</el-button></div>
    <div><el-button @click="openNewWinTest">openNewWin</el-button></div>
    <div><el-button @click="openNewTabTest">openNewTab</el-button></div>
    <div><el-button @click="openNewWin2Test">openNewWin2</el-button></div>
    <div><el-button @click="openNewTab2Test">openNewTab2</el-button></div>
</template>

<script setup lang="ts" name="testPage1">
import { ref, onMounted } from 'vue';
import {RouterUtils} from '@/router/RouterUtils';
import {randomId} from "@/utils/IdUtils"

let textVal = ref();

function testOpen(){
    RouterUtils.openNewWin({
        path: '/testOpenPages1',
        query: {"a": randomId()}
    }, {
        width: 500,
        height: 500,
        top: 100,
        left: 100
    })
}
function testOpen2(){
    RouterUtils.openNewWin({
        path: '/testOpenPages2',
        query: {"a": randomId()}
    },{
        width: 500,
        height: 500,
        top: 100,
        left: 100
    })
}

function testOpen3(){
    RouterUtils.openNewWin({
        path: '/testOpenPages2',
        query: {"a": randomId()}
    },{
        width: 500,
        height: 500,
        top: 100,
        left: 100,
        title: "测试窗口"
    })
}
function testOpen4(){
    RouterUtils.openNewWin2({
        path: '/testOpenPages2',
        query: {"a": randomId()}
    },{
        width: 500,
        height: 500,
        top: 100,
        left: 100,
        title: "测试"
    })
}

function openNewWinTest(){
    RouterUtils.openNewWin({
        path: '/testOpenPages2',
        query: {"a": randomId()}
    },{
        width: 500,
        height: 500,
        top: 100,
        left: 100,
        title: "测试"
    })
}
function openNewTabTest(){
    RouterUtils.openNewTab({
        path: '/testOpenPages2',
        query: {"a": randomId()}
    },{
        width: 500,
        height: 500,
        top: 100,
        left: 100,
        title: "测试"
    })
}
function openNewWin2Test(){
    RouterUtils.openNewWin2({
        path: '/testOpenPages2',
        query: {"a": randomId()}
    },{
        width: 500,
        height: 500,
        top: 100,
        left: 100,
        // title: "测试"
    })
}
function openNewTab2Test(){
    RouterUtils.openNewTab2({
        path: '/testOpenPages2',
        query: {"a": randomId()}
    },{
        width: 500,
        height: 500,
        top: 100,
        left: 100,
        // title: "测试"
    })
}

</script>

<style scoped></style>
@/router/RouterUtils