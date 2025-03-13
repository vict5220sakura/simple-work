export enum DebugMode{
    DebugMode_yes = "DebugMode_yes",
    DebugMode_no = "DebugMode_no"
}

export enum LogSwitch{
    LogSwitch_yes = "LogSwitch_yes",
    LogSwitch_no = "LogSwitch_no"
}

export interface AppConfig{
    debugMode:DebugMode
    logSwitch:LogSwitch
}