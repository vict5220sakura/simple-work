import {baseApi, env} from "@/MyEnv"
import { useAppConfigServiceHooks } from "@/service/appconfig/AppConfigServiceHooks";
import { type AppConfig } from "@/types/AppConfigType";
import { request } from "@/utils/Request";
import { ref } from "vue";
import { useCommonStore } from "./CommonStore";
import { MyWsSocket } from "@/utils/websocket/WebSocketUtils";
import { useWsTempStore } from "@/utils/websocket/WsTempStore";
import { storeToRefs } from "pinia";
import { log } from "@/utils/AppLog";

uni.addInterceptor('request', {
  invoke(args) {
    
  },
  success(args) {
    
  },
  fail(err) {
    uni.showToast({
      title: '网络连接异常',
      duration: 2000,
      position: 'bottom',
      icon: 'none'
  });
    console.log('interceptor-fail', err)
  },
  complete(res) {
    // console.log('interceptor-complete',res)
  }
})

export const init = async ()=>{
  
  console.log("初始化配置文件, 当前环境: " + env)

  let {
    initCommonStore
  } = useCommonStore()

  initCommonStore()

  // myWsSocket初始化
  let myWsSocket = MyWsSocket()
  let checkWsTimer: number;

  let wsTempStore = useWsTempStore()
	let { checkDateJson } = storeToRefs(wsTempStore)

  // #ifdef H5
	if (window.checkWsTimer) {
    clearInterval(window.checkWsTimer)
    window.myWsSocket?.close()
	}
	// #endif

	if (checkWsTimer) {
		clearInterval(checkWsTimer)
	}

	if(myWsSocket.webSocket == undefined){
		await myWsSocket.open()
	}

	checkWsTimer = setInterval(async () => {
		let checkLiveTime: number | undefined = myWsSocket?.sendKeepaliveMsg()

		checkDateJson.value[String(checkLiveTime)] = checkLiveTime

		// 验证ws是否断开
		setTimeout(async () => {
			if (checkDateJson.value[String(checkLiveTime)] != undefined) {
				log("ws断开2")
				myWsSocket?.close()
				checkDateJson.value[String(checkLiveTime)] = undefined
				await myWsSocket.open()
				return;
			}else{
				log("ws没有断开2")
			}
		}, 2000)
	}, 5000)

  window.checkWsTimer = checkWsTimer
  window.myWsSocket = myWsSocket
}

