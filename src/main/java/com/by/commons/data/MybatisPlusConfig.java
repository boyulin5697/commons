package com.by.commons.data;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //Add mybatis plus config
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        //Add oracle interceptor support
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.ORACLE));
        return interceptor;
    }

}
