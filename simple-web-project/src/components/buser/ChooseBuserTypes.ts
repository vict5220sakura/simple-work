export interface CheckFunc {
    (ids: string[]): Promise<Map<string,Choosed>>;
}

export enum Choosed{
    choosed = "choosed",
    not = "not"
}