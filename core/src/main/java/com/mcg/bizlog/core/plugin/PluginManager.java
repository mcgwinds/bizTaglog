package com.mcg.bizlog.core.plugin;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PluginManager {

    private static List<Plugin> plugins=new CopyOnWriteArrayList<Plugin>();

    public static void addPlugin(Plugin plugin) {
        plugins.add(plugin);
    }

    public static void removePlugin(Plugin plugin) {
        plugins.remove(plugin);
    }

    public static List<Plugin> getPlugins() {
        return plugins;
    }
}
