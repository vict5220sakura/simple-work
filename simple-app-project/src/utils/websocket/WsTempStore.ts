import {defineStore} from 'pinia'
import {ref} from 'vue'

export const useWsTempStore =  defineStore('appWsTempStore', ()=>{
    
    // let testMessage = ref<string>()
    // wsType存储类型json
    let checkDateJson:any = ref<any>({})

    let allDataType = ref<string[]>([])

    return {checkDateJson, allDataType}
},{
    persist: {
        storage: {
            getItem: uni.getStorageSync,
            setItem: uni.setStorageSync
        }
    }
})
