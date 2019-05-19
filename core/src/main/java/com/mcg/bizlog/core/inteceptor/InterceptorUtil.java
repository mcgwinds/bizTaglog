package com.mcg.bizlog.core.inteceptor;

import com.mcg.bizlog.core.annotation.Around;
import com.mcg.bizlog.core.annotation.Before;
import com.mcg.bizlog.core.plugin.loader.AgentClassLoader;
import com.mcg.bizlog.core.annotation.After;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mcg
 */
public class InterceptorUtil {

    public static List<BeforeInteceptor> getBeforeInpterceptor(String interceptorClass) throws ClassNotFoundException {
        Class interceptorClazz=Class.forName(interceptorClass);
        List<BeforeInteceptor> inteceptors=new ArrayList<BeforeInteceptor>();
        Method[] var1 = interceptorClazz.getDeclaredMethods();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Method method = var1[var3];
            Annotation[] var5 = method.getDeclaredAnnotations();
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                Annotation annotation = var5[var7];
                if (annotation instanceof Before) {
                    BeforeInteceptor beforeInteceptor=new BeforeInteceptor();
                    beforeInteceptor.setBeforeMethod(interceptorClazz.getName() + "." + method.getName());
                    beforeInteceptor.setName(((Before) annotation).value());
                    inteceptors.add(beforeInteceptor);
                }
            }
        }
        return inteceptors;

    }

    public static BeforeInteceptor getBeforeInpterceptor(String interceptorClass,String name) throws ClassNotFoundException {
        Class interceptorClazz=Class.forName(interceptorClass,true, AgentClassLoader.getDefault());
        List<BeforeInteceptor> inteceptors=new ArrayList<BeforeInteceptor>();
        Method[] var1 = interceptorClazz.getDeclaredMethods();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Method method = var1[var3];
            Annotation[] var5 = method.getDeclaredAnnotations();
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                Annotation annotation = var5[var7];
                if (annotation instanceof Before&&name.equalsIgnoreCase(((Before) annotation).value())) {
                    BeforeInteceptor beforeInteceptor=new BeforeInteceptor();
                    beforeInteceptor.setBeforeMethod(interceptorClazz.getName() + "." + method.getName());
                    inteceptors.add(beforeInteceptor);
                }
            }
        }
        return inteceptors.get(0);

    }

    public static AfterInteceptor getAfterInpterceptor(String interceptorClass,String name) throws ClassNotFoundException {
        Class interceptorClazz=Class.forName(interceptorClass,true,AgentClassLoader.getDefault());
        List<AfterInteceptor> inteceptors=new ArrayList<AfterInteceptor>();
        Method[] var1 = interceptorClazz.getDeclaredMethods();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Method method = var1[var3];
            Annotation[] var5 = method.getDeclaredAnnotations();
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                Annotation annotation = var5[var7];
                if (annotation instanceof After&&name.equalsIgnoreCase(((After) annotation).value())) {
                    AfterInteceptor afterInteceptor=new AfterInteceptor();
                    afterInteceptor.setAfterMethod(interceptorClazz.getName() + "." + method.getName());
                    afterInteceptor.setName(((After) annotation).value());
                    inteceptors.add(afterInteceptor);
                }
            }
        }
        return inteceptors.get(0);

    }

    public static AroundInteceptor getAroundInpterceptor(String inteceptorClass, String name) throws ClassNotFoundException {
        Class interceptorClazz=Class.forName(inteceptorClass,true,AgentClassLoader.getDefault());
        List<AroundInteceptor> inteceptors=new ArrayList<AroundInteceptor>();
        Method[] var1 = interceptorClazz.getDeclaredMethods();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Method method = var1[var3];
            Annotation[] var5 = method.getDeclaredAnnotations();
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                Annotation annotation = var5[var7];
                if (annotation instanceof Around &&name.equalsIgnoreCase(((Around) annotation).value())) {
                    AroundInteceptor aroundInteceptor=new AroundInteceptor();
                    aroundInteceptor.setAroundMethod(interceptorClazz.getName() + "." + method.getName());
                    aroundInteceptor.setName(((Around) annotation).value());
                    inteceptors.add(aroundInteceptor);
                }
            }
        }
        return inteceptors.get(0);
    }
}
