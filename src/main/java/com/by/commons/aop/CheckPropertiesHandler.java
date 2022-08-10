package com.by.commons.aop;

import com.by.commons.annotations.CheckProperties;
import com.by.commons.communication.StandardResp;
import com.by.commons.consts.ResponseCodeEnum;
import com.by.commons.consts.UserRightLevel;
import com.by.commons.contexts.Context;
import com.by.commons.contexts.ContextLocal;
import com.by.commons.tools.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;


/**
 * AOP annotation handler
 *
 * @author by.
 * @date 2022/4/28
 */
@Aspect
@Component
@Slf4j
public class CheckPropertiesHandler {

    @Value("${commons.checkproperties.debug}")
    private boolean isDebugMode;


    @Pointcut(value = "@annotation(com.by.commons.annotations.CheckProperties)")
    public void checkPropertiesPointcut(){

    }

    @Around("checkPropertiesPointcut() && @annotation(checkProperties)")
    public Object interceptor(ProceedingJoinPoint joinPoint, CheckProperties checkProperties)throws Throwable{
        Object[] args = joinPoint.getArgs();
        Object obj = null;
        StringBuffer buffer = new StringBuffer();
        String userId = null;
        String right = null;
        String auth = null;
        String ip = null;
        for(Object object:args){
            if(object instanceof HttpServletRequest){
                HttpServletRequest httpServletRequest = (HttpServletRequest) object;
                userId = httpServletRequest.getHeader("userNo");
                right = httpServletRequest.getHeader("authLevel");
                auth = httpServletRequest.getHeader("Auth");
                ip = IpUtils.getIpAddress(httpServletRequest);
            }
        }
        if(!checkProperties.debug()||!isDebugMode){
            if(auth==null||!auth.equals("Authed!")){
                return new StandardResp<>().error(ResponseCodeEnum.NO_AUTH,"No access outside debug mode!");
            }
        }
        try{
            if(checkRightHandle(right,checkProperties.checkRight())){
                assignToContext(userId, right);
            }else {
                return new StandardResp<>().error(ResponseCodeEnum.NO_AUTH, "Don't have rights to assess!");
            }
        }catch (Exception e){
            return new StandardResp<>().error(ResponseCodeEnum.INTERNAL_ERROR,e.getMessage());
        }

        if(checkProperties.checkIp()){

        }
        obj = joinPoint.proceed(args);
        return obj;
    }

    @AfterReturning(value = "checkPropertiesPointcut()")
    public void checkPropertiesAfterWard(){
        ContextLocal.remove();
    }

    /**
     * check user entry right.
     * @param rightLevel
     * @param requireLevel
     * @return
     * @throws Exception
     */
    private boolean checkRightHandle(String rightLevel,String requireLevel)throws Exception{
        Assert.isTrue(UserRightLevel.map.containsKey(rightLevel),"This user right level is illegal!");
        int rl = UserRightLevel.map.get(rightLevel);
        int rql = UserRightLevel.map.get(requireLevel);
        return rl >= rql;
    }

    /**
     * check IP
     * @return
     * @throws Exception
     */
    private boolean checkIpHandle()throws Exception{
        //todo:fulfill this logic

        return true;
    }

    /**
     * assign context to thread
     * @param userId
     * @param rightLevel
     */
    private void assignToContext(String userId, String rightLevel){
        Context context = new Context();
        context.setUserNo(userId);
        context.setAdmin(rightLevel);
        ContextLocal.setContext(context);
    }
}
