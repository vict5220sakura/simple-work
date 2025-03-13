import {defineStore} from 'pinia'
import {ref} from 'vue'
import {defaultPath, MenuConfig, findbyPath} from '@/router/MenuConfig'
import {useWsTempStore} from './WsTempStore'
import { storeToRefs } from "pinia";
import type { RefSymbol } from '@vue/reactivity';

let wsTempStore = useWsTempStore()
let {allDataType} = storeToRefs(wsTempStore)

export const useWsStore =  defineStore('wsStore', ()=>{
    
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
    persist: true,
})
