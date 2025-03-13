import {type AppConfig } from "@/types/AppConfigType"
import { type R } from "@/types/R"
import { request } from "@/utils/Request"

export const useAppConfigServiceHooks = () => {
    const getAppConfig = async():Promise<AppConfig>=>{
        let r:R<AppConfig> = await request.post("/customerLogin/getAppConfig")
        return r.data
    }
    return {
        getAppConfig
    }
}