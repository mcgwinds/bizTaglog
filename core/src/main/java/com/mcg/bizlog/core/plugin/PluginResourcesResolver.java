package com.mcg.bizlog.core.plugin;

import com.mcg.bizlog.core.plugin.loader.AgentClassLoader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class PluginResourcesResolver {

    public List<URL> getResources() {
        List<URL> cfgUrlPaths = new ArrayList<URL>();
        Enumeration<URL> urls;
        try {
            urls = AgentClassLoader.getDefault().getResources("bizlog-plugin.def");
            while (urls.hasMoreElements()) {
                    URL pluginUrl = urls.nextElement();
                    cfgUrlPaths.add(pluginUrl);
                }


            return cfgUrlPaths;
        } catch (IOException e) {
        }
        return null;
    }
}
