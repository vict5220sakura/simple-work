import type { R } from "@/types/R";
import { request } from "@/utils/Request";

export const useRoleServiceHooks = ()=>{
    const selectRoleList = async (rolename?:string, pageNum:number=1, pageSize:number=20):Promise<R<any>>=> {
        let r:R<any> = await request.post("/role/selectRoleList", {
            rolename,
            pageNum,
            pageSize
        })
        return r;
    }

    return {selectRoleList}
}