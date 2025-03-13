import {defineStore} from 'pinia'
import {ref} from 'vue'

export const useTestPage3PiniaStore =  defineStore('testPage3PiniaStore', ()=>{
    let str = ref("")
    return {str}
},{
    persist: true,
})

