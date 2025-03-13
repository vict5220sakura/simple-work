import {emitter} from '@/utils/Emitter'
import { onUnmounted, ref, useTemplateRef } from "vue"
import { ElMessage, ElMessageBox } from 'element-plus'
import {Optional} from '@/utils/Optional'
import {type Menu} from "@/components/menu/MenuTypes"
import {MenuConfig, findTypeMenu} from '@/router/MenuConfig'
import { storeToRefs } from 'pinia';
import { useApplicationStore } from '@/pages/application/applicationStore'
import { useRouter } from 'vue-router'
import {useTagBarStore} from "@/components/headtag/TagBarStore";
import {type TagType} from '@/components/headtag/TagType'
import { onMounted } from 'vue'
import {request} from '@/utils/Request'
import {useLoginStore} from "@/pages/login/LoginStore"
import {type R} from "@/types/R"
import {usePermissionStore} from "@/components/role/permissionStore"
import {router} from "@/router/Router"
import {useApplicationServiceHooks} from "./applicationServiceHooks"
import { randomId } from '@/utils/IdUtils'
import { MyWsSocket } from '@/utils/websocket/WebSocketUtils'
import {useWsTempStore} from "@/utils/websocket/WsTempStore"
import { keyValueGetByKey } from '@/utils/KeyValueUtils'
import type { KeyValue } from '@/types/KeyValueType'
import {RouterUtils} from '@/router/RouterUtils';

let wsTempStore = useWsTempStore()
let {checkDateJson} = storeToRefs(wsTempStore)

export const useApplicationHooks = function(){
    // console.log("启动环境", import.meta.env.VITE_ENV_NAME)

    let {loginOut, reloadCache, clearCache, flushPermission} = useApplicationServiceHooks()

    const toApp = async ()=>{
        let data:KeyValue = await keyValueGetByKey("app_url") 
        window.open(data.value1, "_blank")
    }

    // 存储
    let permissionStore = usePermissionStore()
    let applicationStore = useApplicationStore();
    let {activeMenuIndex} = storeToRefs(applicationStore)
    let {token} = storeToRefs(useLoginStore())
    let {permissionList} = storeToRefs(permissionStore)

    // 内部dialog锁定功能
    let lockContextFlag = ref(false)
    
    console.log("加载lockContextScroll监听")
    emitter.on("lockContextScroll", (value:any) => {
        
        let {openClosed, className} = value
        // let className = value.className

        if (openClosed == true) {
            lockContextFlag.value = true

            let elOverlay: any = document.getElementsByClassName(className);
            elOverlay[0].style.position = "absolute";

            let elOverlayDialog: any = elOverlay[0].getElementsByClassName("el-overlay-dialog");
            elOverlayDialog[0].style.position = "absolute";

            let top = document.getElementById('context')!.scrollTop

            let left = document.getElementById('context')!.scrollLeft

            elOverlay[0].style.top = top + "px"
            elOverlay[0].style.right = "-" + left + "px"

        } else {
            lockContextFlag.value = false

            let elOverlay: any = document.getElementsByClassName("inside-dialog");
            if(!elOverlay || !elOverlay[0]){
                return
            }
            elOverlay[0].style.position = "fixed";

            let elOverlayDialog: any = elOverlay[0].getElementsByClassName("el-overlay-dialog");
            elOverlayDialog[0].style.position = "fixed";

            elOverlay[0].style.top = 0 + "px"
            elOverlay[0].style.left = 0 + "px"
        }
    })
    // 持久化滚动条功能
    const contextScroll = useTemplateRef<any>("contextScroll")
        
    const pagePathTop: any = {}
    const pagePathLeft: any = {}
    let top = ref(0)
    let left = ref(0)
    /** 获取高度记录到临时存储 */
    emitter.on("getContextTopLeft", (pagePath) => {
        pagePathTop[pagePath as string] = top.value
        pagePathLeft[pagePath as string] = left.value
    })

    /** 设置高度记录从临时存储 */
    // 监听设置高度
    emitter.on("setContextTopLeft", (pagePath) => {
        try{
            setTimeout(()=>{
                
                let obj:any = contextScroll.value!
                let topTemp = pagePathTop[pagePath as string]
                let leftTemp = pagePathLeft[pagePath as string]

                if(topTemp == null || topTemp == undefined){
                    topTemp = 0
                }
                if(leftTemp == null || leftTemp == undefined){
                    leftTemp = 0
                }

                if(!obj){
                    return;
                }
                
                obj.setScrollTop(topTemp)
                obj.setScrollLeft(leftTemp)
            }, 0)

        }catch(err){
            console.log("设置当前组件滚动高度,异常", err)
        }
    })


    /** 监听滚动高度并记录 */
    function scrollEvent(e:any){
        let {scrollTop, scrollLeft} = e
        top.value = scrollTop
        left.value = scrollLeft
    }

    // 初始化WebSocket连接
    // let loginStore = useLoginStore()
    // let key = randomId()
    // let ws:WsSocket|null = null;
    // setTimeout(async()=>{
    //     ws = await WsSocket.InitWS(key, token.value!);
    // })

    onMounted(()=>{
        // console.log("application onMounted")
    })

    onUnmounted(()=>{
        // console.log("application onUnmounted")
        // ws?.destroy();
    })
    
    
    // 菜单
    let menus = ref<Menu[]>(findTypeMenu())
    let menuRef = ref<any>(null)
    
    function menuSelect(menuSelect:any){
        activeMenuIndex.value = menuSelect
    }

    // 菜单栏是否缩起
    let isCollapse = ref<boolean>()
    
    const menuMin = ()=>{
        isCollapse.value = true
    }
    const menuMax = ()=>{
        isCollapse.value = false
    }

    const checkIsMobile = () => {
        const userAgent = navigator.userAgent;
        const mobileKeywords = /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i;
        return mobileKeywords.test(userAgent);
    };

    onMounted(()=>{
        if(checkIsMobile()){
            menuMin()
        }
    })

    // myWsSocket初始化
    let myWsSocket:MyWsSocket|undefined;
    let checkWsTimer:number;

    onMounted(async()=>{
        if(checkWsTimer){
            clearInterval(checkWsTimer)
        }
        myWsSocket = await MyWsSocket.open()
        checkWsTimer = setInterval(async()=>{
            let checkLiveTime:number|undefined = myWsSocket?.sendKeepaliveMsg()
            
            checkDateJson.value[String(checkLiveTime)] = checkLiveTime

            if(checkLiveTime == undefined){
                console.log("ws断开")
                checkDateJson.value[String(checkLiveTime)] = undefined
                myWsSocket = await MyWsSocket.open()
                return;
            }
            setTimeout(async()=>{
                if(checkDateJson.value[String(checkLiveTime)] != undefined){
                    console.log("ws断开")
                    checkDateJson.value[String(checkLiveTime)] = undefined
                    myWsSocket = await MyWsSocket.open()
                    return;
                }
            }, 2000)
        }, 5000)
    })
    onUnmounted(()=>{
        if(checkWsTimer){
            clearInterval(checkWsTimer)
        }
    })

    return {
        loginOut,
        clearCache,
        menuSelect,
        menus,
        menuRef,
        flushPermission,
        lockContextFlag,
        activeMenuIndex,
        reloadCache,
        scrollEvent,
        isCollapse,
        menuMin,
        menuMax,
        toApp
    }
}