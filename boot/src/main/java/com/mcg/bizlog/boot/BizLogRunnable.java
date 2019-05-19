package com.mcg.bizlog.boot;

import java.lang.reflect.Method;

public class BizLogRunnable implements Runnable {

    private ClassLoader classLoader;

    private String[] args;

    private Class<?> mainClazz;

    public BizLogRunnable(ClassLoader classLoader, String[] args, Class<?> mainClazz) {
        this.classLoader = classLoader;
        this.args = args;
        this.mainClazz = mainClazz;
    }

    public void run() {
        try {
            Class<?> mainClass = classLoader.loadClass(mainClazz.getName());
            Method mainMethod = mainClass.getDeclaredMethod("main", String[].class);
            mainMethod.invoke(null, new Object[]{args});
        } catch (Exception e) {
            throw new BizLogRuntimeException(e);
        }
    }
}
