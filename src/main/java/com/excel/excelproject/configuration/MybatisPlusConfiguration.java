package com.excel.excelproject.configuration;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.excel.excelproject.dao")
public class MybatisPlusConfiguration {
}
