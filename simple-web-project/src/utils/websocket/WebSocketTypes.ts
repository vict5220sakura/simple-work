export enum WsType{
    ping = "ping",
    pong = "pong",
    testmessage = "testmessage",
    ack = "ack"
}

export interface WsMessage{
    id?:string,
    type:WsType,
    data?:string
}