<template>
    <div>
        <Toolbar
            style="border-bottom: 1px solid #ccc"
            :editor="editorRef"
            :defaultConfig="toolbarConfig"
            :mode="mode"
        />
        <Editor
            style="height: 500px; overflow-y: hidden;"
            v-model="editorValue"
            :defaultConfig="editorConfig"
            :mode="mode"
            @onCreated="handleCreated"
        />
    </div>
</template>

<script setup lang="ts" name="WangEditor">
import '@wangeditor/editor/dist/css/style.css' // 引入 css
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { onBeforeUnmount, ref, shallowRef, onMounted, defineModel } from 'vue'
import { type IEditorConfig } from '@wangeditor/editor'
import {UploadFilled} from "@/utils/QiNiuUtils"
import {type QiniuConfig, type QiniuData, type QiniuUrl} from "@/types/QiNiuType"

type InsertFnTypeImage = (url: string, alt: string, href: string) => void
type InsertFnTypeVideo = (url: string, poster: string) => void

let mode = ref('default') // 或 'simple'

// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef()

// 内容 HTML
let editorValue = defineModel<string|null>()


const toolbarConfig = {}
const editorConfig: Partial<IEditorConfig> = { 
    placeholder: '请输入内容...',
    MENU_CONF:{}
}

editorConfig.MENU_CONF!['uploadImage'] = {
  // 自定义上传
  async customUpload(file: File, insertFn: InsertFnTypeImage) {
    // TS 语法
    let res:QiniuUrl = await UploadFilled(file)
    // 自己实现上传，并得到图片 url alt href
    // 最后插入图片
    insertFn(res.url, res.name, res.url)
  },
}



editorConfig.MENU_CONF!['uploadVideo'] = {
  // 自定义上传
  async customUpload(file: File, insertFn: InsertFnTypeVideo) {
    let res:QiniuUrl = await UploadFilled(file)
    insertFn(res.url, res.name)
  },
}

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})

const handleCreated = (editor:any) => {
  editorRef.value = editor // 记录 editor 实例，重要！

//   let editorConfig = editorRef.value.getMenuConfig('uploadImage')
//   console.log(">", editorConfig.MENU_CONF['uploadImage'])
}



</script>

<style scoped>

</style>
@/utils/QiNiuUtils