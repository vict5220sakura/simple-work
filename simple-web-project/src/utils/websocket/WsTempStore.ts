import {defineStore} from 'pinia'
import {ref} from 'vue'
import {defaultPath, MenuConfig, findbyPath} from '@/router/MenuConfig'

export const useWsTempStore =  defineStore('wsTempStore', ()=>{
    
    // let testMessage = ref<string>()
    // wsType存储类型json
    let checkDateJson:any = ref<any>({})

    let allDataType = ref<string[]>([])

    return {checkDateJson, allDataType}
},{
    persist: false,
})
