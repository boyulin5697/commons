package com.by.commons.contexts;

/**
 * 上下文工具类
 * @author by.
 */
public class ContextLocal {
    private static final ThreadLocal<Context> CONTEXT_LOCAL = new ThreadLocal<>();
    private ContextLocal(){}
    public static Context getContext(){
        return CONTEXT_LOCAL.get();
    }
    public static void setContext(Context context){
        CONTEXT_LOCAL.set(context);
    }
    public static void remove(){
        CONTEXT_LOCAL.remove();
    }
}
