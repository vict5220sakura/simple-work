import {defineStore} from 'pinia'
import {ref} from 'vue'
import {defaultPath, MenuConfig, findbyPath} from '@/router/MenuConfig'

export const useApplicationStore =  defineStore('applicationStore', ()=>{
    
    let activeMenuIndex = ref(defaultPath)

    function reload(){
        activeMenuIndex.value =  defaultPath
    }

    return {activeMenuIndex, reload}
},{
    persist: true,
})

