package com.mcg.bizlog.slf4j;

import com.mcg.bizlog.core.inteceptor.AfterInteceptor;
import com.mcg.bizlog.core.inteceptor.AroundInteceptor;
import com.mcg.bizlog.core.inteceptor.BeforeInteceptor;
import com.mcg.bizlog.core.inteceptor.InterceptorUtil;
import com.mcg.bizlog.core.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class Slf4JPlugin extends Plugin {

    private final  String inteceptorClass="Slf4JInterceptor";

    private final  String enhanceClass="org/slf4j/impl/StaticMDCBinder";


    private final  String enhanceMethod="getMDCA";

    @Override
    public String getEnhanceClass() {
        return enhanceClass;
    }

    @Override
    public String getEnhanceMethod() {
        return enhanceMethod;
    }

    @Override
    public List<BeforeInteceptor> getBeforeInterceptor() {
       return new ArrayList<BeforeInteceptor>();
    }

    @Override
    public List<AfterInteceptor> getAfterInterceptor() {

        return new ArrayList<AfterInteceptor>();
    }

    @Override
    public List<AroundInteceptor> getAroundInteceptor() {
        List<AroundInteceptor> aroundInteceptors=new ArrayList<AroundInteceptor>();
        try {
            AroundInteceptor aroundInteceptor = InterceptorUtil.getAroundInpterceptor(inteceptorClass,"getMDCA");
            StringBuffer sb = new StringBuffer();
            sb.append("{ return ");
            sb.append(aroundInteceptor.getAroundMethod()).append("();");
            sb.append("}");
            aroundInteceptor.setAroundMethod(sb.toString());
            aroundInteceptors.add(aroundInteceptor);

        }catch (Exception e) {

        }
        return aroundInteceptors;
    }
}
