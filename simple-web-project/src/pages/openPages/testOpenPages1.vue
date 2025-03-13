<template>
    <div>
        新开窗口222222
        <span>{{a}}</span>
    </div>
</template>


<script setup lang="ts" name="testOpenPages1">
import {useRoute} from 'vue-router'
import {ref} from 'vue'

const route = useRoute()
console.log("route.a->", route.query.a)

let a = ref(route.query.a)

</script>


<style scoped>

</style>