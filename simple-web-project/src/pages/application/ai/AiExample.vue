<template>
    <div class="inBodyFullHeight flex flex-col justify-between bg-gray-100">
        <!-- 聊天窗口 -->
        <el-scrollbar ref="chatMessagesRef" :always="true">
            <div class="flex-grow p-4 overflow-y-auto">
                <div class="space-y-4" >
                <div 
                    v-for="(message, index) in messages" 
                    :key="index" 
                    :class="[
                    message.isUser ? 'flex justify-end' : 'flex justify-start',
                    'items-start'
                    ]"
                >
                    <div 
                    :class="[
                        message.isUser ? 'bg-blue-100' : 'bg-green-100',
                        'p-3 rounded-md shadow-md max-w-md break-words'
                    ]"
                    >
                    {{ message.text }}
                    </div>
                </div>
                </div>
            </div>
        </el-scrollbar>

        <!-- 输入区域 -->
        <div v-loading="loading" class="w-full p-4 border-t border-gray-300">
            <div class="flex justify-center items-center space-x-4">
                <input
                v-model="userInput"
                type="text"
                class="flex-grow border border-gray-300 p-3 rounded-md"
                placeholder="请输入订票需求"
                @keyup.enter="sendMessage"
                />
                <button
                @click="sendMessage"
                class="bg-blue-600 text-white p-3 rounded-md hover:bg-blue-700"
                >
                发送
                </button>
            </div>
        </div>
    </div>
  </template>

<script setup lang="ts" name="AiExample">
import {useAiExampleHooks} from "@/pages/application/ai/AiExampleHooks";
let{
    messages,
    userInput,
    sendMessage,
    loading
} = useAiExampleHooks()
</script>

<style scoped>

</style>
