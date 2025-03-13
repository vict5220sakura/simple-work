import {defineStore} from 'pinia'
import {ref} from 'vue'

export const useImageShowStore =  defineStore('ImageShowStore', ()=>{
    let imageUrl = ref<string>("")
    
    return {
        imageUrl
    }
},{
    persist: true,
})

