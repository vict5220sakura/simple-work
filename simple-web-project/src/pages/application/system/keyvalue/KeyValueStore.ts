import {defineStore} from 'pinia'
import {ref} from 'vue'
import {type PageRequest} from '@/types/PageRequest'

// 富文本编辑对象
export enum EditorTarget {
    addValue1,
    addValue2,
    updateValue1,
    updateValue2
}

export const useKeyValueStore =  defineStore('keyValueStore', ()=>{
    
    let addKey = ref<string|null>()
    let addValue1 = ref<string|null>()
    let addValue2 = ref<string|null>()
    let addDesc = ref<string|null>()
    let addDialogVisible = ref<boolean>(false)
    let selectKey = ref<string|null>()
    let selectDesc = ref<string|null>()

    let pageRequest = ref<PageRequest>({
        pageNum: 1,
        pageSize: 20
    })
    
    let updateDialogVisible = ref<boolean>(false)
    
    let updateId = ref<string|null>()
    let updateKey = ref<string|null>()
    let updateValue1 = ref<string|null>()
    let updateValue2 = ref<string|null>()
    let updateDesc = ref<string|null>()

    // 富文本编辑器
    let editorDialog = ref<boolean>(false)
    
    let editorTarget = ref<EditorTarget|null>(null)
    // 富文本内容
    let editorValue = ref<string|null>()
    return {
        addKey,
        addValue1,
        addValue2,
        addDesc,
        addDialogVisible,
        selectKey,
        selectDesc,
        pageRequest,
        updateDialogVisible,
        updateKey,
        updateValue1,
        updateValue2,
        updateDesc,
        updateId,
        editorDialog,
        editorTarget,
        editorValue
    }
},{
    persist: true,
})

