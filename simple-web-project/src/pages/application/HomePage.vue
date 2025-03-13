<template>
    <div v-html="homePageValue"></div>
</template>

<script setup lang="ts" name="homePage">
import type { KeyValue } from '@/types/KeyValueType';
import { keyValueGetByKey } from '@/utils/KeyValueUtils';
import { onMounted, ref } from 'vue';

let homePageValue = ref<string>()

onMounted(async()=>{
    let keyValue:KeyValue = await keyValueGetByKey("homePage")
    homePageValue.value = keyValue.value1
})


</script>

<style scoped></style>
