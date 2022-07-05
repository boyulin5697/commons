package com.by.commons.apis;

/**
 * Api request standard
 *
 * @author by.
 * @date 2022/6/3
 * @since 1.0.2
 */
public interface ApiRequest {
    /**
     * All frontend requests most implement this method to achieve param check operation.
     */
    void verify();
}
