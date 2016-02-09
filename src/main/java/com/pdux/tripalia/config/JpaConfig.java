package com.pdux.tripalia.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ClassUtils;

import com.heroku.sdk.jdbc.DatabaseUrl;
import com.pdux.tripalia.Application;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = Application.class)
class JpaConfig {

	@Value("${dataSource.driverClassName}")
	private String driver;
	@Value("${dataSource.url}")
	private String url;
	@Value("${dataSource.username}")
	private String username;
	@Value("${dataSource.password}")
	private String password;
	@Value("${hibernate.dialect}")
	private String dialect;
	@Value("${hibernate.hbm2ddl.auto}")
	private String hbm2ddlAuto;

	@Bean
	public DataSource dataSource() throws URISyntaxException {
		HikariConfig config = new HikariConfig();
		String connectionString = System.getenv("DATABASE_URL");
		config.setDriverClassName(driver);
		if (connectionString == null) {
			config.setJdbcUrl(url);
			config.setUsername(username);
			config.setPassword(password);
		} else {
			System.out.println("Database URL: " + connectionString);
			URI uri = new URI(connectionString);
			String databaseJdbcURL = "jdbc:postgresql:" + uri.getHost() + ":"
					+ uri.getPort();
			System.out.println("Database jdbc URL: " + databaseJdbcURL);
			config.setJdbcUrl(databaseJdbcURL);
			System.out.println(uri.getRawUserInfo());
			config.setUsername(uri.getRawUserInfo().split(":")[0]);
			config.setPassword(uri.getRawUserInfo().split(":")[1]);
		}
		config.addDataSourceProperty("ssl", "true");
		config.addDataSourceProperty("sslfactory",
				"org.postgresql.ssl.NonValidatingFactory");
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		config.addDataSourceProperty("useServerPrepStmts", "true");

		return new HikariDataSource(config);
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
			DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(dataSource);

		String entities = ClassUtils.getPackageName(Application.class);
		String converters = ClassUtils
				.getPackageName(Jsr310JpaConverters.class);
		entityManagerFactoryBean.setPackagesToScan(entities, converters);

		entityManagerFactoryBean
				.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

		Properties jpaProperties = new Properties();
		jpaProperties.put(org.hibernate.cfg.Environment.DIALECT, dialect);
		jpaProperties.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO,
				hbm2ddlAuto);
		entityManagerFactoryBean.setJpaProperties(jpaProperties);

		return entityManagerFactoryBean;
	}

	@Bean
	public PlatformTransactionManager transactionManager(
			EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
}
