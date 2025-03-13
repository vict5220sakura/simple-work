// import {apiBaseWsUrl} from "@/config/Common"
import { WsType, type WsMessage } from "./WebSocketTypes";
import {useLoginServiceHooks} from "@/pages/login/LoginServiceHooks"
import {useWsStore} from "./WsStore"
import {storeToRefs} from "pinia"
import { router } from "@/router/Router";
import { randomId } from '@/utils/IdUtils'
import {useLoginStore} from "@/pages/login/LoginStore"
import {apiBaseWsUrl} from "@/config/Common"
import {getHost, isSsl} from "@/utils/UrlUtils"
import {useWsTempStore} from "@/utils/websocket/WsTempStore"

let {isLogin, buserInfo} = useLoginServiceHooks()
let wsStore = useWsStore()
let {wsTypeJson} = storeToRefs(wsStore)
let loginStore = useLoginStore()
let {token} = storeToRefs(loginStore)
let wsTempStore = useWsTempStore()
let {checkDateJson} = storeToRefs(wsTempStore)

export class MyWsSocket{
    // 定义全局WebSocket实例和相关变量
    webSocket?: WebSocket;

    /** 获取ws链接地址 */
    static getWsUrl = ()=>{
        if(isSsl()){
            return "wss://" + getHost() + apiBaseWsUrl + "/buser/{keyToken}"
        }else{
            return "ws://" + getHost() + apiBaseWsUrl + "/buser/{keyToken}"
        }
    }

    /**
     * 初始化ws
     */
    static open = async (): Promise<MyWsSocket|undefined> => {
        if (!window.WebSocket) {
            console.log("你的浏览器不支持Socket的长连接!");
            return;
        }

        let key = randomId()

        // 判断是否登录
        let isLoginFlag:boolean = await isLogin();
        if(isLoginFlag == false){
            console.log("未登录, 无法使用ws")
            router.push("/login")
            return;
        }

        // 创建实例
        let myWsSocket = new MyWsSocket()

        let url = MyWsSocket.getWsUrl()
        url = url.replace("{keyToken}", key + "_" + token.value)
        myWsSocket.webSocket = new WebSocket(url);
    
        myWsSocket.webSocket.onopen = () => {
            // 全部创建
            wsStore.reloadAll()
        };
    
        /** 收到消息 */
        myWsSocket.webSocket.onmessage = (e: MessageEvent) => {
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
        };
    
        return myWsSocket;
    };

    /** 发送ping消息 */
    sendKeepaliveMsg():number|undefined {
        try{
            if (this.webSocket != undefined && this.webSocket != null) {
                let checkLiveTime = Date.now();
                let data:WsMessage = {
                    type: WsType.ping,
                    data: String(checkLiveTime)
                }
                this.webSocket.send(JSON.stringify(data));
                return checkLiveTime;
            }
        }catch(err){
            console.error("发送ping消息异常", err)
        }
    }

    close() {
        try {
            if (this.webSocket != undefined && this.webSocket != null) {
                this.webSocket.close();
            }
        } catch (error) {
            // 捕获并记录可能发生的异常
            console.error("destroy ws error:", error);
        }
    }

    send(message:WsMessage){
        this.webSocket?.send(JSON.stringify(message))
    }

}
