package com.mcg.bizlog.core.inteceptor;

/**
 * @author mcg
 */
public class AroundInteceptor implements Inteceptor {

    private String name;

    private String aroundMethod;


    public String getAroundMethod() {
        return aroundMethod;
    }

    public void setAroundMethod(String aroundMethod) {
        this.aroundMethod = aroundMethod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
