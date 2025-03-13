package com.vict.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {
    /**
     *
     * @param list 数组
     * @param size 切分大小
     * @param <T> 类型
     * @return
     */
    public static <T> List<List<T>> groupByNum(List<T> list, int size){
        if(list == null){
            return null;
        }
        ArrayList<List<T>> lists = new ArrayList<>();
        if(list.size() == 0){
            lists.add(list);
            return lists;
        }
        if(size <= 0){
            lists.add(list);
            return lists;
        }
        for(int i = 0 ; i < list.size() ; i += size){
            List<T> subList = list.subList(i, i + size > list.size() ? list.size() : i + size);
            lists.add(subList);
        }
        return lists;
    }

    /***
     * 根据ascii码排序
     * @param list
     * @return
     */
    public static List<String> sortASCII(List<String> list){
        list.sort((o1, o2)->{
            if(o1.compareTo(o2) > 0){
                return 1;
            }else if(o1.compareTo(o2) < 0){
                return -1;
            }else{
                return 0;
            }
        });
        return list;
    }

}
