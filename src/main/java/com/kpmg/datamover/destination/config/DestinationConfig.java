package com.kpmg.datamover.destination.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        entityManagerFactoryRef = "destinationEntityManagerFactory",
        transactionManagerRef = "destinationTransactionManager",
        basePackages = {
                "com.kpmg.datamover.destination"
        }
)
public class DestinationConfig {

    private final DestinationSettings destinationSettings;

    public DestinationConfig(DestinationSettings destinationSettings) {
        this.destinationSettings = destinationSettings;
    }

    @Bean(name = "destinationDataSource")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName(destinationSettings.getDriverClassname())
                .url(destinationSettings.getUrl())
                .username(destinationSettings.getUsername())
                .password(destinationSettings.getPassword()).build();
    }


    @Bean(name = "destinationEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("destinationDataSource") DataSource dataSource
    ) throws URISyntaxException {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan("com.kpmg.datamover.destination");
        entityManagerFactoryBean.setPersistenceUnitName("destinationdb");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabasePlatform(destinationSettings.getDialect());

        //TODO change to false after testing
        vendorAdapter.setGenerateDdl(true);


        vendorAdapter.setShowSql(true);
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        return entityManagerFactoryBean;
    }


    @Bean(name = "destinationTransactionManager")
    public PlatformTransactionManager productTransactionManager(
            @Qualifier("destinationEntityManagerFactory") EntityManagerFactory productEntityManagerFactory
    ) {
        return new JpaTransactionManager(productEntityManagerFactory);
    }
}
