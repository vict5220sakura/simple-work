<template>
    <el-dialog 
        v-model="dialogVisible" 
        :width="width"
        :top="top"
        :close-on-click-modal="false" 
        append-to.async="#content"
        @open="open" 
        @closed="closed"
        :modal-class="className"
    >
        <template #header>
            <slot name="header"></slot>
        </template>
        <template #default>
            <slot name="default"></slot>
        </template>
        <template #footer>
            <slot name="footer"></slot>
        </template>
    </el-dialog>
</template>

<script setup lang="ts" name="InnerDialog">
import {emitter} from '@/utils/Emitter'
import { defineProps, withDefaults, onMounted , watch, toRefs, defineModel} from 'vue';
import {randomId} from "@/utils/IdUtils"
import {ref} from "vue"

let className = ref<string>("inside-dialog-"+randomId(10))

let dialogVisible = defineModel<boolean>()

let props = withDefaults(defineProps<{width?:number, top?:string}>(), {
    width: ()=>500,
    top: ()=>"15vh"
})
// let {dialogVisible} = toRefs(props)

onMounted(()=>{
    if(dialogVisible.value){
        dialogVisible.value = false
        setTimeout(()=>{
            dialogVisible.value = true
        },0)
    }
})

let open = ()=>{
    emitter.emit('lockContextScroll', {openClosed: true, className: className.value})
}

let closed = ()=>{
    if(dialogVisible.value === true){
        dialogVisible.value = false
    }
    emitter.emit('lockContextScroll', {openClosed: false, className: className.value})
}


</script>

<style scoped>

</style>
@/utils/Emitter