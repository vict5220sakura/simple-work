import type { BuserVO } from "@/pages/application/buscenter/inwork/buser/BuserTypes"
import type { R } from "@/types/R"
import { request } from "@/utils/Request"

export const useLoginServiceHooks = ()=>{
    const buserInfo = async ():Promise<BuserVO>=>{
        let r:R<BuserVO> = await request.post("/bUserLogin/buserInfo")
        return r?.data
    }

    const isLogin = async ():Promise<boolean>=>{
        try{
            let r:R<any> = await request.post("/bUserLogin/isLogin")
            return r.data.isLogin == "loginIn"
        }catch(err){
            return false;
        }
    }

    return {buserInfo, isLogin}
}