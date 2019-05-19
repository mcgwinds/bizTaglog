package com.mcg.bizlog.core.agent;

import com.alibaba.ttl.threadpool.agent.internal.logging.Logger;
import com.alibaba.ttl.threadpool.agent.internal.transformlet.JavassistTransformlet;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

public class TtlTransformer implements ClassFileTransformer {
    private static final Logger logger = Logger.getLogger(com.alibaba.ttl.threadpool.agent.TtlTransformer.class);
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    private final List<JavassistTransformlet> transformletList = new ArrayList();

    TtlTransformer(List<? extends JavassistTransformlet> transformletList) {
        Iterator var2 = transformletList.iterator();

        while(var2.hasNext()) {
            JavassistTransformlet transformlet = (JavassistTransformlet)var2.next();
            this.transformletList.add(transformlet);
            logger.info("[TtlTransformer] add Transformlet " + transformlet.getClass() + " success");
        }

    }

    @Override
    public final byte[] transform( ClassLoader loader,  String classFile, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classFileBuffer) {
        try {
            if (classFile == null) {
                return EMPTY_BYTE_ARRAY;
            }

            String className = toClassName(classFile);
            Iterator var11 = this.transformletList.iterator();

            while(var11.hasNext()) {
                JavassistTransformlet transformlet = (JavassistTransformlet)var11.next();
                byte[] bytes = transformlet.doTransform(className, classFileBuffer, loader);
                if (bytes != null) {
                    return bytes;
                }
            }
        } catch (Throwable var10) {
            String msg = "Fail to transform class " + classFile + ", cause: " + var10.toString();
            logger.log(Level.SEVERE, msg, var10);
            throw new IllegalStateException(msg, var10);
        }

        return EMPTY_BYTE_ARRAY;
    }

    private static String toClassName(String classFile) {
        return classFile.replace('/', '.');
    }
}
