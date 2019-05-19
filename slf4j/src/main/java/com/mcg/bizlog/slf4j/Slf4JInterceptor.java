package com.mcg.bizlog.slf4j;

import com.mcg.bizlog.core.annotation.Around;
import org.slf4j.spi.MDCAdapter;

public class Slf4JInterceptor {


    @Around("getMDCA")
    public static MDCAdapter getMDCA() {
        return new TtlMDCAdapter();

    }
}
