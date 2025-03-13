import {ref, onMounted} from "vue"
import {request} from "@/utils/Request"
import {md5} from "js-md5";
import { storeToRefs } from 'pinia';
import {type R} from "@/types/R"
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {useLoginStore} from "@/pages/login/LoginStore"
import {usePermissionStore} from "@/components/role/permissionStore"
import {findDefaultPage} from "@/router/MenuConfig"
import { useTagBarStore } from "@/components/headtag/TagBarStore";
import type { BuserVO } from "@/pages/application/buscenter/inwork/buser/BuserTypes";
import {router} from "@/router/Router"
import {useApplicationServiceHooks} from "@/pages/application/applicationServiceHooks"
import type { KeyValue } from "@/types/KeyValueType";
import { keyValueGetByKey } from "@/utils/KeyValueUtils";
import type { Menu } from "@/components/menu/MenuTypes";
import {useRoute} from 'vue-router'



export const useLoginHooks = function(){

    let tagBarStore = useTagBarStore()
    let {tags} = storeToRefs(tagBarStore)

    let {token, loginBuserCode} = storeToRefs(useLoginStore())
    let permissionStore = usePermissionStore()
    let {permissionList} = storeToRefs(permissionStore)
    
    let applicationServiceHooks = useApplicationServiceHooks()
    
    
    // 用户名密码
    let bUserCode = ref("")
    let password = ref("")
    
    // 登录
    async function login(){
        let r:R<any> = await request.post("/bUserLogin/loginIn", {
            buserCode: bUserCode.value,
            password: md5(password.value)
        })
        if(r.code == "success"){
            
            // 更新登录后的buserCode信息, 如果不一致, 清空用户缓存
            if(r.data.buserCode == loginBuserCode.value){
                // 同一个人登录
            }else{
                applicationServiceHooks.reloadCache()
                loginBuserCode.value = r.data.buserCode
            }
    
            token.value = r.data.token
            permissionList.value = r.data.permissionList
    
            ElMessage.success("登录成功")
            for(let tag of tags.value){
                if(tag.choosed == true){
                    router.push(tag.path)
                    return;
                }
            }

            let menu:Menu = findDefaultPage(permissionList.value)!
            router.push(menu.path!)
        }
    }

    let loginPageValue = ref<string>()

    onMounted(async()=>{
        let keyValue:KeyValue = await keyValueGetByKey("loginPage")
        loginPageValue.value = keyValue.value1
    })

    const loginTest = async()=>{
        let r:R<any> = await request.post("/bUserLogin/loginIn", {
            buserCode: "test",
            password: md5("123456")
        })
        if(r.code == "success"){
            
            // 更新登录后的buserCode信息, 如果不一致, 清空用户缓存
            if(r.data.buserCode == loginBuserCode.value){
                // 同一个人登录
            }else{
                applicationServiceHooks.reloadCache()
                loginBuserCode.value = r.data.buserCode
            }
    
            token.value = r.data.token
            permissionList.value = r.data.permissionList
    
            ElMessage.success("登录成功")
            for(let tag of tags.value){
                if(tag.choosed == true){
                    router.push(tag.path)
                    return;
                }
            }
            let menu:Menu = findDefaultPage(permissionList.value)!
            router.push(menu.path!)
        }
    }

    let ICPValue = ref<string>()
    onMounted(async()=>{
        let keyValue:KeyValue = await keyValueGetByKey("ICPValue")
        ICPValue.value = keyValue.value1
    })

    const route = useRoute()
    onMounted(async ()=>{

        let username = route.query.username;
        let password = route.query.password;
        if(username == null || username == undefined || String(username).trim() == ""){
            return;
        }
        if(password == null || password == undefined || String(password).trim() == ""){
            return;
        }
        let r:R<any> = await request.post("/bUserLogin/loginIn", {
            buserCode: username,
            password: md5(String(password))
        })
        if(r.code == "success"){
            
            // 更新登录后的buserCode信息, 如果不一致, 清空用户缓存
            if(r.data.buserCode == loginBuserCode.value){
                // 同一个人登录
            }else{
                applicationServiceHooks.reloadCache()
                loginBuserCode.value = r.data.buserCode
            }
    
            token.value = r.data.token
            permissionList.value = r.data.permissionList
    
            ElMessage.success("登录成功")
            for(let tag of tags.value){
                if(tag.choosed == true){
                    router.push(tag.path)
                    return;
                }
            }
            let menu:Menu = findDefaultPage(permissionList.value)!
            router.push(menu.path!)
        }
    })

    return {
        bUserCode,
        password,
        login,
        loginPageValue,
        loginTest,
        ICPValue
    }
}

