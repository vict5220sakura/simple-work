<template>
  <button @click="jumpTo('/pages/home/children/learnPopup')">弹出层</button>
</template>

<script setup lang="ts">
const jumpTo = (path:string)=>{
  uni.navigateTo({
    url:path
  })
}
</script>

<style scoped></style>