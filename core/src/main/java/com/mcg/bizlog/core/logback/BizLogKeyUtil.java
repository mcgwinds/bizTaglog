package com.mcg.bizlog.core.logback;

import java.util.HashSet;
import java.util.Set;

/**
 * @author mcg
 */
public class BizLogKeyUtil {

    private final static Set<String> keys=new HashSet<String>();

    public static void addKey(String key) {
        keys.add(key);
    }

    public static Set<String> getKeys() {
        return keys;
    }

    public static void remove(String key) {
        keys.remove(key);
    }
}
