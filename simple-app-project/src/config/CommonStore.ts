import { type AppConfig } from '@/types/AppConfigType'
import { type R } from '@/types/R'
import { request } from '@/utils/Request'
import {defineStore} from 'pinia'
import {ref} from 'vue'

export const useCommonStore =  defineStore('appCommonStore', ()=>{
    
    let appConfig = ref<AppConfig>()

    const getAppConfig = async():Promise<AppConfig>=>{
        let r:R<AppConfig> = await request.post("/customerLogin/getAppConfig")
        return r.data
    }

    const initCommonStore = async()=>{
        appConfig.value = await getAppConfig()
    }

    return {appConfig, initCommonStore, getAppConfig}
},{
    persist: {
        storage: {
            getItem: uni.getStorageSync,
            setItem: uni.setStorageSync
        }
    }
})

