import {defineStore} from 'pinia'
import {ref} from 'vue'
import {type PageRequest} from '@/types/PageRequest'
import type { ImageType } from './ImageTypes'


export const useImageStore =  defineStore('imageStore', ()=>{
    
    let selectAttname = ref<string>()
    let selectImageType = ref<ImageType>()
    
    let pageRequest = ref<PageRequest>({
        pageNum: 1,
        pageSize: 20
    })

    let imageSize = ref<string>("small")

    return {
        selectAttname,
        selectImageType,
        pageRequest,
        imageSize
    }
},{
    persist: true,
})

