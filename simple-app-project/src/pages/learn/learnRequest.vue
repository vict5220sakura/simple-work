<template>
  <view class="w-24 max-w-24 p-1">
    <uv-button type="primary" text="测试" @click="test"></uv-button>
  </view>
  <view class="w-24 max-w-24 p-1">
    <uv-button type="primary" text="测试2" @click="test2"></uv-button>
  </view>
  <!-- <view v-html="data"></view> --><!-- 小程序会被转换为rich-text -->
  <rich-text :nodes="data"></rich-text>
</template>

<script setup lang="ts">
import { type KeyValue } from "@/types/KeyValueType";
import { type R } from "@/types/R";
import { keyValueGetByKey } from "@/utils/KeyValueUtils";
import {request} from "@/utils/Request"
import { ref } from "vue";

let data = ref<any>();

const test = async ()=>{
  let r:R<KeyValue> = await request.post("/keyValue/keyValueByKey?key=" + "ICPValue");
  console.log(r)
}

const test2 = async ()=>{
  let keyValue:KeyValue = await keyValueGetByKey("ICPValue");
  console.log(keyValue)

  data.value = keyValue.value1
}

</script>

<style scoped>

</style>