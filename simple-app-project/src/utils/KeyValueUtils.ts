import type { KeyValue } from "@/types/KeyValueType"
import { request } from "./Request"
import type { R } from "@/types/R"

export const keyValueGetByKey = async (key:string):Promise<KeyValue>=>{
    let r:R<KeyValue> = await request.post("/keyValue/keyValueByKey?key=" + key)
    return r.data
}