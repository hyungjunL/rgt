package com.rgt.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@MapperScan(basePackages="com.rgt.repository")
public class MybatisConfig {


	@Autowired
	private MybatisSetting properties;

	@Autowired
	private DataSource dataSource;

	@Bean
	DataSource dataSource(){
		BasicDataSource ret = new BasicDataSource();
	
		try{
			ret.setDriverClassName(properties.getDriverClassName());
			ret.setUsername(properties.getUsername());
			ret.setPassword(properties.getPassword());
			ret.setUrl(properties.getUrl());
			ret.setTestWhileIdle(properties.isTestWhileIdle());
			ret.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRunsMillis());
			ret.setValidationQuery(properties.getValidationQuery());
			ret.setMaxTotal(properties.getMaxTotal());
			ret.setMaxIdle(properties.getMaxIdle());

		}
		catch(Exception e){
			log.debug(" ## MAPPERS : [{}]", properties.getMapperLocations());
			log.error(e.getMessage(), e);
		}

		return ret;
	}

	@Bean
	public SqlSessionFactoryBean sqlSessionFactoryForMyBatis(DataSource dataSource) throws IOException {
		log.debug(" ## MAPPERS : [{}]", properties.getMapperLocations());
		Resource[] mapperLocations = new PathMatchingResourcePatternResolver().getResources(properties.getMapperLocations());

		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setConfigLocation(properties.getConfigLocation());
		sqlSessionFactoryBean.setMapperLocations(mapperLocations);

		return sqlSessionFactoryBean;
	}

	@Bean
	public SqlSession sqlSessionForMyBatis(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
	
	@Bean
	public DataSourceTransactionManager transactionManager() {

		if(log.isDebugEnabled()){
			log.debug("> transactionManager");
		}

		return new DataSourceTransactionManager(dataSource);
	}

}
