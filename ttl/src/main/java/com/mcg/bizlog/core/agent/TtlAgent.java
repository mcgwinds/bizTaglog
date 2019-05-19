package com.mcg.bizlog.core.agent;

import com.alibaba.ttl.threadpool.agent.internal.logging.Logger;
import com.alibaba.ttl.threadpool.agent.internal.transformlet.JavassistTransformlet;
import com.alibaba.ttl.threadpool.agent.internal.transformlet.impl.TtlExecutorTransformlet;
import com.alibaba.ttl.threadpool.agent.internal.transformlet.impl.TtlForkJoinTransformlet;
import com.alibaba.ttl.threadpool.agent.internal.transformlet.impl.TtlTimerTaskTransformlet;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class TtlAgent {

    private static volatile Map<String, String> kvs;
    private static volatile boolean ttlAgentLoaded = false;
    private static final String TTL_AGENT_ENABLE_TIMER_TASK_KEY = "ttl.agent.enable.timer.task";
    private static final String TTL_AGENT_DISABLE_INHERITABLE_FOR_THREAD_POOL = "ttl.agent.disable.inheritable.for.thread.pool";

    public static void premain(String agentArgs,  Instrumentation inst) {
        agentmain(agentArgs,inst);
    }
    public static void agentmain(String agentArgs,  Instrumentation inst) {
        kvs = splitCommaColonStringToKV(agentArgs);
        Logger.setLoggerImplType(getLogImplTypeFromAgentArgs(kvs));
        Logger logger = Logger.getLogger(TtlAgent.class);

        try {
            logger.info("[TtlAgent.agent] begin, agentArgs: " + agentArgs + ", Instrumentation: " + inst);
            boolean disableInheritable = isDisableInheritableForThreadPool();
            List<JavassistTransformlet> transformletList = new ArrayList();
            transformletList.add(new TtlExecutorTransformlet(disableInheritable));
            transformletList.add(new TtlForkJoinTransformlet(disableInheritable));
            if (isEnableTimerTask()) {
                transformletList.add(new TtlTimerTaskTransformlet());
            }

            ClassFileTransformer transformer = new TtlTransformer(transformletList);
            inst.addTransformer(transformer, true);
            logger.info("[TtlAgent.agent] addTransformer " + transformer.getClass() + " success");
            logger.info("[TtlAgent.agent] end");
            ttlAgentLoaded = true;
        } catch (Exception var6) {
            String msg = "Fail to load TtlAgent , cause: " + var6.toString();
            logger.log(Level.SEVERE, msg, var6);
            throw new IllegalStateException(msg, var6);
        }
    }

    private static String getLogImplTypeFromAgentArgs( Map<String, String> kvs) {
        return (String)kvs.get("ttl.agent.logger");
    }

    public static boolean isTtlAgentLoaded() {
        return ttlAgentLoaded;
    }

    public static boolean isDisableInheritableForThreadPool() {
        return isOptionSet(kvs, "ttl.agent.disable.inheritable.for.thread.pool");
    }

    public static boolean isEnableTimerTask() {
        return isOptionSet(kvs, "ttl.agent.enable.timer.task");
    }

    private static boolean isOptionSet( Map<String, String> kvs,  String key) {
        if (null == kvs) {
            return false;
        } else {
            boolean hasEnableKey = kvs.containsKey(key);
            if (!hasEnableKey) {
                return false;
            } else {
                return !"false".equalsIgnoreCase((String)kvs.get(key));
            }
        }
    }


    static Map<String, String> splitCommaColonStringToKV( String commaColonString) {
        Map<String, String> ret = new HashMap();
        if (commaColonString != null && commaColonString.trim().length() != 0) {
            String[] splitKvArray = commaColonString.trim().split("\\s*,\\s*");
            String[] var3 = splitKvArray;
            int var4 = splitKvArray.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String kvString = var3[var5];
                String[] kv = kvString.trim().split("\\s*:\\s*");
                if (kv.length != 0) {
                    if (kv.length == 1) {
                        ret.put(kv[0], "");
                    } else {
                        ret.put(kv[0], kv[1]);
                    }
                }
            }

            return ret;
        } else {
            return ret;
        }
    }

    private TtlAgent() {
        throw new InstantiationError("Must not instantiate this class");
    }
}
