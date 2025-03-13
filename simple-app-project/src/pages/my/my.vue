<template>
  <!-- debug选项 -->
  <view v-if="appConfig?.debugMode == DebugMode.DebugMode_yes">{{ appConfig?.debugMode == DebugMode.DebugMode_yes ?
    "debug模式" : "" }}</view>
  <view v-if="appConfig?.debugMode == DebugMode.DebugMode_yes">
    <navigator url="/pages/learn/learnHome" hover-class="navigator-hover">
      <button type="default">Debug页面</button>
    </navigator>
  </view>

  <scroll-view scroll-y class="mybody">

    <view class="h-28">
      <!-- 设置行 -->
      <view class="flex flex-row justify-end">
        <navigator class="" url="/pages/my/setting/setting">
          <image class="p-0 h-10 w-10"
            src="data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSI2NCIgaGVpZ2h0PSI2NCIgY2xhc3M9Imljb24iIHZpZXdCb3g9IjAgMCAxMDI0IDEwMjQiPjxwYXRoIGZpbGw9IiMyYzJjMmMiIGQ9Ik01MTIgMzI4Yy0xMDAuOCAwLTE4NCA4My4yLTE4NCAxODRzODMuMiAxODQgMTg0IDE4NCAxODQtODMuMiAxODQtMTg0LTgzLjItMTg0LTE4NC0xODRtMCAzMjBjLTc1LjIgMC0xMzYtNjAuOC0xMzYtMTM2czYwLjgtMTM2IDEzNi0xMzYgMTM2IDYwLjggMTM2IDEzNi02MC44IDEzNi0xMzYgMTM2Ii8+PHBhdGggZmlsbD0iIzJjMmMyYyIgZD0iTTg1Ny42IDU3Mi44QzgzNi44IDU2MCA4MjQgNTM3LjYgODI0IDUxMnMxMi44LTQ2LjQgMzMuNi02MC44YzE0LjQtOS42IDIwLjgtMjcuMiAxNi00NC44LTgtMjcuMi0xOS4yLTUyLjgtMzItNzYuOC04LTE0LjQtMjUuNi0yNC00My4yLTE5LjItMjQgNC44LTQ4LTEuNi02NS42LTE5LjItMTcuNi0xNy42LTI0LTQxLjYtMTkuMi02NS42IDMuMi0xNi00LjgtMzMuNi0xOS4yLTQzLjItMjQtMTQuNC01MS4yLTI0LTc2LjgtMzItMTYtNC44LTM1LjIgMS42LTQ0LjggMTZDNTYwIDE4Ny4yIDUzNy42IDIwMCA1MTIgMjAwcy00Ni40LTEyLjgtNjAuOC0zMy42Yy05LjYtMTQuNC0yNy4yLTIwLjgtNDQuOC0xNi0yNy4yIDgtNTIuOCAxOS4yLTc2LjggMzItMTQuNCA4LTI0IDI1LjYtMTkuMiA0My4yIDQuOCAyNC0xLjYgNDkuNi0xOS4yIDY1LjYtMTcuNiAxNy42LTQxLjYgMjQtNjUuNiAxOS4yLTE2LTMuMi0zMy42IDQuOC00My4yIDE5LjItMTQuNCAyNC0yNCA1MS4yLTMyIDc2LjgtNC44IDE2IDEuNiAzNS4yIDE2IDQ0LjhDMTg3LjIgNDY0IDIwMCA0ODYuNCAyMDAgNTEycy0xMi44IDQ2LjQtMzMuNiA2MC44Yy0xNC40IDkuNi0yMC44IDI3LjItMTYgNDQuOCA4IDI3LjIgMTkuMiA1Mi44IDMyIDc2LjggOCAxNC40IDI1LjYgMjIuNCA0My4yIDE5LjIgMjQtNC44IDQ5LjYgMS42IDY1LjYgMTkuMiAxNy42IDE3LjYgMjQgNDEuNiAxOS4yIDY1LjYtMy4yIDE2IDQuOCAzMy42IDE5LjIgNDMuMiAyNCAxNC40IDUxLjIgMjQgNzYuOCAzMiAxNiA0LjggMzUuMi0xLjYgNDQuOC0xNkM0NjQgODM2LjggNDg2LjQgODI0IDUxMiA4MjRzNDYuNCAxMi44IDYwLjggMzMuNmM4IDExLjIgMjAuOCAxNy42IDMzLjYgMTcuNiAzLjIgMCA4IDAgMTEuMi0xLjYgMjcuMi04IDUyLjgtMTkuMiA3Ni44LTMyIDE0LjQtOCAyNC0yNS42IDE5LjItNDMuMi00LjgtMjQgMS42LTQ5LjYgMTkuMi02NS42IDE3LjYtMTcuNiA0MS42LTI0IDY1LjYtMTkuMiAxNiAzLjIgMzMuNi00LjggNDMuMi0xOS4yIDE0LjQtMjQgMjQtNTEuMiAzMi03Ni44IDQuOC0xNy42LTEuNi0zNS4yLTE2LTQ0LjhtLTU2IDkyLjhjLTM4LjQtNi40LTc2LjggNi40LTEwNCAzMy42LTI3LjIgMjcuMi00MCA2NS42LTMzLjYgMTA0LTE3LjYgOS42LTM2LjggMTcuNi01NiAyNC0yMi40LTMwLjQtNTcuNi00OS42LTk3LjYtNDkuNi0zOC40IDAtNzMuNiAxNy42LTk3LjYgNDkuNi0xOS4yLTYuNC0zOC40LTE0LjQtNTYtMjQgNi40LTM4LjQtNi40LTc2LjgtMzMuNi0xMDQtMjcuMi0yNy4yLTY1LjYtNDAtMTA0LTMzLjYtOS42LTE3LjYtMTcuNi0zNi44LTI0LTU2IDMwLjQtMjIuNCA0OS42LTU3LjYgNDkuNi05Ny42IDAtMzguNC0xNy42LTczLjYtNDkuNi05Ny42IDYuNC0xOS4yIDE0LjQtMzguNCAyNC01NiAzOC40IDYuNCA3Ni44LTYuNCAxMDQtMzMuNiAyNy4yLTI3LjIgNDAtNjUuNiAzMy42LTEwNCAxNy42LTkuNiAzNi44LTE3LjYgNTYtMjQgMjIuNCAzMC40IDU3LjYgNDkuNiA5Ny42IDQ5LjYgMzguNCAwIDczLjYtMTcuNiA5Ny42LTQ5LjYgMTkuMiA2LjQgMzguNCAxNC40IDU2IDI0LTYuNCAzOC40IDYuNCA3Ni44IDMzLjYgMTA0IDI3LjIgMjcuMiA2NS42IDQwIDEwNCAzMy42IDkuNiAxNy42IDE3LjYgMzYuOCAyNCA1NkM3OTUuMiA0MzYuOCA3NzYgNDcyIDc3NiA1MTJjMCAzOC40IDE3LjYgNzMuNiA0OS42IDk3LjYtNi40IDE5LjItMTQuNCAzOC40LTI0IDU2Ii8+PC9zdmc+">
            <!-- src="http://file.vict5220.top/QMCBgsIxp3__2025_03_05__15_51_59.svg?attname=%E8%AE%BE%E7%BD%AE.svg"> -->
          </image>
        </navigator>
      </view>

      <!-- 我的头像 名称 手机号 -->
      <view class="flex flex-row justify-start items-start">
        <view>
          <image class="pl-2 h-16 w-16"
            src="data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSI2NCIgaGVpZ2h0PSI2NCIgY2xhc3M9Imljb24iIHZpZXdCb3g9IjAgMCAxMDI0IDEwMjQiPjxwYXRoIGZpbGw9IiM4QThBOEEiIGQ9Ik01MTIgMGE1MTIgNTEyIDAgMSAwIDUxMiA1MTJBNTEyIDUxMiAwIDAgMCA1MTIgME0yMTMuMzMzIDgzMkEyOTguNjY3IDI5OC42NjcgMCAwIDEgNTEyIDUzMy4zMzNhMTcwLjY2NyAxNzAuNjY3IDAgMSAxIDE3MC42NjctMTcwLjY2NkExNzAuNjY3IDE3MC42NjcgMCAwIDEgNTEyIDUzMy4zMzMgMjk4LjY2NyAyOTguNjY3IDAgMCAxIDgxMC42NjcgODMyeiIvPjwvc3ZnPg==">
          </image>
        </view>
        <view class="pl-1">
          <view class="text-2xl">{{ customerInfo?.customerName }}</view>
          <view>{{ customerInfo?.customerPhone }}</view>
        </view>
      </view>
      

    </view>

    <view class="flex flex-row justify-center items-center mt-10 text-slate-400">
      这里什么都没有, 还需要搭建
    </view>

  </scroll-view>


  <view class="fixed bottom-0 w-full">
    <tabBar></tabBar>
  </view>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import tabBar from "@/components/tabBar.vue"
import { request } from '@/utils/Request';
import { useCommonStore } from '@/config/CommonStore';
import { storeToRefs } from 'pinia';
import { DebugMode } from '@/types/AppConfigType';
import { useCustomerStore } from './customerStore';

let commonStore = useCommonStore()
let { appConfig } = storeToRefs(commonStore)

let customerStore = useCustomerStore();
let { customerInfo } = storeToRefs(customerStore)

onMounted(async () => {
  await commonStore.initCommonStore()
  customerInfo.value = await customerStore.getCustomerInfo()
})

</script>

<style>
.mybody {
  height: calc(100vh - 3.5rem)
}
</style>
