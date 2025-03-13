// 创建一个路由器, 并暴露出去
// 引入createRouter
import { createRouter, createWebHistory } from "vue-router";
import {Optional} from "@/utils/Optional";
import {emitter} from "@/utils/Emitter";
import { storeToRefs } from 'pinia';
import { useApplicationStore } from '@/pages/application/applicationStore'
import { useTagBarStore } from '@/components/headtag/TagBarStore'
import { defaultPath, MenuConfig, findbyPath } from '@/router/MenuConfig'
import {type Menu} from "@/components/menu/MenuTypes"
import {randomId} from '@/utils/IdUtils'

// 引入一个个的路由组件

// 创建路由器
const router = createRouter({
    history: createWebHistory("simple-server/web"), // 路由器的工作模式(history,hash)
    routes: [ // 一个一个的路由
        {
            path: "/",
            redirect: "/application/homePage"
        },
        {
            path: "/application",
            component: () => import("@/pages/application/application.vue"),
            children:[
                {
                    path: "homePage",
                    component: () => import("@/pages/application/HomePage.vue")
                },
                {
                    path: "testPage1",
                    component: () => import("@/pages/application/learn/testPage1.vue")
                },
                {
                    path: "testPage2",
                    component: () => import("@/pages/application/learn/testPage2.vue")
                },
                {
                    path: "testPage3Pinia",
                    component: () => import("@/pages/application/learn/testPage3Pinia.vue")
                },
                {
                    path: "testTailwindcss",
                    component: () => import("@/pages/application/learn/testTailwindcss.vue")
                },
                {
                    path: "testTailwindcss2",
                    component: () => import("@/pages/application/learn/testTailwindcss2.vue")
                },
                {
                    path: "testTailwindcss3Flex",
                    component: () => import("@/pages/application/learn/testTailwindcss3Flex.vue")
                },
                {
                    path: "testPageKeepScroll",
                    component: () => import("@/pages/application/learn/testPageKeepScroll.vue")
                },
                {
                    path: "testMenus",
                    component: () => import("@/pages/application/learn/testMenus.vue")
                },
                {
                    path: "testPageTag",
                    component: () => import("@/pages/application/learn/testPageTag.vue"),
                },
                {
                    path: "axios01",
                    component: () => import("@/pages/application/learn/axios/axios01.vue"),
                },
                {
                    path: "axios02",
                    component: () => import("@/pages/application/learn/axios/axios02.vue"),
                },
                {
                    path: "buser",
                    component: () => import("@/pages/application/buscenter/inwork/buser/BUser.vue"),
                },
                {
                    path: "role",
                    component: () => import("@/pages/application/buscenter/inwork/role/Role.vue"),
                },
                {
                    path: "keyValue",
                    component: () => import("@/pages/application/system/keyvalue/KeyValue.vue"),
                },
                {
                    path: "testUpload",
                    component: () => import("@/pages/application/learn/testUpload.vue"),
                },
                {
                    path: "testWangEditor",
                    component: () => import("@/pages/application/learn/testWangEditor.vue"),
                },
                {
                    path: "ImageShow",
                    component: () => import("@/pages/application/system/imageshow/ImageShow.vue"),
                },
                {
                    path: "EditorShow",
                    component: () => import("@/pages/application/system/editorshow/EditorShow.vue"),
                },{
                    path: "customerOption",
                    component: () => import("@/pages/application/customer/CustomerOption.vue"),
                },
                {
                    path: "job",
                    component: () => import("@/pages/application/system/job/Job.vue"),
                },
                {
                    path: "testTree",
                    component: () => import("@/pages/application/learn/TestTree.vue"),
                },
                {
                    path: "organize",
                    component: () => import("@/pages/application/buscenter/inwork/organize/Organize.vue"),
                },
                {
                    path: "testWs",
                    component: () => import("@/pages/application/learn/testWs.vue"),
                },
                {
                    path: "testWs2",
                    component: () => import("@/pages/application/learn/testWs2.vue"),
                },
                {
                    path: "customPage",
                    component: () => import("@/pages/application/buscenter/custompage/CustomPage.vue"),
                },
                {
                    path: "aiExample",
                    component: () => import("@/pages/application/ai/AiExample.vue"),
                },
                {
                    path: "chinesePoetry",
                    component: () => import("@/pages/application/chinesepoetry/ChinesePoetry.vue"),
                },
                {
                    path: "images",
                    component: () => import("@/pages/application/system/images/Images.vue"),
                },
                {
                    path: "learnDrag",
                    component: () => import("@/pages/application/learn/learnDrag.vue"),
                }
            ]
        },
        {
            path: "/testOpenPages1",
            component: () => import("@/pages/openPages/testOpenPages1.vue"),
        },
        {
            path: "/testOpenPages2",
            component: () => import("@/pages/openPages/testOpenPages2.vue"),
        },
        {
            path: "/login",
            component: () => import("@/pages/login/Login.vue"),
        },
        {
            path: "/customPageShow",
            component: () => import("@/pages/openPages/CustomPageShow.vue"),
        }
    ]
})


// 全局前置钩子
router.beforeEach((to, from, next)=>{
    let { activeMenuIndex } = storeToRefs(useApplicationStore())
    let tagBarStore = useTagBarStore()
    let {initTags, initTag} = tagBarStore
    let { tags , setTagLeftInitActionPath} = storeToRefs(tagBarStore)

    emitter.emit("getContextTopLeft", from.fullPath)
    
    next()
    
    // 菜单栏选择to.fullPath菜单菜单
    activeMenuIndex.value = to.fullPath

    // tag栏选择to.fullPath菜单
    let path = to.fullPath
    let item: Menu = findbyPath(path)
    if(item == null){
        return;
    }
    let existTag:boolean = false;
    for (let tag of tags.value) {
        if (tag.path == item.path) {
            for (let tag of tags.value) {
                tag.choosed = false
            }
            tag.choosed = true
            existTag = true;
            break;
        }
    }
    
    if(existTag === false){ // 不存在 新增
        let newTag = {
            id: randomId(),
            name: item.name,
            choosed: true,
            path: path as string,
            permission: item.permission,
        }
        tags.value.push(newTag)
        initTag(newTag)
    
        for (let tag of tags.value) {
            tag.choosed = false
        }
        newTag.choosed = true
    }
    

    // 重新设置内框高度事件
    emitter.emit("setContextTopLeft", to.fullPath)

    // 设置tag左边距离事件
    emitter.emit("setTagLeft", to.fullPath)
    // 初始化路由监听, 当渲染还未完成时触发
    setTagLeftInitActionPath.value = to.fullPath
})

// 全局后置钩子
router.afterEach((to, from)=>{
    
})

// 全局解析钩子
// router.beforeResolve((to,from,next)=>{
// })

export {router};