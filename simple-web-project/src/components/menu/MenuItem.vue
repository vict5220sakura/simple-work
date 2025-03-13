<template>
    <el-tooltip 
        placement="right" 
        :offset="4" 
        :disabled="menu.showTip?false:true" 
        :enterable="false" 
        :show-arrow="true" 
        :content="menu.name"
        :hide-after="0"
        effect="dark"
        :show-after="0"
        popper-class="menuTooltip"
    >
        <component 
            v-permission="menu.permission"
            :index="menu.id" 
            :is="menu.children ? 'el-sub-menu' : 'el-menu-item'"
            @click.self="menuChick"    
        >
            
            <template v-if="menu.children" #title>
                <i v-html='menu.iconSvg' class="el-icon"></i>
                <span>{{menu.name}}</span>
            </template>

            <MenuItem v-for="menuChild in menu.children" :menu="menuChild" :key="menuChild.id" ></MenuItem>

            <i v-if="!menu.children" v-html='menu.iconSvg' class="el-icon"></i>
            
            
            <span v-if="!menu.children" truncate>{{ menu.name }}</span>
        </component>
    </el-tooltip>
</template>

<script setup lang="ts" name="MenuItem">
import {type Menu} from "@/components/menu/MenuTypes"
import { useRouter } from 'vue-router'

const router = useRouter()

const props = defineProps<{menu:Menu}>()
let menu = props.menu

function menuChick(){
    router.push({
        path: menu.path
    })
}

</script>

<style scoped>

</style>
