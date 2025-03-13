import { type R } from "@/types/R";
import { baseApi } from "@/MyEnv"
import { useLoginStore } from "@/pages/login/loginStore";
import { storeToRefs } from "pinia";

export const request = {
  post: async (url: string, data?: any): Promise<R<any>> => {

    let loginStore = useLoginStore()
    let { aToken } = storeToRefs(loginStore)

    if (url?.startsWith("http")) {

    } else {
      if (url?.startsWith("/")) {
        url = baseApi + url
      } else {
        url = baseApi + "/" + url
      }
    }

    let res = await uni.request({
      url: url,
      data: data,
      header: {
        "content-type": "application/json",
        "aToken": aToken.value
      },
      method: "POST",
    })

    let r: R<any> = res.data as R<any>
    if (r.code == "success") {

    } else if (r.code == "authFail") {
      uni.showToast({
        title: r.msg,
        duration: 1000,
        position: 'bottom',
        icon: 'none'
      });
      uni.redirectTo({ url: "/pages/login/login" })
    } else {
      uni.showToast({
        title: r.msg,
        duration: 1000,
        position: 'bottom',
        icon: 'none'
      });
    }

    return r;
  },
  get: async (url: string): Promise<R<any>> => {

    if (url?.startsWith("http")) {

    } else {
      if (url?.startsWith("/")) {
        url = baseApi + url
      } else {
        url = baseApi + "/" + url
      }
    }

    let res = await uni.request({
      url: url,
      method: "GET",
    })

    return res.data as R<any>;
  }
}