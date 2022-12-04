package com.side.global.configuration;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(
		basePackages = "com.side.domain.mapper"
)
public class DatabaseConfiguration {}