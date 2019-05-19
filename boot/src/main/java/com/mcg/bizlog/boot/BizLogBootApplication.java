package com.mcg.bizlog.boot;

import com.sun.tools.attach.VirtualMachine;
import com.mcg.bizlog.core.agent.BizLogBootAgent;
import com.mcg.bizlog.core.agent.TtlAgent;
import com.mcg.bizlog.core.exception.AgentPackageNotFoundException;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class BizLogBootApplication {

    private volatile static boolean  enable=false;



    public static void run(String[] args) throws AgentPackageNotFoundException, ClassNotFoundException {


        if(enable) {
            return;
        }
        //打印banner
        BizLogBanner banner = new BizLogBanner();
        banner.printBanner();

        ClassLoader classLoader= BizLogApplication.class.getClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);
        BizLogBootAgent.start();
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        String pid=runtimeMXBean.getName().split("@")[0];
        String ttlagentpath= TtlAgent.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if(ttlagentpath.length()>0) {
            attachAgent(pid, ttlagentpath);
        }

        enable=true;

    }

    private static boolean attachAgent(String pid,String agentpath)  {
        try {
            VirtualMachine vm = VirtualMachine.attach(pid);
            vm.loadAgent(agentpath);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

}
