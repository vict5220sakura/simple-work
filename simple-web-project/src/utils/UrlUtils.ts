/** return 192.168.0.229:13000 */
export const getHost = ()=>{
    let str = window.location.href
    str = str.replaceAll("https://", "")
    str = str.replaceAll("http://", "")
    str = str.replace(/[\/\\].*/, "")
    return str;
}

/** 判断是否ssl */
export const isSsl = ()=>{
    let str = window.location.href
    if(str.startsWith("https://")){
        return true
    }else{
        return false
    }
}