export function getLength(str: string) {
    let realLength = 0, len = str.length, charCode = -1;
    for (var i = 0; i < len; i++) {
        charCode = str.charCodeAt(i);
        if (charCode >= 0 && charCode <= 128)          //重点
            realLength += 1;
        else
            realLength += 2;
    }
    return realLength;
}

export function indexLength(str: string, index: number) {
    let realLength = 0, len = str.length, charCode = -1;
    for (var i = 0; i < len; i++) {
        charCode = str.charCodeAt(i);
        if (charCode >= 0 && charCode <= 128)          //重点
            realLength += 1;
        else
            realLength += 2;

        if (realLength >= index) {
            return i + 1
        }
    }
    return realLength;
}