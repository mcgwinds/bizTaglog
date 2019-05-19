package com.mcg.bizlog.core.exception;

/**
 * @author mcg
 */
public class IllegalPluginDefineException extends Exception {
    public IllegalPluginDefineException(String define) {
        super("Illegal plugin define : " + define);
    }
}

