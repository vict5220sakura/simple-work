package com.vict.utils;

import java.util.HashMap;
import java.util.Map;

public class ThreadUtil {
    private static final ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();

    public ThreadUtil() {
    }

    public static void putThreadVariable(String key, Object obj) {
        Map<String, Object> vm = threadLocal.get();
        if (vm == null) {
            vm = new HashMap<>();
            threadLocal.set(vm);
        }

        vm.put(key, obj);
    }

    public static Object getThreadVariable(String key) {
        Map<String, Object> vm = threadLocal.get();
        if (vm == null) {
            return null;
        } else {
            return vm.get(key);
        }
    }

    public static Object removeThreadVariable(String key) {
        Map<String, Object> vm = threadLocal.get();
        if (vm == null) {
            return null;
        } else {
            return vm.remove(key);
        }
    }

    public static void clearThreadVariable() {
        Map<String, Object> vm = threadLocal.get();
        if (vm != null) {
            vm.clear();
        }
    }
}
