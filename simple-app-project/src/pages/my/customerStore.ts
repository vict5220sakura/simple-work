import { type AppConfig } from '@/types/AppConfigType'
import { type R } from '@/types/R'
import { request } from '@/utils/Request'
import {defineStore} from 'pinia'
import {ref} from 'vue'
import { type Customer } from './CustomerTypes'

export const useCustomerStore =  defineStore('appCustomerStore', ()=>{

    let customerInfo = ref<Customer>()

    const getCustomerInfo = async ():Promise<Customer>=>{
        let r:R<Customer> = await request.post("/appCustomer/customerInfo")
        return r.data;
    }

    const isAppLogin = async ():Promise<boolean>=>{
        try{
            let r:R<any> = await request.post("/customerLogin/isAppLogin")
            return r.data.isLogin == "loginIn"
        }catch(err){
            return false;
        }
    }

    return {customerInfo, getCustomerInfo, isAppLogin}
},{
    persist: {
        storage: {
            getItem: uni.getStorageSync,
            setItem: uni.setStorageSync
        }
    }
})

