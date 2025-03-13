package com.vict.utils;


import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @version 1.4
 * 添加set方法
 * 增加空指针判断
 */
public class PatternUtil {

    public static boolean canFind(String pattern, String context){
        if(context == null || context.trim().equals("")){
            return false;
        }
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(context);
        boolean b = matcher.find();
        return b;
    }

    @Data
    public static class MatchItem{
        private String str;
        public MatchItem matcher(String pattern){
            MatchItem matcher = PatternUtil.matcher(pattern, this.str);
            return matcher;
        }
        public MatchItem replaceAll(String pattern, String replacement){
            if(str == null){
                return this;
            }
            String s = this.str.replaceAll(pattern, replacement);
            MatchItem matchItem = new MatchItem();
            matchItem.setStr(s);
            return matchItem;
        }
    }

    public static MatchItem matcher(String pattern, String context){
        if(context == null || context.trim().equals("")){
            MatchItem matchItem = new MatchItem();
            return matchItem;
        }
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(context);
        boolean b = matcher.find();
        if(b){
            String group = matcher.group();

            MatchItem matchItem = new MatchItem();
            matchItem.setStr(group);
            return matchItem;
        }else{
            MatchItem matchItem = new MatchItem();
            return matchItem;
        }
    }

    public static boolean check(String pattern, String context){
        if(context == null || context.trim().equals("")){
            return false;
        }
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(context);
        return matcher.find();
    }

    public static String matcherOne(String pattern, String context){
        if(context == null || context.trim().equals("")){
            return null;
        }
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(context);
        boolean b = matcher.find();
        if(b){
            String group = matcher.group();
            return group;
        }else{
            return null;
        }
    }

    public static List<String> matcherAll(String pattern, String context){
        if(context == null || context.trim().equals("")){
            return null;
        }
        ArrayList<String> strings = new ArrayList<>();

        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(context);
        while(true){
            boolean b = matcher.find();
            if(b){
                String group = matcher.group();
                strings.add(group);
            }else{
                return strings;
            }
        }
    }

    public static Set<String> matcherAllSet(String pattern, String context){
        if(context == null || context.trim().equals("")){
            return null;
        }
        HashSet<String> strings = new HashSet<>();

        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(context);
        while(true){
            boolean b = matcher.find();
            if(b){
                String group = matcher.group();
                strings.add(group);
            }else{
                return strings;
            }
        }
    }

}
