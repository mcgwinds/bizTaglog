package com.mcg.bizlog.boot;

public class BizLogRuntimeException extends RuntimeException {

    public BizLogRuntimeException(String message) {
        super(message);
    }

    public BizLogRuntimeException(String message, Exception e) {
        super(message,e);
    }

    public BizLogRuntimeException(Exception e) {
        super(e);
    }
}
