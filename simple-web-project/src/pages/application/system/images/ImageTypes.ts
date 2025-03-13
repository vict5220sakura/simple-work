export enum ImageType{
    url = "url",
    svg = "svg",
    base64 = "base64"
}

export interface Image{
    id:string,
    url:string,
    attname:string
    isCheck:boolean
    imageType:ImageType,
    svgCode:string,
    base64Code:string
}

export interface ImageSize{
    cardW:string,
    cardH:string,
    imageW:string,
    imageH:string,
}

export const ImageSizeInstant = {
    small: {
        cardW: "w-20",
        cardH: "h-20",
        imageW: "w-16",
        imageH: "h-16",
    },
    middle: {
        cardW: "w-40",
        cardH: "h-40",
        imageW: "w-32",
        imageH: "h-32",
    },
    large: {
        cardW: "w-60",
        cardH: "h-60",
        imageW: "w-48",
        imageH: "h-48",
    },
}

