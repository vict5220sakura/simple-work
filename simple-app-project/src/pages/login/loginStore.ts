import { type AppConfig } from '@/types/AppConfigType'
import { type R } from '@/types/R'
import { request } from '@/utils/Request'
import {defineStore} from 'pinia'
import {ref} from 'vue'

export const useLoginStore =  defineStore('appLoginStore', ()=>{

    let aToken = ref<string>();
    
    return {aToken}
},{
    persist: {
        storage: {
            getItem: uni.getStorageSync,
            setItem: uni.setStorageSync
        }
    }
})

