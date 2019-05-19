package com.mcg.bizlog.core.plugin;


import com.mcg.bizlog.core.inteceptor.AfterInteceptor;
import com.mcg.bizlog.core.inteceptor.AroundInteceptor;
import com.mcg.bizlog.core.inteceptor.BeforeInteceptor;

import java.util.List;

public abstract class Plugin {

    private String name;

    public abstract String getEnhanceClass();

    public abstract  String getEnhanceMethod();

    public abstract List<BeforeInteceptor> getBeforeInterceptor();

    public abstract List<AfterInteceptor> getAfterInterceptor();

    public abstract List<AroundInteceptor> getAroundInteceptor();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}


