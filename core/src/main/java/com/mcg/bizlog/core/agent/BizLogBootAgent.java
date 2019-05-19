package com.mcg.bizlog.core.agent;

import com.mcg.bizlog.core.plugin.Plugin;
import com.mcg.bizlog.core.plugin.PluginDefine;
import com.mcg.bizlog.core.plugin.PluginManager;
import com.mcg.bizlog.core.plugin.PluginService;
import com.mcg.bizlog.core.plugin.loader.AgentClassLoader;
import com.mcg.bizlog.core.exception.AgentPackageNotFoundException;
import com.mcg.bizlog.core.inteceptor.AfterInteceptor;
import com.mcg.bizlog.core.inteceptor.AroundInteceptor;
import com.mcg.bizlog.core.inteceptor.BeforeInteceptor;
import javassist.*;

import java.util.List;

public class BizLogBootAgent {

    public static void start() throws AgentPackageNotFoundException {
        AgentClassLoader.initDefaultLoader();
        //加载插件
        List<PluginDefine> pluginDefineList=new PluginService().loadPlugins();

        //安装插件
        for(PluginDefine pluginDefine:pluginDefineList) {
            install(pluginDefine);
        }

        for(Plugin plugin: PluginManager.getPlugins()) {
             registerPlugin(plugin);
        }
    }

    private static void registerPlugin(Plugin plugin) {

        try {

            ClassPool pool = ClassPool.getDefault();
            pool.insertClassPath(new ClassClassPath(Plugin.class));
            String clazz = plugin.getEnhanceClass().replace("/", ".");
            if (null == pool.getOrNull(clazz)) {
                System.out.println(plugin.getClass().getName());
                return;
            }
            CtClass ctclass = pool.get(clazz);
            List<BeforeInteceptor> beforeInteceptors = plugin.getBeforeInterceptor();
            List<AfterInteceptor> afterInteceptors = plugin.getAfterInterceptor();
            List<AroundInteceptor> aroundInteceptors = plugin.getAroundInteceptor();

                String method = plugin.getEnhanceMethod();
                CtMethod ctMethod = ctclass.getDeclaredMethod(method);
                ctMethod.setModifiers(Modifier.PUBLIC);
                for (BeforeInteceptor beforeInteceptor : beforeInteceptors) {
                    ctMethod.insertBefore(beforeInteceptor.getBeforeMethod());
                }

                for (AfterInteceptor afterInteceptor : afterInteceptors) {
                    ctMethod.insertAfter(afterInteceptor.getAfterMethod());

                }
                for (AroundInteceptor aroundInteceptor : aroundInteceptors) {

                    ctMethod.setBody(aroundInteceptor.getAroundMethod());

                }


                ctclass.toClass();

            }
            catch (Throwable var5) {
                System.out.println(var5.getMessage());
            }


        }


    private static void install(PluginDefine pluginDefine)  {
        try {
            String pluginClass = pluginDefine.getDefineClass();
            Class pluginClazz=Class.forName(pluginClass, true, AgentClassLoader.getDefault());
            Plugin plugin=(Plugin)pluginClazz.newInstance();
            plugin.setName(pluginDefine.getName());
            PluginManager.addPlugin(plugin);
        }
        catch (Exception e) {

        }

    }
}
