<template>
    嵌套inner测试
    <el-button @click="outer = true">点开外层</el-button>
    <InnerDialog v-model="outer">
        <el-button @click="inner = true">点开内层</el-button>
    </InnerDialog>

    <InnerDialog v-model="inner">
        <div>内层</div>
    </InnerDialog>
</template>

<script setup lang="ts" name="testPage2">
import InnerDialog from "@/components/utils/InnerDialog.vue";
import {ref} from 'vue'
import { useTestPage2Hooks } from "./testPage2Hooks";

let outer = ref(false)
let inner = ref(false)
useTestPage2Hooks()

</script>

<style scoped></style>
