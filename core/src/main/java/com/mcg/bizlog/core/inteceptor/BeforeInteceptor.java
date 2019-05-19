package com.mcg.bizlog.core.inteceptor;

/**
 * @author mcg
 */
public class BeforeInteceptor implements Inteceptor {


   private String beforeMethod;

   private String name;


   public String getBeforeMethod() {
      return beforeMethod;
   }

   public void setBeforeMethod(String beforeMethod) {
      this.beforeMethod = beforeMethod;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }
}
