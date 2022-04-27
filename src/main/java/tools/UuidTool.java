package tools;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * uuid生成工具
 */
public  class UuidTool {

    public static String getUUID(){
        return StringUtils.replace(UUID.randomUUID().toString(), "-", "");
    }

}
