<template>
    <div class="m-4">
        <div class="mb-4">富文本查看</div>
        <div class="mb-4">
            <el-input clearable v-model="editorValue" class="w-80" placeholder="请输入文本内容"></el-input>
        </div>
        <div v-html="editorValue"></div>
    </div>
</template>

<script setup lang="ts" name="ImageShow">
import {useEditorShowStore} from '@/pages/application/system/editorshow/EditorShowStore'
import {storeToRefs} from 'pinia'

let editorShowStore = useEditorShowStore();
let {
    editorValue
} = storeToRefs(editorShowStore);
</script>

<style scoped></style>
@/pages/application/pages/system/EditorShowStore@/pages/application/pages/system/editorshow/EditorShowStore@/pages/application/system/editorshow/EditorShowStore