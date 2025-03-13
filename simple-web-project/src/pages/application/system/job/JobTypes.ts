export interface Job{
    id:string,
    jobName:string,
    beanName:string,
    methodName:string,
    cron:string,
    status:string
}

export interface JobHistory{
    id:string,
    runTime:string,
    status:string,
    stopTime:string,
    exception:string,
}