package com.mcg.bizlog.core.logback;

import org.slf4j.MDC;

public class LogContext implements Context {

    private static final ThreadLocal<LogContext> local = new ThreadLocal();

    public static LogContext context() {
        return (LogContext)local.get();
    }

    public static void remove() {
        local.remove();
    }

    public static void setLogContext(LogContext logContext) {
        local.set(logContext);
    }

    @Override
    public void addProperty(String var1, String var2) {
        MDC.put(var1, var2);
    }

    @Override
    public String getProperty(String var1) {
        return MDC.get(var1);
    }


}
