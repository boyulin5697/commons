package contexts;

/**
 * 上下文工具类
 * @author by.
 */
public class ContextLocal {
    private static final ThreadLocal<Context> contextLocal = new ThreadLocal<>();
    private ContextLocal(){}
    public static Context getContext(){
        return contextLocal.get();
    }
    public static void setContext(Context context){
        contextLocal.set(context);
    }
    public static void remove(){
        contextLocal.remove();
    }
}
