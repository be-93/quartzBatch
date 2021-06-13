package com.quartz.batch.spring.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.quartz.batch.basic.repository"}
        , entityManagerFactoryRef = "basicEntityManagerFactory"
        , transactionManagerRef = "basicTransactionManager"
)
public class BasicDataSourceConfig {

    @Primary
    @Bean(name = "basicDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.basic")
    @Qualifier("basicEntityManagerFactory")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "basicEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("basicDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.ensys.batch.basic.model")
                .persistenceUnit("basic")
                .build();
    }

    @Primary
    @Bean(name = "basicTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("basicEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
