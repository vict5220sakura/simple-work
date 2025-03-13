package com.vict.utils;

import com.vict.framework.enums.MonthNameEnum;
import com.vict.framework.myannotation.MyDescription;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

public class TimeUtil {

    public static final long day_msec = 24L * 60L * 60L * 1000L;
    public static final long week_msec = 7L * 24L * 60L * 60L * 1000L;
    public static LocalDateTime convert(Long timestamp){
        if(timestamp == null){
            return null;
        }
        return new Date(timestamp).toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime();
    }
    public static Long convert(LocalDateTime time){
        if(time == null){
            return null;
        }
        return Date.from(time.toInstant(ZoneOffset.of("+8"))).getTime();
    }

    public static Long getTime(String format, String time){
        if(format == null || format.trim().equals("")){
            format = "yyyy-MM-dd HH:mm:ss";
        }
        if(time == null || time.trim().equals("")){
            return null;
        }
        try {
            return new SimpleDateFormat(format).parse(time).getTime();
        } catch (ParseException e) {
            throw new RuntimeException("格式化时间异常");
        }
    }

    public static String getTimeStr(String format, LocalDate localDate){
        if(format == null || format.trim().equals("")){
            format = "yyyy-MM-dd HH:mm:ss";
        }
        if(localDate == null){
            return null;
        }
        long timestamp = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return new SimpleDateFormat(format).format(timestamp);
    }

    public static String getTimeStr(String format, Long time){
        if(format == null || format.trim().equals("")){
            format = "yyyy-MM-dd HH:mm:ss";
        }
        if(time == null){
            return null;
        }
        return new SimpleDateFormat(format).format(time);
    }
    public static String getTimeStr(Long time){
        if(time == null){
            return null;
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
    }

    public static Long getTime(String timeStr){
        if(timeStr == null || timeStr.trim().equals("")){
            return null;
        }
        if(timeStr.trim().matches("\\d+")){
            return Long.parseLong(timeStr.trim());
        }else if(timeStr.trim().length() == 10){
            try{
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date parse = simpleDateFormat.parse(timeStr);
                return parse.getTime();
            }catch(Exception e){
                throw new RuntimeException("日期格式不正确" + timeStr);
            }
        }else if(timeStr.trim().length() == 16){
            try{
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date parse = simpleDateFormat.parse(timeStr);
                return parse.getTime();
            }catch(Exception e){
                throw new RuntimeException("日期格式不正确" + timeStr);
            }
        }else{
            try{
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date parse = simpleDateFormat.parse(timeStr);
                return parse.getTime();
            }catch(Exception e){
                throw new RuntimeException("日期格式不正确" + timeStr);
            }
        }
    }

    @MyDescription("获取时间日, 05")
    public static String getDayStr(Long timestamp){
        int dayOfMonth = convert(timestamp).getDayOfMonth();
        String str = "0" + ("" + dayOfMonth);
        return str.substring(str.length() - 2);
    }

    @MyDescription("获取时间月")
    public static MonthNameEnum getMonth(Long timestamp){
        int monthValue = convert(timestamp).getMonthValue();
        return MonthNameEnum.parsing(monthValue);
    }

    @MyDescription("获取时间年, 24")
    public static String getYearStr(Long timestamp){
        int year = convert(timestamp).getYear();
        String str = "0" + ("" + year);
        return str.substring(str.length() - 2);
    }
}
