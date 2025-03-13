<template>
    <el-button @click="testInnerDialog">测试组件内部弹窗</el-button>

    <InnerDialog :width="500" v-model="innerDialogVisible">
        <template #header>
            <div>标题</div>
        </template>
        <template #default>
            <div>我是组件内部弹窗</div>
        </template>
        <template #footer>
            <div>底部</div>
        </template>
    </InnerDialog>
    <div ref="my" id="my" class="w-[2000px]  flex flex-col">
        <div id="my2" class="text-4xl">你好</div>
        <div>
            <el-button class="w-32" @click="dialogOutVisible = true">测试外部弹窗</el-button>
        </div>
        <div>
            <el-button class="w-32" @click="dialogVisible = true">测试内部弹窗</el-button>
        </div>
        <div class="text-nowrap ">
            内容.................内容........................................2...................................................................................3.........................................4..........................5.....................6.............................7.............................8..........................9.............................10..........11
        </div>
        <div v-for="n in 60">1</div>
        <el-button class="w-32" @click="dialogVisible = true">测试内部弹窗</el-button>
        <div>20</div>
    </div>

    <el-dialog 
        v-model="dialogOutVisible" 
        title="全局弹出框" 
        width="500" 
        :close-on-click-modal="false" 
        >
        <span>This is a message</span>
        <template #footer>
            <div>
                <el-button @click="dialogOutVisible = false">Cancel</el-button>
                <el-button type="primary" @click="dialogOutVisible = false">
                    Confirm
                </el-button>
            </div>
        </template>
    </el-dialog>

    <el-dialog 
        v-model="dialogVisible" 
        title="标题" 
        width="500" 
        :close-on-click-modal="false" 
        append-to.async="#content"
        @open="()=>{emitter.emit('lockContextScroll', true)}" 
        @closed="()=>{emitter.emit('lockContextScroll', false)}"
        modal-class="inside-dialog"
        >
        <span>This is a message</span>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="dialogVisible = false">Cancel</el-button>
                <el-button type="primary" @click="dialogVisible = false">
                    Confirm
                </el-button>
            </div>
        </template>
    </el-dialog>

    

</template>

<script setup lang="ts" name="testTailwindcss3Flex">
import { ElMessage, ElMessageBox } from 'element-plus'
import type { Action } from 'element-plus'
import { ref, onDeactivated, onMounted, toRef } from 'vue'
import {emitter} from '@/utils/Emitter'
import {useTestTailwindcss3FlexStore} from '@/pages/application/learn/testTailwindcss3FlexStore'
import { storeToRefs } from 'pinia';
import InnerDialog from '@/components/utils/InnerDialog.vue'

let store = useTestTailwindcss3FlexStore()
let {dialogVisible, dialogOutVisible} = storeToRefs(store)

onMounted(()=>{
    // console.log("dialogVisible默认值->", dialogVisible.value)
    if(dialogVisible.value){
        dialogVisible.value = false
        setTimeout(()=>{
            dialogVisible.value = true
        },0)
    }
})

let innerDialogVisible = ref<boolean>(false)
let testInnerDialog = ()=>{
    innerDialogVisible.value = true
    console.log("组件外部", innerDialogVisible.value)
}

</script>

<style scoped>

</style>
@/pages/application/pages/learn/testTailwindcss3FlexStore@/utils/Emitter@/pages/application/learn/testTailwindcss3FlexStore