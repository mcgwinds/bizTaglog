package com.mcg.bizlog.core.agent;

import com.mcg.bizlog.core.inteceptor.AroundInteceptor;
import com.mcg.bizlog.core.plugin.Plugin;
import com.mcg.bizlog.core.inteceptor.AfterInteceptor;
import com.mcg.bizlog.core.inteceptor.BeforeInteceptor;
import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.List;

/**
 * @author mcg
 */
public class Transformer {

    private String agentArgs;

    private Instrumentation instrumentation;

    public Transformer(String agentArgs, Instrumentation instrumentation) {
        this.agentArgs=agentArgs;
        this.instrumentation=instrumentation;
    }

    public void registerPlugin(final Plugin plugin) {

        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

                String clazz=plugin.getEnhanceClass();
                String method=plugin.getEnhanceMethod();
                byte[] bytecode = classfileBuffer;

                if(className.equalsIgnoreCase(clazz)) {
                    List<BeforeInteceptor> beforeInteceptors=plugin.getBeforeInterceptor();
                    List<AfterInteceptor> afterInteceptors=plugin.getAfterInterceptor();
                    List<AroundInteceptor> aroundInteceptors=plugin.getAroundInteceptor();
                    ClassPool pool = ClassPool.getDefault();
                    pool.appendClassPath(new ClassClassPath(plugin.getClass()));
                    try {
                        CtClass ctclass = pool.get(className.replace("/","."));
                        CtMethod ctMethod=ctclass.getDeclaredMethod(method);
                        ctMethod.setModifiers(Modifier.PUBLIC);
                        for(BeforeInteceptor beforeInteceptor:beforeInteceptors) {
                             ctMethod.insertBefore(beforeInteceptor.getBeforeMethod());
                         }

                        for(AfterInteceptor afterInteceptor:afterInteceptors) {
                            ctMethod.insertAfter(afterInteceptor.getAfterMethod());

                        }
                        for(AroundInteceptor aroundInteceptor:aroundInteceptors) {

                            ctMethod.setBody(aroundInteceptor.getAroundMethod());

                        }

                        bytecode = ctclass.toBytecode();

                    }
                    catch (IOException e) {
                        throw new IllegalClassFormatException(e.getMessage());
                    }
                    catch (NotFoundException e) {
                        throw new IllegalClassFormatException(e.getMessage());

                    }
                    catch (CannotCompileException e) {
                        throw new IllegalClassFormatException(e.getMessage());

                    }

                }

                return bytecode;
            }
        });

    }

}
