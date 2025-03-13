import {defineStore} from 'pinia'
import {ref} from 'vue'
import {defaultPath, MenuConfig, findbyPath} from '@/router/MenuConfig'
import {type PageRequest} from '@/types/PageRequest'
import {type OrganizeVO } from '@/pages/application/buscenter/inwork/organize/OrganizeTypes'

export const useOrganizeStore =  defineStore('useOrganizeStore', ()=>{
    
    let chooseOrganizeId = ref<string>()
    let chooseOrganize = ref<OrganizeVO>()

    return {
        chooseOrganizeId,
        chooseOrganize
    }
},{
    persist: true,
})

