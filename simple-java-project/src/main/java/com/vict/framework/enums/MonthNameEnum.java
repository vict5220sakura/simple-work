package com.vict.framework.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MonthNameEnum implements IEnum<String> {
    January("JAN", "一月"),
    February("FEB", "二月"),
    March("MAR", "三月"),
    April("APR", "四月"),
    May("MAY", "五月"),
    June("JUN", "六月"),
    Jun("JUL", "七月"),
    August("AUG", "八月"),
    September("SEP", "九月"),
    October("OCT", "十月"),
    November("NOV", "十一月"),
    December("DEC", "十二月"),
    ;
    private String value;
    private String name;

    public static MonthNameEnum parsing(int i){
        if (i == 1){
            return MonthNameEnum.January;
        }else if (i == 2){
            return MonthNameEnum.February;
        }else if (i == 3){
            return MonthNameEnum.March;
        }else if (i == 4){
            return MonthNameEnum.April;
        }else if (i == 5){
            return MonthNameEnum.May;
        }else if (i == 6){
            return MonthNameEnum.June;
        }else if (i == 7){
            return MonthNameEnum.Jun;
        }else if (i == 8){
            return MonthNameEnum.August;
        }else if (i== 9){
            return MonthNameEnum.September;
        }else if (i == 10){
            return MonthNameEnum.October;
        }else if (i == 11){
            return MonthNameEnum.November;
        }else if (i == 12){
            return MonthNameEnum.December;
        }else{
            return null;
        }
    }

    public static int getMonth(MonthNameEnum month){
        return month.ordinal() + 1;
    }
}
