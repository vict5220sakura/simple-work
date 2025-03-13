export interface TagType{
    id: string,
    name: string,
    choosed: boolean,
    path: string,
    tagName?: string, // tag标签名称
    showTooltip?: boolean, // 是否展示Tooltip
    tooltipVisible?: boolean, // 临时存储tooltip展示变量
    permission: string
}