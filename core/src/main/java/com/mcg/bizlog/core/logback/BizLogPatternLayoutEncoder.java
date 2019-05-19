package com.mcg.bizlog.core.logback;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;

/**
 * @author mcg
 */
public class BizLogPatternLayoutEncoder extends PatternLayoutEncoder {

    public BizLogPatternLayoutEncoder() {

    }

    @Override
    public void start() {
        BizLogPatternLayout patternLayout = new BizLogPatternLayout();
        patternLayout.setContext(this.context);
        patternLayout.setPattern(this.getPattern());
        patternLayout.setOutputPatternAsHeader(this.outputPatternAsHeader);
        patternLayout.start();
        this.layout = patternLayout;
        super.start();
    }
}
