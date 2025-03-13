package com.vict.framework.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {
    public static String getStackTrace(Throwable throwable) {
        // StringBuilder sb = new StringBuilder();
        // sb.append(throwable.toString());
        // for (StackTraceElement element : throwable.getStackTrace()) {
        //     sb.append("\n\tat ");
        //     sb.append(element);
        // }
        // return sb.toString();
        // 使用 StringWriter 将异常信息转换为字符串
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
