<template>
    <el-button @click="dialogVisible = true">富文本编辑器</el-button>
    <InnerDialog v-model="dialogVisible" :width="1000">
        <WangEditor v-if="dialogVisible" v-model="editorValue"></WangEditor>
        <el-button>取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
    </InnerDialog>

    <div v-html="showHtml"></div>
</template>

<script setup lang="ts" name="testWangEditor">
import WangEditor from "@/components/utils/WangEditor.vue"
import InnerDialog from "@/components/utils/InnerDialog.vue";
import {ref} from "vue"

let dialogVisible = ref(false)

let editorValue = ref("")

let showHtml = ref("")

const save = ()=>{
    showHtml.value = editorValue.value
}

</script>

<style scoped></style>
