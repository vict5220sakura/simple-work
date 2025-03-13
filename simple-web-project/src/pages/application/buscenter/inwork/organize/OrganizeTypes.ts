export interface OrganizeVO{
    id:string,
    fatherId:string,
    organizeName:string,
    children:OrganizeVO[]
} 

export interface CheckChooseVO{
    map:Record<string,string>
}