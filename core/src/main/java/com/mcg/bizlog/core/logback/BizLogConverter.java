package com.mcg.bizlog.core.logback;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.util.OptionHelper;

/**
 * @author mcg
 */
public class BizLogConverter extends ClassicConverter {
    private String key;
    private String defaultValue = "";

    public BizLogConverter() {
    }

    @Override
    public void start() {
        String[] keyInfo = OptionHelper.extractDefaultReplacement(this.getFirstOption());
        this.key = keyInfo[0];
        BizLogKeyUtil.addKey(key);

        super.start();
    }

    @Override
    public void stop() {
        this.key = null;
        BizLogKeyUtil.remove(key);
        super.stop();
    }

    @Override
    public String convert(ILoggingEvent event) {
        String value = (String)event.getMDCPropertyMap().get(this.key);
        return value != null ? value : this.defaultValue;

    }
}

