export interface PageInfo<T>{
    pageNum:string,
    pageSize:string,
    total:string,
    totalPage:string,
    list:T[]
}