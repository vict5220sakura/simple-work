export const getNow = ()=>{
    var myDate = new Date();
    //获取当前年
    var year = myDate.getFullYear();
    //获取当前月
    var month = myDate.getMonth() + 1;
    //获取当前日
    var date = myDate.getDate();
    var h = myDate.getHours(); //获取当前小时数(0-23)
    var m = myDate.getMinutes(); //获取当前分钟数(0-59)
    var s = myDate.getSeconds();
    //获取当前时间
    var now = year + '_' + conver(month) + "_" + conver(date) + "__" + conver(h) + '_' + conver(m) + "_" + conver(s);
    return now;
}

export const conver = (s:any)=> {
    return s < 10 ? '0' + s : s;
}
