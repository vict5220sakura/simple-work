<template>
    <div>
        <el-upload
            ref="uploadRef"
            :action="domain"
            :data="{...qiniuData}"
            :on-remove="handleRemove"
            :on-error="uploadError"
            :on-success="uploadSuccess"
            :before-remove="beforeRemove"
            :before-upload="beforeAvatarUpload"
            :multiple="false"
            :show-file-list="false"
            :limit="1"
            :on-exceed="handleExceed"
            :file-list="fileList"
        >
            <slot name=default></slot>
        </el-upload>
    </div>
</template>

<script setup lang="ts" name="QiniuUpload">
import { defineProps, withDefaults, useTemplateRef , onMounted, defineEmits} from "vue"
import QiniuUploadHooks from "./QiniuUploadHooks"

const uploadRef:any = useTemplateRef<any>("uploadRef")

// const props = defineProps<{buttonText:string, buttonDesc:string}>()
// let {buttonText, buttonDesc} = props
const emit = defineEmits(["getUrl"])

let {
    qiniuData,
    handleRemove,
    uploadError,
    handleExceed,
    beforeAvatarUpload,
    domain,
    uploadSuccess,
    beforeRemove,
    fileList,
    init
} = QiniuUploadHooks();

// 初始化获取一下配置
onMounted(async ()=>{
    init(
        uploadRef,
        emit
    )
})


</script>

<style scoped>

</style>