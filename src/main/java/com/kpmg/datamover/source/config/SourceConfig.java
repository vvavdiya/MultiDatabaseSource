package com.kpmg.datamover.source.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.net.URISyntaxException;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "sourceEntityManagerFactory",
        transactionManagerRef = "sourceTransactionManager",
        basePackages = {
                "com.kpmg.datamover.source"
        }
)
public class SourceConfig {

    private final SourceSettings sourceSettings;

    public SourceConfig(SourceSettings sourceSettings) {
        this.sourceSettings = sourceSettings;
    }

    @Primary
    @Bean(name = "sourceDataSource")
    public DataSource sourceDataSource() {
        return DataSourceBuilder.create()
                .driverClassName(sourceSettings.getDriverClassname())
                .url(sourceSettings.getUrl())
                .username(sourceSettings.getUsername())
                .password(sourceSettings.getPassword()).build();
    }

    @Primary
    @Bean(name = "sourceEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("sourceDataSource") DataSource dataSource
    ) throws URISyntaxException {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan("com.kpmg.datamover.source");
        entityManagerFactoryBean.setPersistenceUnitName("sourcedb");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabasePlatform(sourceSettings.getDialect());

        //TODO change to false after testing
        vendorAdapter.setGenerateDdl(true);


        vendorAdapter.setShowSql(true);
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        return entityManagerFactoryBean;
    }

    @Primary
    @Bean(name = "sourceTransactionManager")
    public PlatformTransactionManager customerTransactionManager(
            @Qualifier("sourceEntityManagerFactory") EntityManagerFactory sourceEntityManagerFactory
    ) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager(sourceEntityManagerFactory);
        return jpaTransactionManager;
    }
}
