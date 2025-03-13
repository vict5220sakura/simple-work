import {defineStore} from 'pinia'
import {ref} from 'vue'
import {useWsTempStore} from './WsTempStore'
import { storeToRefs } from "pinia";
import type { RefSymbol } from '@vue/reactivity';

export const useWsStore =  defineStore('appWsStore', ()=>{
    
    let wsTempStore = useWsTempStore()
    let {allDataType} = storeToRefs(wsTempStore)

    // let testMessage = ref<string>()
    // wsType存储类型json
    let wsTypeJson = ref<any>({})

    const reloadAll = ()=>{
        for(let type of allDataType.value){
            wsTypeJson.value[type] = type
        }
    }

    return {wsTypeJson, reloadAll}
},{
    persist: {
        storage: {
            getItem: uni.getStorageSync,
            setItem: uni.setStorageSync
        }
    }
})
