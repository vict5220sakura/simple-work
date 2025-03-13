package com.vict.utils;

public class TrainSeatClassUtil {
    public static String getSeatNameBySeatClass(String seatClass){
        if(seatClass.trim().equals("9")){
            return "商务座";
        }else if(seatClass.trim().equals("P")){
            return "特等座";
        }else if(seatClass.trim().equals("M")){
            return "一等座";
        }else if(seatClass.trim().equals("DM")){
            return "卧代一等座";
        }else if(seatClass.trim().equals("O")){
            return "二等座";
        }else if(seatClass.trim().equals("DO")){
            return "卧代二等座";
        }else if(seatClass.trim().equals("6")){
            return "高级软卧";
        }else if(seatClass.trim().equals("F")){
            return "动卧";
        }else if(seatClass.trim().equals("4")){
            return "软卧";
        }else if(seatClass.trim().equals("3")){
            return "硬卧";
        }else if(seatClass.trim().equals("2")){
            return "软座";
        }else if(seatClass.trim().equals("D2")){
            return "软卧代软座";
        }else if(seatClass.trim().equals("E")){
            return "特等软座";
        }else if(seatClass.trim().equals("1")){
            return "硬座";
        }else if(seatClass.trim().equals("D1")){
            return "硬卧代硬座";
        }else if(seatClass.trim().equals("I")){
            return "一等卧";
        }else if(seatClass.trim().equals("J")){
            return "二等卧";
        }else if(seatClass.trim().equals("A")){
            return "高级动卧";
        }else if(seatClass.trim().equals("5")){
            return "包厢硬卧";
        }else if(seatClass.trim().equals("H")){
            return "一人软包";
        }else if(seatClass.trim().equals("7")){
            return "一等软座";
        }else if(seatClass.trim().equals("8")){
            return "二等软座";
        }else if(seatClass.trim().equals("0")){
            return "无座";
        }else if(seatClass.trim().equals("Q")){
            return "多功能座";
        }else{
            return null;
        }
    }
}
