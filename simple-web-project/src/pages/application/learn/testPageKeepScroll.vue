<template>
        
    <div ref="my" id="my" class="w-[2000px]  flex flex-col">
        <div id="my2" class="text-4xl">你好</div>
        <div class="text-nowrap ">
            内容.................内容........................................2...................................................................................3.........................................4..........................5.....................6.............................7.............................8..........................9.............................10..........11
        </div>
        <div v-for="n in 60">1</div>
        <div>20</div>
    </div>

    
</template>

<script setup lang="ts" name="testPageKeepScroll">
import { ElMessage, ElMessageBox } from 'element-plus'
import type { Action } from 'element-plus'
import { ref, onDeactivated, onMounted, toRef } from 'vue'
import {emitter} from '@/utils/Emitter'
import {useTestTailwindcss3FlexStore} from '@/pages/application/learn/testTailwindcss3FlexStore'
import { storeToRefs } from 'pinia';

onMounted(()=>{
    let top = document.getElementById('context')!.scrollTop
    // console.log("onMounted->top", top)
})


</script>

<style scoped>

</style>
@/store/application/testTailwindcss3FlexStore@/pages/application/pages/learn/testTailwindcss3FlexStore@/utils/Emitter@/pages/application/learn/testTailwindcss3FlexStore