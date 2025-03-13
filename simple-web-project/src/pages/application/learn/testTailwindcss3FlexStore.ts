import {defineStore} from 'pinia'
import {ref} from 'vue'

export const useTestTailwindcss3FlexStore =  defineStore('testTailwindcss3FlexStore', ()=>{
    let dialogVisible = ref(false)
    let dialogOutVisible = ref(false)
    return {dialogVisible, dialogOutVisible}
},{
    persist: true,
})

