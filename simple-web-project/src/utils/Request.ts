import axios from "axios"
import { ElMessage, ElMessageBox } from 'element-plus'
import {apiBaseUrl} from "@/config/Common"
import {router} from "@/router/Router"
import {useLoginStore} from "@/pages/login/LoginStore"
import {storeToRefs} from "pinia"
import { randomId } from "./IdUtils"

const request = axios.create({
    // 新axios配置
    timeout: 60000
})

// 拦截
// 发送数据之前调用
request.interceptors.request.use(
    config => {
        if(config.url?.startsWith("http")){
            
        }else{
            if(config.url?.startsWith("/")){
                config.url = apiBaseUrl + config.url
            }else{
                config.url = apiBaseUrl + "/" + config.url
            }
        }

        let {token} = storeToRefs(useLoginStore())
        if(token.value){
            config.headers['token'] = token.value;
        }
        config.headers['requestId'] = randomId();
        return config
    }, error=>{
        ElMessage.error({
            message: '请求异常(request_error),请检查网络配置!!',
            type: 'error', // 类型：success, warning, info, error
            duration: 2000,  // 显示时间，默认 3000 毫秒，为 0 时需手动关闭
            showClose: false, // 显示关闭按钮
            grouping: true,  // 是否将同类型消息合并
        })
        return Promise.reject(error)
    }
)

// 返回数据之前
request.interceptors.response.use(
    response => {
        if(response.status == 200){
            if(response.data.code == "success"){
                return Promise.resolve(response.data)
            }else if(response.data.code == "authFail"){
                let errorMsg = response.data.msg
                if(errorMsg == undefined || errorMsg == null || errorMsg.trim() == ""){
                    errorMsg = "服务器异常(errorMsg=null), 请联系管理员"
                }
                ElMessage.error({
                    message: errorMsg,
                    type: 'error', // 类型：success, warning, info, error
                    duration: 2000,  // 显示时间，默认 3000 毫秒，为 0 时需手动关闭
                    showClose: false, // 显示关闭按钮
                    grouping: true,  // 是否将同类型消息合并
                })
                
                // const router = useRouter()
                router.push("/login")
                
                return Promise.resolve(response.data)
            }else{
                let errorMsg = response.data.msg
                if(errorMsg == undefined || errorMsg == null || errorMsg.trim() == ""){
                    errorMsg = "服务器异常(errorMsg=null), 请联系管理员"
                }
                ElMessage.error({
                    message: errorMsg,
                    type: 'error', // 类型：success, warning, info, error
                    duration: 2000,  // 显示时间，默认 3000 毫秒，为 0 时需手动关闭
                    showClose: false, // 显示关闭按钮
                    grouping: true,  // 是否将同类型消息合并
                })
                return Promise.resolve(response.data)
            }
        }else{
            ElMessage.error({
                message: "服务器异常(200_error), 请联系管理员",
                type: 'error', // 类型：success, warning, info, error
                duration: 2000,  // 显示时间，默认 3000 毫秒，为 0 时需手动关闭
                showClose: false, // 显示关闭按钮
                grouping: true,  // 是否将同类型消息合并
            })
            return Promise.reject(response)
        }
        return response
    }, error=>{
        if(error.code === "ERR_NETWORK"){
            ElMessage.error({
                message: '网络异常(ERR_NETWORK),请检查网络配置!!',
                type: 'error', // 类型：success, warning, info, error
                duration: 2000,  // 显示时间，默认 3000 毫秒，为 0 时需手动关闭
                showClose: false, // 显示关闭按钮
                grouping: true,  // 是否将同类型消息合并
            })
        }else{
            ElMessage.error({
                message: '出错了(response_error), 请联系管理员',
                type: 'error', // 类型：success, warning, info, error
                duration: 2000,  // 显示时间，默认 3000 毫秒，为 0 时需手动关闭
                showClose: false, // 显示关闭按钮
                grouping: true,  // 是否将同类型消息合并
            })
        }
    }
)

export {
    request
}