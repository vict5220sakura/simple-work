<template>
    <div class="menu">首页</div>
    <div class="menu">学习Tailwindcss</div>
    <div class="menu">TailwindCSS的设计哲学</div>
    <div class="menu">最佳实践</div>
    <hr />
    <div>
        <p class="text-red-500 hover:text-blue-500">测试悬停</p>
        <p><input class="bg-blue-500 focus:bg-red-500" type="text" placeholder="测试焦点"></p>
    </div>
    <hr/>
    <div>
        <div>p</div>
        <span class="p-2 bg-red-500 border-blue-500 border-2">111</span>
        <span class="p-2 bg-red-500 border-blue-500 border-2">222</span>
    </div>
    <div>
        <div>m</div>
        <span class="m-2 bg-red-500 border-blue-500 border-2">111</span>
        <span class="m-2 bg-red-500 border-blue-500 border-2">222</span>
    </div>
    <div>
        <div>border</div>
        <span class="p-2 bg-red-500">111</span>
        <span class="p-2 bg-red-500">222</span>
    </div>
    <div>
        <div>border</div>
        <span class="p-2 bg-red-500 border-blue-500 border-2">111</span>
        <span class="p-2 bg-red-500 border-blue-500 border-2">222</span>
    </div>
</template>

<script setup lang="ts" name="testTailwindcss">

</script>

<style scoped>
.menu {
    @apply p-2 text-gray-900 font-semibold;
}
</style>
