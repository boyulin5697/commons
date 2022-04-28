package com.by.commons.annotations;

import java.lang.annotation.*;

/**
 * This annotation is used to mark for checking whether the request to an interface is illegal by
 * verifying existence of some required params.
 * These params could be updated via future updates.
 *
 * If there is no requirement for identity checking in the interfaces, don't use this!
 *
 *
 * @author by.
 * @date 2022/4/28
 * @since 1.0.0
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CheckProperties {
    //Restrict to assess right, default admin level
    String checkRight() default "admin";
    //Check whether checkIp is useful
    boolean checkIp() default false;
}
