
import {permissionList} from "@/config/PermissionConfig";
import {useJobStore} from "@/pages/application/system/job/JobStore"
import {storeToRefs} from "pinia"
import {request} from "@/utils/Request"
import type {R} from "@/types/R";
import { ElMessage, ElMessageBox } from "element-plus";
import {ref, onMounted, useTemplateRef} from 'vue'
import type {PageInfo} from "@/types/PageInfo";
import {randomId} from '@/utils/IdUtils'

export const useAiExampleHooks = function(){
    let id = randomId()

    onMounted(async()=>{
        let r:R<any> = await request.post("/trainAi/chat", {
            id: id
        })
        
        messages.value.push({ text: r.data.message, isUser: false });
    })

    // 存储聊天消息
    let messages = ref<any[]>([]);
    // { text: '欢迎使用订票智能 AI，请告诉我你的订票需求。', isUser: false }

    let userInput = ref<string>("")

    const sendMessage = async()=>{
        loading.value = true
        if (userInput.value.trim() === '') return;
        // 添加用户消息到消息列表
        messages.value.push({ text: userInput.value, isUser: true });
        let r:R<any> = await request.post("/trainAi/chat", {
            id: id,
            question: messages.value[messages.value.length - 2].text,
            message: userInput.value
        })
        
        messages.value.push({ text: r.data.message, isUser: false });
        // 清空输入框
        userInput.value = '';

        scrollToBottom()

        loading.value = false
    }

    let chatMessagesRef = useTemplateRef<any>("chatMessagesRef")

    // 滚动到聊天窗口底部
    const scrollToBottom = () => {
        if (chatMessagesRef.value) {
            chatMessagesRef.value.setScrollTop(9999);
            setTimeout(()=>{
                chatMessagesRef.value.setScrollTop(9999);
            },0)
        }
    };

    let loading = ref<boolean>(false)

    return {
        messages,
        userInput,
        sendMessage,
        loading
    }
}