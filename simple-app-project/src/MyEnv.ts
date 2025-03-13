export enum Env{
	local = "local",
	dev = "dev",
	test = "test",
	pro = "pro",
}
export const env:Env = Env.pro; // 配置环境

// 基础请求路径
export let baseApi:string;
if(env == Env.dev){
	baseApi = "http://192.168.98.104:30001/simple-server/api"
}else if(env == Env.test){
	baseApi = "http://192.168.98.106/simple-server/api"
}else if(env == Env.pro){
	baseApi = "https://www.vict5220.top/simple-server/api"
}else if(env == Env.local){
	baseApi = "http://127.0.0.1:30001/simple-server/api"
}

export let apiBaseWsUrl:string = "/simple-server/ws"
// if(env == Env.dev){
// 	apiBaseWsUrl = "/simple-server/ws"
// }else if(env == Env.test){
// 	apiBaseWsUrl = "/simple-server/ws"
// }else if(env == Env.pro){
// 	apiBaseWsUrl = "/simple-server/ws"
// }
