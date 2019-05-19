package com.mcg.bizlog.core.logback;

public interface Context {

    void addProperty(String var1, String var2);

    String getProperty(String var1);
}
