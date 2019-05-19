package com.mcg.bizlog.core.logback;

import ch.qos.logback.classic.PatternLayout;

public class BizLogPatternLayout extends PatternLayout {

    static {
        defaultConverterMap.put("BizLog", BizLogConverter.class.getName());
    }
}
