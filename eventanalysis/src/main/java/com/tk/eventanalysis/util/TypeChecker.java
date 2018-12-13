package com.tk.eventanalysis.util;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component
public class TypeChecker {

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isOrNull(Object... objs) {
        if (TypeChecker.isNull(objs)) {
            return true;
        }

        for (Object obj : objs) {
            if (TypeChecker.isNull(obj)) {
                return true;
            }
        }
        return false;
    }
    public static boolean isEmpty(StringBuffer sb) {
        return TypeChecker.isNull(sb) || sb.length() == 0;

    }

    public static boolean isEmpty(Collection collection) {

        return isNull(collection) || collection.isEmpty();

    }

    public static boolean isEmpty(Map map) {

        return isNull(map) || map.isEmpty();

    }

    public static boolean isEmpty(String str) {

        return isNull(str) || str.trim().length() == 0 || "null".equalsIgnoreCase(str);
    }
    public static boolean isEmpty(CharSequence str) {

        return TypeChecker.isNull(str) || str.length() == 0;

    }
    public static boolean isEmpty(Object[] arrays) {
        return TypeChecker.isNull(arrays) || arrays.length == 0;

    }
}
