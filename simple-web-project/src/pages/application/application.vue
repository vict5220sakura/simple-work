<template>
    <div class="h-full w-full flex flex-col">
        <div class="w-full h-[48px] p-2 flex flex-row justify-between">
            <div class="text-1xl text-bootstrap_info">
                SIMPLE_PROJECT 简单高效 极致便捷
            </div>
            <div>
                <el-button @click="toApp">打开APP(H5)</el-button>
                <el-button @click="loginOut">登出</el-button>
                <!-- <el-button @click="flushPermission">刷新权限</el-button>
                <el-button @click="clearCache">清除缓存</el-button> -->
            </div>
        </div>
        <div class="w-full h-0 flex-1 flex flex-row">
            <div id="menu" :class='["transition-width", "duration-500", isCollapse?"w-[64px]":"w-[200px]", "bg-[#545c64]", "menu"]'>
                <el-scrollbar :class='["transition-width", "duration-500", isCollapse?"w-[64px]":"w-[200px]"]'>
                    <el-menu 
                        :collapse="isCollapse"
                        :default-active="activeMenuIndex"
                        ref="menuRef"
                        class="" 
                        active-text-color="#ffd04b" 
                        background-color="#545c64" 
                        text-color="#fff" 
                        @select="menuSelect"
                    >
                        <MenuItem v-for="menu in menus" :menu="menu" :key="menu.id" ></MenuItem>
                    </el-menu>
                </el-scrollbar>
                <div :class='["bottom-0", "fixed", "transition-width", "duration-500", isCollapse?"w-[64px]":"w-[200px]", "flex", "justify-end", "p-1"]'>
                    <div v-if="!isCollapse" class="p-1 flex flex-row items-center bg-slate-500 rounded cursor-pointer select-none" @click="menuMin">
                        <div class="">
                            <svg t="1739494069383" class="icon" viewBox="0 0 1204 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4193" width="14" height="14"><path d="M1114.352941 0h-1024a90.352941 90.352941 0 0 0 0 180.705882h1024a90.352941 90.352941 0 0 0 0-180.705882z m0 421.647059h-602.352941a90.352941 90.352941 0 0 0 0 180.705882h602.352941a90.352941 90.352941 0 0 0 0-180.705882zM0 512L301.176471 662.588235V361.411765L0 512zM1114.352941 843.294118h-1024a90.352941 90.352941 0 0 0 0 180.705882h1024a90.352941 90.352941 0 0 0 0-180.705882z" fill="#ffffff" p-id="4194" data-spm-anchor-id="a313x.search_index.0.i3.274e3a81Y2ELAa" class="selected"></path></svg>
                        </div>
                        &nbsp;<span class="text-[#fff] text-xs">缩起</span>
                    </div>
                    <div v-if="isCollapse" class="p-1 flex flex-row items-center bg-slate-500 rounded cursor-pointer select-none" @click="menuMax">
                        <div class="rotate-180">
                            <svg t="1739494069383" class="icon" viewBox="0 0 1204 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4193" width="14" height="14"><path d="M1114.352941 0h-1024a90.352941 90.352941 0 0 0 0 180.705882h1024a90.352941 90.352941 0 0 0 0-180.705882z m0 421.647059h-602.352941a90.352941 90.352941 0 0 0 0 180.705882h602.352941a90.352941 90.352941 0 0 0 0-180.705882zM0 512L301.176471 662.588235V361.411765L0 512zM1114.352941 843.294118h-1024a90.352941 90.352941 0 0 0 0 180.705882h1024a90.352941 90.352941 0 0 0 0-180.705882z" fill="#ffffff" p-id="4194" data-spm-anchor-id="a313x.search_index.0.i3.274e3a81Y2ELAa" class="selected"></path></svg>
                        </div>
                        &nbsp;<span class="text-[#fff] text-xs">展开</span>
                    </div>
                </div>
            </div>
            <div class="flex-1 w-0">
                <div class="w-full h-full flex flex-col">
                    <div class="w-full h-[30px] overflow-x-auto overflow-y-hidden" id="tagDiv" ref="tagRef">
                        <HeadTag></HeadTag>
                    </div>
                    <div ref="context" 
                        id="context"
                        :class="'w-full h-full relative ' + (lockContextFlag ? 'overflow-hidden' : 'overflow-hidden')">
                        <el-scrollbar 
                            ref="contextScroll" 
                            @scroll="scrollEvent"
                        >

                            <RouterView v-slot="{ Component }">
                                <component :is="Component"></component>
                                <!-- <KeepAlive>
                                </KeepAlive> -->
                            </RouterView>
                        </el-scrollbar>
                    </div>
                </div>
            </div>
        </div>
    </div>

</template>

<script setup lang="ts" name="application">
import MenuItem from '@/components/menu/MenuItem.vue'
import HeadTag from '@/components/headtag/HeadTag.vue'

import {useApplicationHooks} from "./applicationHooks"
import { emitter } from '@/utils/Emitter'
import { onUnmounted, ref } from 'vue'
import { MyWsSocket } from '@/utils/websocket/WebSocketUtils'
import { useLoginStore } from '../login/LoginStore'
import {storeToRefs} from "pinia"
import { randomId } from '@/utils/IdUtils'

let {
    loginOut,
    clearCache,
    menuSelect,
    menus,
    menuRef,
    flushPermission,
    lockContextFlag,
    activeMenuIndex,
    scrollEvent,
    isCollapse,
    menuMin,
    menuMax,
    toApp
} = useApplicationHooks()



</script>

<style scoped>
.menu > .el-scrollbar__wrap{
  overflow-x: hidden !important;
}        
.menu > .el-scrollbar__view{
  overflow-x: hidden !important;
}
</style>@/store/bUser/LoginStore@/components/headtag/HeadTag.vue