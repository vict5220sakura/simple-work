import { useCommonStore } from "@/config/CommonStore";
import { request } from "./Request"
import { storeToRefs } from "pinia";
import {type AppConfig , LogSwitch} from "@/types/AppConfigType"


export const log = (...str2:string[])=>{
    console.log(...str2)

    let list:string[] = [];
    // list.push(str)
    list.push(...str2)

    let commonStore = useCommonStore()
    let {appConfig} = storeToRefs(commonStore)

    if(appConfig.value?.logSwitch == LogSwitch.LogSwitch_yes){
        request.post("/applog/info", {
            logInfos: list
        })
    }
    
}
