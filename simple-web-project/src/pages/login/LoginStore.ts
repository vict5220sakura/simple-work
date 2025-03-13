import {defineStore} from 'pinia'
import {ref} from 'vue'

export const useLoginStore =  defineStore('loginStore', ()=>{
    let token = ref<string>()
    let loginBuserCode = ref<string|null>()
    return {token, loginBuserCode}
},{
    persist: true,
})

