<template>
    <div v-html="pageValue"></div>
</template>

<script setup lang="ts" name="customPageShow">
import { request } from '@/utils/Request';
import { onMounted, ref } from 'vue';
import {useRoute} from "vue-router"
import type { CustomPageVO } from '../application/buscenter/custompage/CustomPageTypes';
import type { R } from '@/types/R';
import { ElMessage } from 'element-plus';

let route = useRoute()

let pageValue = ref<string>()

onMounted(async()=>{
    let id = route.query.id
    if(id == undefined || id == null){
        ElMessage({
            message: "页面不存在",
            type: "error",
            center: true,
            duration: 1500
        });
        return;
    }
    let r:R<CustomPageVO> = await request.post("/customPage/customPageInfo", {
        id: id,
    })
    if(r.code == "success"){
        pageValue.value = r.data.pageValue
    }else{
        ElMessage({
            message: "页面不存在",
            type: "error",
            center: true,
            duration: 1500
        });
    }
})

</script>


<style scoped>

</style>