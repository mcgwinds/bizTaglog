package com.mcg.bizlog.dubbo;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mcg.bizlog.core.logback.BizLogKeyUtil;
import com.mcg.bizlog.core.logback.LogContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Activate(
        group = {"provider"}

)
public class BizLogFilter implements Filter {


    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            before(invocation);
        }
        catch (Exception e) {
            System.out.println(e.getCause());
            //不影响业务
        }
        Result result = null;
        try {
            result = invoker.invoke(invocation);
        } catch (RuntimeException var9) {
            throw var9;
        } finally {
            this.after(result, invocation);
        }
        return result;
    }




    private void bingParams(Invocation invocation, Set<String> logParamKeys) {
        LogContext logContext= getLogContext(invocation);
        LogContext.setLogContext(logContext);
        Object[] params = invocation.getArguments();
        Class[] types= invocation.getParameterTypes();
        if(params==null||params.length==0) {
            return;
        }
        for(int i = 0; i < params.length; ++i) {
            if(params[i]==null) {
                continue;
            }
            Map<String,Object> map=null;
            Object obj = JSON.parse(JSON.toJSONString(params[i]));

            if(obj instanceof Map) {
                map=(JSONObject) JSON.toJSON(obj);
            }

            else if(obj instanceof JSONObject) {
                map=(JSONObject)obj;
            }
            else {
                return;
            }
            Map<String,Object> newMap=new HashMap<String,Object>();
            Set<String> keySets=map.keySet();
            for(String key:keySets) {
                Object value= map.get(key);
                newMap.put(key.toLowerCase(),value);

            }

            for(String logParamKey:logParamKeys) {
                String value= JSON.toJSONString(newMap.get(logParamKey.toLowerCase()));
                logContext.addProperty(logParamKey,value);

            }
            }
        }


    private void bindAttachments(Invocation invocation,Set<String> logParamKeys) {
        LogContext logContext= getLogContext(invocation);
        LogContext.setLogContext(logContext);
        if(invocation==null) {
            return;
        }
        Map<String, String> params = invocation.getAttachments();
        if(params==null) {
            return;
        }
        Set<String> keySets=params.keySet();
        Map<String,String> newMap=new HashMap<String,String>();
        for(String key:keySets) {
           String value= JSON.toJSONString(params.get(key));
           newMap.put(key.toLowerCase(),value);

        }
        for(String logParamKey:logParamKeys) {
            String value=newMap.get(logParamKey.toLowerCase());
            if(StringUtils.isNotEmpty(value)) {
                logContext.addProperty(logParamKey, value);
            }

        }

    }


    protected LogContext getLogContext(Invocation invocation) {
        if (!(invocation instanceof RpcInvocation)) {
            return null;
        } else {
            return LogContext.context() == null ? new LogContext() : LogContext.context();
        }
    }


    private void before(Invocation invocation) {
        Set<String> logParamKeys= BizLogKeyUtil.getKeys();
        bingParams(invocation,logParamKeys);
        bindAttachments(invocation,logParamKeys);

    }

    private void after(Result result,Invocation invocation) {
        LogContext.remove();
    }


}
