package com.mcg.bizlog.core.agent;

import com.mcg.bizlog.core.plugin.Plugin;
import com.mcg.bizlog.core.plugin.PluginDefine;
import com.mcg.bizlog.core.plugin.PluginManager;
import com.mcg.bizlog.core.plugin.PluginService;
import com.mcg.bizlog.core.plugin.loader.AgentClassLoader;
import com.mcg.bizlog.core.exception.AgentPackageNotFoundException;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.List;


/**
 * @author mcg
 */
public class LogAgent {

    public static void premain(String agentArgs, Instrumentation instrumentation) throws AgentPackageNotFoundException, IOException {

        agentmain(agentArgs,instrumentation);
    }


    public static void agentmain(String agentArgs, Instrumentation instrumentation) throws AgentPackageNotFoundException, IOException {

        AgentClassLoader.initDefaultLoader();
        //加载插件
        List<PluginDefine> pluginDefineList=new PluginService().loadPlugins();

        //安装插件
        for(PluginDefine pluginDefine:pluginDefineList) {
                install(pluginDefine);
        }
        //注册插件
        Transformer transformer=new Transformer(agentArgs,instrumentation);
        for(Plugin plugin: PluginManager.getPlugins()) {
            transformer.registerPlugin(plugin);
        }
        //TtlAgent.premain(agentArgs,instrumentation);


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
