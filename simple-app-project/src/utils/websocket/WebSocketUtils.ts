// import {apiBaseWsUrl} from "@/config/Common"
import { WsType, type WsMessage } from "./WebSocketTypes";
import {storeToRefs} from "pinia"
import { randomId } from '@/utils/IdUtils'
import { isSsl, getHost } from "@/utils/UrlUtils";
import { apiBaseWsUrl } from "@/MyEnv";
import { useCustomerStore } from "@/pages/my/customerStore";
import { useLoginStore } from "@/pages/login/loginStore";
import { useWsStore } from "./WsStore";
import { useWsTempStore } from "./WsTempStore";
import { log } from "@/utils/AppLog";

export const MyWsSocket = ()=>{
    let webSocket: any;

    /** 获取ws链接地址 */
    const getWsUrl = ()=>{
        if(isSsl()){
            return "wss://" + getHost() + apiBaseWsUrl + "/customer/{keyAToken}"
        }else{
            return "ws://" + getHost() + apiBaseWsUrl + "/customer/{keyAToken}"
        }
    }

    /** 初始化ws */
    const open = async ()=> {
        try {
            let customerStore = useCustomerStore()
            let {isAppLogin} = customerStore
            
            let loginStore = useLoginStore()
            let {aToken} = storeToRefs(loginStore)
            
            let wsStore = useWsStore()
            let {wsTypeJson} = storeToRefs(wsStore)
            
            let wsTempStore = useWsTempStore()
            let {checkDateJson} = storeToRefs(wsTempStore) 
    
            // if (!window.WebSocket) {
            //     log("你的浏览器不支持Socket的长连接!")
            //     console.log("你的浏览器不支持Socket的长连接!");
            //     return;
            // }
            // log("你的浏览器支持Socket的长连接!")
    
            let key = randomId()
    
            // 判断是否登录
            let isLoginFlag:boolean = await isAppLogin();
            if(isLoginFlag == false){
                log("未登录, 无法使用ws")
                return;
            }
            log("已登录, 正常使用ws")
    
            // 创建实例
            // let myWsSocket = new MyWsSocket()
    
            let url = getWsUrl()
            url = url.replace("{keyAToken}", key + "_" + aToken.value)
    
            webSocket = uni.connectSocket({url: url, complete: ()=> {}})
        
            webSocket.onopen = () => {
                // 全部创建
                wsStore.reloadAll()
            };
        
            /** 收到消息 */
            webSocket.onMessage((e: any) => {
                // log("收到信息", String(e))
                let data:WsMessage = JSON.parse(e.data)
                
                if(data.type == WsType.pong){
                    checkDateJson.value[String(data.data)] = undefined
                }else{
                    try{
                        wsTypeJson.value[String(data.type)] = data.type
                    }catch(err){
                        console.error(err)
                    }
                }
            });
            
        } catch (err) {
            log("ws异常!", "异常信息:", String(err))
        }
    };

    /** 发送ping消息 */
    const sendKeepaliveMsg = ():number|undefined=> {
        let checkLiveTime = Date.now();
        try{
            if (webSocket != undefined && webSocket != null) {
                
                let data:WsMessage = {
                    type: WsType.ping,
                    data: String(checkLiveTime)
                }
                webSocket.send({
					data: JSON.stringify(data),
				});
            }
        }catch(err){
            console.error("发送ping消息异常", err)
        }
        return checkLiveTime;
    }

    const close = ()=> {
        try {
            if (webSocket != undefined && webSocket != null) {
                webSocket.close();
            }
        } catch (error) {
            // 捕获并记录可能发生的异常
            log("destroy ws error:", String(error));
        }
    }

    const send = (message:WsMessage)=> {
        webSocket?.send(JSON.stringify(message))
    }

    return {
        open, sendKeepaliveMsg, close, send, webSocket
    }
}

// export class MyWsSocket2{
//     // 定义全局WebSocket实例和相关变量
//     webSocket?: any;

//     /** 获取ws链接地址 */
//     static getWsUrl = ()=>{
//         if(isSsl()){
//             return "wss://" + getHost() + apiBaseWsUrl + "/customer/{keyAToken}"
//         }else{
//             return "ws://" + getHost() + apiBaseWsUrl + "/customer/{keyAToken}"
//         }
//     }

//     /**
//      * 初始化ws
//      */
//     static open = async (): Promise<MyWsSocket|undefined> => {
//         try {
//             let customerStore = useCustomerStore()
//             let {isAppLogin} = customerStore
            
//             let loginStore = useLoginStore()
//             let {aToken} = storeToRefs(loginStore)
            
//             let wsStore = useWsStore()
//             let {wsTypeJson} = storeToRefs(wsStore)
            
//             let wsTempStore = useWsTempStore()
//             let {checkDateJson} = storeToRefs(wsTempStore) 
    
//             // if (!window.WebSocket) {
//             //     log("你的浏览器不支持Socket的长连接!")
//             //     console.log("你的浏览器不支持Socket的长连接!");
//             //     return;
//             // }
//             // log("你的浏览器支持Socket的长连接!")
    
//             let key = randomId()
    
//             // 判断是否登录
//             let isLoginFlag:boolean = await isAppLogin();
//             if(isLoginFlag == false){
//                 console.log("未登录, 无法使用ws")
//                 log("未登录, 无法使用ws")
//                 return;
//             }
//             log("已登录, 正常使用ws")
    
//             // 创建实例
//             let myWsSocket = new MyWsSocket()
    
//             let url = MyWsSocket.getWsUrl()
//             url = url.replace("{keyAToken}", key + "_" + aToken.value)
    
            
//             myWsSocket.webSocket = uni.connectSocket({url: url, complete: ()=> {}})
        
//             myWsSocket.webSocket.onopen = () => {
//                 // 全部创建
//                 wsStore.reloadAll()
//             };
        
//             /** 收到消息 */
//             myWsSocket.webSocket.onMessage((e: any) => {
//                 // log("收到信息", String(e))
//                 let data:WsMessage = JSON.parse(e.data)
                
//                 if(data.type == WsType.pong){
//                     checkDateJson.value[String(data.data)] = undefined
//                 }else{
//                     try{
//                         wsTypeJson.value[String(data.type)] = data.type
//                     }catch(err){
//                         console.error(err)
//                     }
//                 }
//             });
        
//             return myWsSocket;
//         } catch (err) {
//             log("ws异常!", "异常信息:", String(err))
//         }
//     };

//     /** 发送ping消息 */
//     sendKeepaliveMsg():number|undefined {
//         try{
//             if (this.webSocket != undefined && this.webSocket != null) {
//                 let checkLiveTime = Date.now();
//                 let data:WsMessage = {
//                     type: WsType.ping,
//                     data: String(checkLiveTime)
//                 }
//                 this.webSocket.send({
// 					data: JSON.stringify(data),
// 				});
//                 return checkLiveTime;
//             }
//         }catch(err){
//             console.error("发送ping消息异常", err)
//         }
//     }

//     close() {
//         try {
//             if (this.webSocket != undefined && this.webSocket != null) {
//                 this.webSocket.close();
//             }
//         } catch (error) {
//             // 捕获并记录可能发生的异常
//             log("destroy ws error:", String(error));
//         }
//     }

//     send(message:WsMessage){
//         this.webSocket?.send(JSON.stringify(message))
//     }

// }
