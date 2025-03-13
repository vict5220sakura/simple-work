import { baseApi } from "@/MyEnv"


/** return 192.168.0.229:13000 */
export const getHost = () => {
  let str = baseApi
  str = str.replace("https://", "") 
  str = str.replace("http://", "")
  str = str.replace(/[\/\\].*/, "")
  return str;
}

/** 判断是否ssl */
export const isSsl = () => {
  let str = baseApi
  if (str.startsWith("https://")) {
    return true
  } else {
    return false
  }
}