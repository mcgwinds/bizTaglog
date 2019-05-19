package com.mcg.bizlog.core.inteceptor;

/**
 * @author mcg
 */
public class AfterInteceptor implements Inteceptor {

    private String name;

    private String afterMethod;

    public String getAfterMethod() {
        return afterMethod;
    }

    public void setAfterMethod(String afterMethod) {
        this.afterMethod = afterMethod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
