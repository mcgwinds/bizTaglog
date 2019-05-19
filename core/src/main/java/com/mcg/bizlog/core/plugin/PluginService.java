package com.mcg.bizlog.core.plugin;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PluginService {

    public List<PluginDefine> loadPlugins() {
        PluginResourcesResolver resolver = new PluginResourcesResolver();
        List<URL> resources = resolver.getResources();

        if (resources == null || resources.size() == 0) {

            return new ArrayList<PluginDefine>();
        }

        for (URL pluginUrl : resources) {
            try {
                PluginCfg.INSTANCE.load(pluginUrl.openStream());
            } catch (Throwable t) {

            }
        }

        List<PluginDefine> pluginClassList = PluginCfg.INSTANCE.getPluginClassList();

        return pluginClassList;

    }
}
