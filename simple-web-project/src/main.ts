import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'

// 引入路由器
import {router} from '@/router/Router'

// 引入pinia
import {createPinia} from "pinia"

// 引入pinia持久化插件
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
// 引入全局组件通信工具
import {emitter} from "@/utils/Emitter"

// 引入elementPlus
import ElementPlus from "element-plus"
import "element-plus/dist/index.css"
import * as ElementPlusIconsVue from "@element-plus/icons-vue"
import zhCn from "element-plus/es/locale/lang/zh-cn"

// 引入tailwindcss
import "./tailwindcss.css"

// 创建app
const app = createApp(App)

// 创建pinia
const pinia = createPinia()
// 配置pinia持久化
pinia.use(piniaPluginPersistedstate)
// 安装pinia
app.use(pinia)

// 使用路由器
app.use(router)

// elementPlus配置
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.use(ElementPlus, {locale: zhCn})

// 挂载app
app.mount('#app')


/** 权限 */
import { usePermissionStore } from '@/components/role/permissionStore'
app.directive('permission', {
    mounted(el, binding) {
        let store = usePermissionStore()
        const permission = binding.value
        if(!store.hasPermission(permission)){
            el.style.display = 'none'
        }
    },
    updated(el, binding) {
        let store = usePermissionStore()
        const permission = binding.value
        if(!store.hasPermission(permission)){
            el.style.display = 'none'
        }
    }
})

