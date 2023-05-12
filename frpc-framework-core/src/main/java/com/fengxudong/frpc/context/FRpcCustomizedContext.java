package com.fengxudong.frpc.context;

import java.util.jar.Attributes;

/**
 * @author feng xud
 */
public class FRpcCustomizedContext extends FRpcContext{

    private Attributes customizedAttrs;

    public FRpcCustomizedContext() {
        this.customizedAttrs = new Attributes();
    }

    public Object get(String key){
        return customizedAttrs.get(key);
    }

    public FRpcContext set(String key,Object value){
        customizedAttrs.put(key,value);
        return this;
    }

    public <T> T get(Class<T> type){
        return (T) customizedAttrs.get(type);
    }

    public <T> FRpcContext set(Class<T> type,T value){
        customizedAttrs.put(type,value);
        return this;
    }
}
