import {defineStore} from 'pinia'
import {ref} from 'vue'
import {type PageRequest} from "@/types/PageRequest"
import type { CustomPageEditorTarget } from './CustomPageHooks'

export const useCustomPageStore =  defineStore('customPageStore', ()=>{
    
    let addCustomPageDialogVisible = ref<boolean>(false)
    let addCustomName = ref<string>()
    let addPageValue = ref<string>()
    let selectCustomName = ref<string>()

    let pageRequest = ref<PageRequest>({
        pageNum: 1,
        pageSize: 20
    })

    let editDialogVisible = ref<boolean>(false)

    let editorDialog = ref<boolean>(false)
    let editorTarget = ref<CustomPageEditorTarget>()
    let editorValue = ref<string>()

    let updateDialogVisible = ref<boolean>(false)
    
    let updateId = ref<string>()
    let updateCustomName = ref<string>()
    let updatePageValue = ref<string>()

    return {
        addCustomPageDialogVisible,
        addCustomName,
        addPageValue,
        selectCustomName,
        pageRequest,
        editDialogVisible,
        editorDialog,
        editorTarget,
        editorValue,
        updateId,
        updateCustomName,
        updatePageValue
    }
},{
    persist: true,
})
