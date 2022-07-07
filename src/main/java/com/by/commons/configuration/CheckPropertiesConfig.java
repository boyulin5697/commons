package com.by.commons.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Check properties config
 *
 * @author by.
 * @date 2022/7/7
 * @since 1.0.5
 */
@Component
@ConfigurationProperties("commons.checkproperties")
@Getter
@Setter
public class CheckPropertiesConfig {
    private boolean debug = false;
}
