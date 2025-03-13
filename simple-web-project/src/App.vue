<template>
  <RouterView></RouterView>
</template>

<script setup lang="ts" name="App">
import {randomId} from "@/utils/IdUtils"

</script>

<style>
.required::before {
  content: '*';
  color: #f56c6c;
  margin-right: 4px;
}

.inBodyFullHeight{
    height: calc(100vh - 78px)
}
</style>
