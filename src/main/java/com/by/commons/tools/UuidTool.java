package com.by.commons.tools;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class UuidTool {

    public static String getUUID(){
        return StringUtils.replace(UUID.randomUUID().toString(), "-", "");
    }

}
