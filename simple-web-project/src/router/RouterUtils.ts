
import {router} from '@/router/Router'

interface OpenConfig {
    title?:string
    height?:number
    width?:number
    top?:number
    left?:number
}

class RouterUtils {
    
    /** 打开新窗口 */
    static openNewWin(config: any, openConfig?:OpenConfig){
        const { href } = router.resolve(
            config
        )

        let {height, width, top, left, title} = openConfig || {}

        let widthStr = width ? `width=${width},` : ""
        let heightStr = height ? `height=${height},` : ""
        let topStr = top ? `top=${top},` : ""
        let leftStr = left ? `left=${left},` : ""
        let titleStr = title ? `${title}` : "_blank"

        let openConfigStr = `
                            menubar=0, 
                            scrollbars=1, 
                            resizable=1, 
                            status=1, 
                            titlebar=0, 
                            toolbar=0, 
                            location=1, 
                            ${heightStr}
                            ${widthStr}
                            ${topStr} 
                            ${leftStr}
                            `

        window.open(
            href, 
            titleStr,
            openConfigStr
        )
    }

    /** 打开新tab */
    static openNewTab(config: any, openConfig?:OpenConfig){
        let {height, width, top, left, title} = openConfig || {}

        let titleStr = title ? `${title}` : "_blank"

        const { href } = router.resolve(
            config
        )
        const newWindow = window.open(
            href, 
            titleStr)
    }
    
    /** 打开新窗口 */
    static openNewWin2(config: any, openConfig?:OpenConfig){

        const { href } = router.resolve(
            config
        )

        let {height, width, top, left, title} = openConfig || {}
        
        

        let widthStr = width ? `width=${width},` : ""
        let heightStr = height ? `height=${height},` : ""
        let topStr = top ? `top=${top},` : ""
        let leftStr = left ? `left=${left},` : ""
        let titleStr = title ? `${title}` : document.title

        let openConfigStr = `
                            menubar=0,
                            scrollbars=1, 
                            resizable=1, 
                            status=1, 
                            titlebar=0, 
                            toolbar=0, 
                            location=1, 
                            ${heightStr}
                            ${widthStr}
                            ${topStr} 
                            ${leftStr}
                            `
        console.log(openConfigStr)
        const newWindow = window.open('about:blank', titleStr, openConfigStr);
        newWindow!.document.title = titleStr!;
        let iframe = document.createElement('iframe');
        iframe.src = href;
        iframe.style.width = '100%';
        iframe.style.height = '100vh';
        iframe.style.margin = '0';
        iframe.style.padding = '0';
        iframe.style.overflow = 'hidden';
        iframe.style.border = 'none';
        newWindow!.document.body.style.margin = '0';
        newWindow!.document.body.appendChild(iframe);
    }

    /** 打开新tab */
    static openNewTab2(config: any, openConfig?:OpenConfig){
        let {height, width, top, left, title} = openConfig || {}

        let titleStr = title ? `${title}` : document.title

        const { href } = router.resolve(
            config
        )

        const newWindow = window.open('about:blank', titleStr);
        newWindow!.document.title = titleStr!;
        let iframe = document.createElement('iframe');
        iframe.src = href;
        iframe.style.width = '100%';
        iframe.style.height = '100vh';
        iframe.style.margin = '0';
        iframe.style.padding = '0';
        iframe.style.overflow = 'hidden';
        iframe.style.border = 'none';
        newWindow!.document.body.style.margin = '0';
        newWindow!.document.body.appendChild(iframe);
    }
}

export {RouterUtils};