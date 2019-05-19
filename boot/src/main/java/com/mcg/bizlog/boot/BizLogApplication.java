package com.mcg.bizlog.boot;

import com.alibaba.ttl.threadpool.agent.TtlAgent;
import com.sun.tools.attach.VirtualMachine;
import com.mcg.bizlog.core.agent.LogAgent;
import com.mcg.bizlog.core.exception.AgentPackageNotFoundException;
import sun.reflect.Reflection;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * @author mcg
 */
public class BizLogApplication {
        private volatile static boolean  enable=false;



        public static void run(String[] args) throws AgentPackageNotFoundException {


            if(enable) {
                return;
            }
            //打印banner
            BizLogBanner banner = new BizLogBanner();
            banner.printBanner();

            ClassLoader classLoader= BizLogApplication.class.getClassLoader();
            String agentpath=LogAgent.class.getProtectionDomain().getCodeSource().getLocation().getPath();

            String ttlagentpath= TtlAgent.class.getProtectionDomain().getCodeSource().getLocation().getPath();



            RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
            String pid=runtimeMXBean.getName().split("@")[0];

            if(agentpath.length()>0) {
                attachAgent(pid, agentpath);
            }
            if(ttlagentpath.length()>0) {
                attachAgent(pid, ttlagentpath);
            }

            try {
                enable=true;

                Class<?> mainClazz= Reflection.getCallerClass(2);
                Thread thread=new Thread(new BizLogRunnable(classLoader,args,mainClazz));
                thread.start();
                thread.join();
                System.exit(0);

            } catch (Exception e) {
                System.exit(1);
            }


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
