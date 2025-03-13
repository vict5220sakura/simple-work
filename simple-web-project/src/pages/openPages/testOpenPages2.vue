<template>
    <el-scrollbar>
        <div>
            新开窗口
            <span>{{a}}</span>
            <div v-for="item in 100">1</div>
        </div>
    </el-scrollbar>
</template>


<script setup lang="ts" name="testOpenPages2">
import {useRoute} from 'vue-router'
import {ref, onMounted} from 'vue'

const route = useRoute()
console.log("route.a->", route.query.a)

let a = ref(route.query.a)


</script>


<style scoped>

</style>