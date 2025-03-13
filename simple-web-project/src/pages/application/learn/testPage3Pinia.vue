<template>
  <div class="text-xs text-blue-600">testPage3Pinia</div>
  <hr/>
  <div class="flex-initial">
    <h3>pinia</h3>
    <span>{{ str }}</span>
    <div><el-input v-model="str"/></div>
  </div>
  <br>
  <hr>
  <h3>customRef</h3>
  <el-icon :size="100" >
    <Edit />
  </el-icon>
</template>


<script setup lang="ts" name="testPage3Pinia">
    import { useTestPage3PiniaStore } from '@/pages/application/learn/testPage3Pinia';
    import { storeToRefs } from 'pinia';
    import { useTestPage2Hooks } from "./testPage2Hooks";

    useTestPage2Hooks()

    let testPage3PiniaStore = useTestPage3PiniaStore()
    const { str } = storeToRefs(testPage3PiniaStore)

</script>


<style scoped>

</style>
@/store/application/testPage3Pinia@/pages/application/learn/testPage3Pinia