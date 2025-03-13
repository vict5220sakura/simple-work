export const useQiniuUploadServiceHooks = function(){
    
    const urlGetFileName = (url:string):string|undefined=>{
        if(url == undefined || url == null){
            return undefined;
        }
        try{
            return decodeURIComponent(url.substring(url.lastIndexOf("attname") + 8))
        }catch(err){
            console.error(err)
            return undefined;
        }
    }

    return {
        urlGetFileName
    };
}