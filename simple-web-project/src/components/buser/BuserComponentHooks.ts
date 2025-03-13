import {permissionList} from "@/config/PermissionConfig"
import type { BuserVO } from "@/pages/application/buscenter/inwork/buser/BuserTypes"
import {type PageInfo} from "@/types/PageInfo"
import {type PageRequest} from "@/types/PageRequest"
import {type R} from "@/types/R"
import { request } from "@/utils/Request"
import { ref, onMounted, useTemplateRef, onUpdated, defineExpose } from "vue"
import { type Choosed } from "./ChooseBuserTypes"


export default function(){
    const buserRef = useTemplateRef<any>("buserRef")
    // 组织id
    let organizeId:any;
    let emits:any;
    const init = (organizeId_:any, emits_:any)=>{
        organizeId = organizeId_
        emits = emits_
    }
    

    // 刷新选中行
    const clearSelection = ()=>{
        select()
    }

    /** 查询 */
    let tableLoading = ref<boolean>(false)
    // 请求参数
    let selectUsername = ref<string>()
    let selectBuserCode = ref<string>()
    let pageRequest = ref<PageRequest>({
        pageNum: 1,
        pageSize: 20
    })
    // 响应参数
    let pageInfo = ref<PageInfo<BuserVO>>()
    // 重置
    let reset = ()=>{
        selectUsername.value = ""
        selectBuserCode.value = ""
        select()
    }
    // 查询
    let select = async()=>{
        tableLoading.value = true;
        let r:R<PageInfo<BuserVO>> = await request.post("/bUser/selectBuserList", {
            organizeId: organizeId.value,
            username: selectUsername.value,
            buserCode: selectBuserCode.value,
            pageNum: pageRequest.value.pageNum,
            pageSize: pageRequest.value.pageSize
        })
        
        if(r.code == "success"){
            buserRef.value.clearSelection()
            pageInfo.value = r.data
        }
        tableLoading.value = false;
    }
    // 启动查询一次
    onMounted(()=>{
        select()
    })

    const removeBuser = (id:string)=>{
        emits("removeBuser", id)
    }

    const getSelectionRows = ():BuserVO[]=>{
        return buserRef.value.getSelectionRows()
    }

    const removeBuserBatch = ()=>{
        emits("removeBuserBatch")
    }

    return {
        init,
        permissionList,
        selectUsername,
        selectBuserCode,
        reset,
        select,
        tableLoading,
        pageInfo,
        pageRequest,
        clearSelection,
        removeBuser,
        getSelectionRows,
        removeBuserBatch
    };
}