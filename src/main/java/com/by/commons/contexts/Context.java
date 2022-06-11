package com.by.commons.contexts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * New version of Context
 * @author by.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Context {
    /**
     * user no
     */
    private String userNo;
    /**
     * user right symbol
     */
    private String admin;
    /**
     * ip address
     */
    private String ipAddress;
}
