<template>
    <div calss="flex">
        <div>
            <el-radio-group v-model="linkChoose">
                <el-radio :value="false">单选选择</el-radio>
                <el-radio :value="true">级联选择</el-radio>
            </el-radio-group>
            
        </div>
        <el-tree
            :check-strictly="true"
            ref="treeRef"
            style="max-width: 600px"
            :data="data"
            show-checkbox
            node-key="permission"
            :default-checked-keys="permissionList"
            @check-change="checkChange"
        />
    </div>
    
</template>

<script setup lang="ts" name="permissionList">
import { defineProps, withDefaults, onMounted , watch, toRefs, ref, onUpdated} from 'vue';
import {MenuConfig, findAllChildrenDeep} from '@/router/MenuConfig'
import {type Menu} from "@/components/menu/MenuTypes"

// 选中数据
let permissionList = defineModel<string[]>()

// 级联模式
let linkChoose = ref<boolean>(false)

let treeRef = ref<any>(null)

// 展示数据
let data:any[] = []

function packageData(menuList:Menu[], dataList:any[]){
    for(let menu of menuList){
        let dataItem:any = {}
        dataItem.permission = menu.permission
        dataItem.label = menu.name
        dataList.push(dataItem)
        if(menu.children){
            dataItem.children = []
            packageData(menu.children, dataItem.children)
        }
    }

    
}
packageData(MenuConfig, data)


let checkChange = (node:any, checked:boolean)=>{
    if(linkChoose.value){
        let allChildrenMenus = findAllChildrenDeep(MenuConfig, node.permission)
        if(allChildrenMenus){
            for(let menu of allChildrenMenus){
                treeRef.value.setChecked(menu.permission, checked, false)
            }
        }
    }
    
    permissionList.value = [...treeRef.value.getCheckedKeys()]
}

watch(permissionList, (newPerson, oldPerson)=>{
    if(treeRef && treeRef.value){
        treeRef.value.setCheckedNodes(newPerson)
    }
}, {deep: true /*深度监视*/, immediate: true/*一上来先执行一下*/})


</script>

<style scoped>

</style>
@/router/MenuConfig