package com.by.commons.consts;

import java.util.HashMap;
import java.util.Map;

/**
 * User Right Level
 *
 * @author by.
 * @date 2022/4/28
 */
public class UserRightLevel {
    /**
     * Right restrict map.
     */
    public static Map<String,Integer>map = new HashMap<>();
    /**
     * normal user
     */
    public static int NORMAL_USER = 0;
    /**
     * admin
     */
    public static int ADMIN = 1;

    static {
        map.put("common",NORMAL_USER);
        map.put("admin",ADMIN);
    }


}
