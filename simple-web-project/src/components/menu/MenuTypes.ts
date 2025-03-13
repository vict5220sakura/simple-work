export interface Menu{
    type: string, // 类型 menu btn
    name: string, // 名称
    permission: string, // 权限码
    iconSvg?: string // 图标
    id?: string, // 
    path?: string // 菜单路径
    showTip?: boolean // 是否展示Tip
    children?: Menu[], // 子菜单
}