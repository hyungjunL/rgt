package com.rgt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import lombok.Data;

@ConfigurationProperties(prefix="mybatis")
@Data
public class MybatisSetting{

	private Resource configLocation;
	private String mapperLocations;
	private String driverClassName;
	private String username;
	private String password;
	private String url;
	private boolean testWhileIdle;
	private long timeBetweenEvictionRunsMillis;
	private String validationQuery;
	private int maxTotal;
	private int maxIdle;

}