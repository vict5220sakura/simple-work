import { ref, onMounted, onUnmounted} from "vue";
import {useWsStore} from "@/utils/websocket/WsStore"
import { storeToRefs } from "pinia";
import {useWsTempStore} from "@/utils/websocket/WsTempStore"

export const useWsServiceHooks = (type:string, func:any)=>{
    let wsTempStore = useWsTempStore()
    let {allDataType} = storeToRefs(wsTempStore)

    // 注册监听类型
    allDataType.value.push(type)

    let wsStore = useWsStore()
    let {wsTypeJson} = storeToRefs(wsStore)

    // 测试消息
    let testTimer:number;
    onMounted(async()=>{
        if(testTimer){
            clearInterval(testTimer)
        }
        testTimer = setInterval(async()=>{
            if(wsTypeJson.value[type] != undefined){
                wsTypeJson.value[type] = undefined
                func()
            }
        }, 500)
    })
    onUnmounted(()=>{
        if(testTimer){
            clearInterval(testTimer)
        }
    })
}