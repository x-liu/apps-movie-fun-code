package org.superbiz.moviefun.db;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;


import javax.sql.DataSource;

@Configuration
public class DbConfig {

    @Bean
    public HibernateJpaVendorAdapter getHibernateJpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setGenerateDdl(true);
        adapter.setDatabase(Database.MYSQL);
        adapter.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect");
        return adapter;
    }

    @Bean("albumsTM")
    public PlatformTransactionManager getPlatformTransactionManagerAlbums(@Qualifier("albumsFactory") LocalContainerEntityManagerFactoryBean localBean) {
        JpaTransactionManager jpaManager = new JpaTransactionManager(localBean.getObject());
        return jpaManager;
    }

    @Bean("moviesTM")
    public PlatformTransactionManager getPlatformTransactionManagerMovies(@Qualifier("moviesFactory") LocalContainerEntityManagerFactoryBean localBean) {
        JpaTransactionManager jpaManager = new JpaTransactionManager(localBean.getObject());
        return jpaManager;
    }

    @Bean
    public DataSource albumsDataSource(
            @Value("${moviefun.datasources.albums.url}") String url,
            @Value("${moviefun.datasources.albums.username}") String username,
            @Value("${moviefun.datasources.albums.password}") String password
    ) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setDatabaseName("albums");
        return dataSource;
    }

    @Bean
    public DataSource moviesDataSource(
            @Value("${moviefun.datasources.movies.url}") String url,
            @Value("${moviefun.datasources.movies.username}") String username,
            @Value("${moviefun.datasources.movies.password}") String password
    ) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setDatabaseName("movies");
        return createConnectionPool(dataSource);
    }

    @Bean
    public DataSource createConnectionPool(DataSource dataSource) {
        HikariConfig config = new HikariConfig();
        config.setDataSource(dataSource);
        return new HikariDataSource(config);
    }

    private static LocalContainerEntityManagerFactoryBean buildEntityManagerFactoryBean(DataSource dataSource, HibernateJpaVendorAdapter adapter, String unitName) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setJpaVendorAdapter(adapter);
        factoryBean.setPackagesToScan("org.superbiz.moviefun.movies", "org.superbiz.moviefun.albums");
        factoryBean.setPersistenceUnitName(unitName);
        return factoryBean;
    }

    @Bean("moviesFactory")
    public LocalContainerEntityManagerFactoryBean getLocalContainerEntityManagerFactoryBeanMovies(
//              can also use belowVVV
//            @Bean
//            public LocalContainerEntityManagerFactoryBean movies(
            @Qualifier("moviesDataSource") DataSource dataSource,
            @Qualifier("getHibernateJpaVendorAdapter") HibernateJpaVendorAdapter adapter) {
        return buildEntityManagerFactoryBean(dataSource, adapter, "movies");
    }

    @Bean("albumsFactory")
    public LocalContainerEntityManagerFactoryBean getLocalContainerEntityManagerFactoryBeanAlbums(
//        @Bean
//        public LocalContainerEntityManagerFactoryBean albums(
            @Qualifier("albumsDataSource") DataSource dataSource,
            @Qualifier("getHibernateJpaVendorAdapter") HibernateJpaVendorAdapter adapter) {
        return buildEntityManagerFactoryBean(dataSource, adapter, "albums");
    }
}
