import {permissionList} from "@/config/PermissionConfig"
import type { BuserVO } from "@/pages/application/buscenter/inwork/buser/BuserTypes"
import {type PageInfo} from "@/types/PageInfo"
import {type PageRequest} from "@/types/PageRequest"
import {type R} from "@/types/R"
import {request} from "@/utils/Request"
import { ref, onMounted, useTemplateRef, onUpdated, defineExpose } from "vue"
import { Choosed } from "./ChooseBuserTypes"



export default function(){
    const buserRef = useTemplateRef<any>("buserRef")

    // 组织id
    let organizeId;
    let props:any;
    const init = (organizeId_:any, props_:any)=>{
        organizeId = organizeId_
        props = props_
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
    let checkMap:Map<string,Choosed>;
    let select = async()=>{
        tableLoading.value = true;
        let r:R<PageInfo<BuserVO>> = await request.post("/bUser/selectBuserList", {
            username: selectUsername.value,
            buserCode: selectBuserCode.value,
            pageNum: pageRequest.value.pageNum,
            pageSize: pageRequest.value.pageSize
        })
        
        if(r.code == "success"){
            if(props && props.checkChoose){ // 检查是否可选择
                let ids = []
                for(let buserVO of r.data.list){
                    ids.push(buserVO.id)
                }
                buserRef.value.clearSelection()
                checkMap = await props.checkChoose(ids)
            }
            pageInfo.value = r.data
        }
        tableLoading.value = false;
    }
    // 启动查询一次
    onMounted(()=>{
        select()
    })

    /** 选择是否可以选 */
    const selectable = (row: BuserVO) => {
        if(checkMap == undefined){
            return false
        }
        return (checkMap.get(row.id) != Choosed.choosed);
    }

    const getSelectionRows = ():BuserVO[]=>{
        return buserRef.value.getSelectionRows()
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
        selectable,
        clearSelection,
        getSelectionRows
    };
}