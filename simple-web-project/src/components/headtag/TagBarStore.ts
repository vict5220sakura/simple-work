import {type TagType} from '@/components/headtag/TagType'
import {defineStore} from 'pinia'
import {ref} from 'vue'
import {defaultPath, MenuConfig, findbyPath} from '@/router/MenuConfig'
import {getLength, indexLength} from '@/utils/StrUtils'
import { usePermissionStore } from '@/components/role/permissionStore'
import { storeToRefs } from 'pinia';



export const useTagBarStore =  defineStore('TagBarStore', ()=>{

    // 权限
    const permissionStore = usePermissionStore()

    let tagDefault: TagType = findbyPath(defaultPath)

    let tags = ref<TagType[]>([
        
    ])

    tags.value.push(tagDefault)

    // let scrollLeft = ref(0)

    // 标签初始化时，设置初始位置, 当渲染还为完成时
    let setTagLeftInitActionPath = ref<string|null>("")

    

    let initTags = ()=> {
        for(let i = tags.value.length - 1 ; i >= 0; i--){
            let tag = tags.value[i]
            if(permissionStore.hasPermission(tag.permission) == false){
                tags.value.splice(i, 1);
            }
        }

        for(let tag of tags.value){
            if(getLength(tag.name) > 12){
                tag.tagName = tag.name.substring(0, indexLength(tag.name, 12)) + "..."
                tag.showTooltip = true
            }else{
                tag.tagName = tag.name
                tag.showTooltip = false
            }
            tag.tooltipVisible = false
        }
    }

    let initTag = (tag:TagType)=>{
        if(getLength(tag.name) > 12){
            tag.tagName = tag.name.substring(0, indexLength(tag.name, 12)) + "..."
            tag.showTooltip = true
        }else{
            tag.tagName = tag.name
            tag.showTooltip = false
        }
        tag.tooltipVisible = false
    }

    const reload = ()=>{
        tags.value = []
        tags.value.push(tagDefault)
        setTagLeftInitActionPath.value = ""
        initTags()
    }

    return {tags, setTagLeftInitActionPath, initTags, initTag, reload}
},{
    persist: true,
})

