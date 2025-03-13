import {defineStore} from 'pinia'
import {ref} from 'vue'
import {defaultPath, MenuConfig, findbyPath} from '@/router/MenuConfig'
import type { PageRequest } from '@/types/PageRequest'
import type { ChinesePoetryClassify } from './ChinesePoetryTypes'

export const useChinesePoetryStore =  defineStore('ChinesePoetryStore', ()=>{
    
    let addChinesePoetryClassifyDialogVisible = ref<boolean>(false)
    let addChinesePoetryClassifyName = ref<string>()

    let pageRequest = ref<PageRequest>({
        pageNum: 1,
        pageSize: 20
    })

    let selectChinesePoetryClassifyId = ref<string>();

    let selectTitle = ref<string>();
    let selectAuthor = ref<string>();

    // 新增古诗
    let addChinesePoetryDialogVisible = ref<boolean>(false)
    let addChinesePoetryTitle = ref<string>()
    let addChinesePoetryAuthor = ref<string>()
    let addChinesePoetryParagraphs = ref<string>()

    // 更新窗口
    let updateChinesePoetryDialogVisible = ref<boolean>(false)
    let updateChinesePoetryId = ref<string>()
    let updateChinesePoetryTitle = ref<string>()
    let updateChinesePoetryAuthor = ref<string>()
    let updateChinesePoetryParagraphs = ref<string>()

    return {
        selectChinesePoetryClassifyId,
        addChinesePoetryClassifyDialogVisible,
        addChinesePoetryClassifyName,
        pageRequest,
        selectTitle,
        selectAuthor,

        addChinesePoetryDialogVisible,
        addChinesePoetryTitle,
        addChinesePoetryAuthor,
        addChinesePoetryParagraphs,

        // 更新
        updateChinesePoetryDialogVisible,
        updateChinesePoetryId,
        updateChinesePoetryTitle,
        updateChinesePoetryAuthor,
        updateChinesePoetryParagraphs,
    }
},{
    persist: true,
})

