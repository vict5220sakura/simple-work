import {defineStore} from 'pinia'
import {ref} from 'vue'

export const useEditorShowStore =  defineStore('EditorShowStore', ()=>{
    let editorValue = ref<string>("")
    
    return {
        editorValue
    }
},{
    persist: true,
})

