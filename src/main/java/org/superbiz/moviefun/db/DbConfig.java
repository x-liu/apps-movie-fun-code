package org.superbiz.moviefun.db;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
            adapter.setDatabase(Database.MYSQL);
            adapter.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect");
            adapter.setGenerateDdl(true);

            return adapter;

    }

    public LocalContainerEntityManagerFactoryBean getLocalContainerEntityManagerFactoryBean(DataSource dataSource,
                                                                                            HibernateJpaVendorAdapter adapter) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setJpaVendorAdapter(adapter);
        factoryBean.setPackagesToScan(DbConfig.class.getPackage().toString());
        factoryBean.setPersistenceUnitName("albums");

        return factoryBean;
    }


    @Configuration
    private class AlbumDS {

        public AlbumDS() {
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
        @Bean("albums")
        public LocalContainerEntityManagerFactoryBean getLocalContainerEntityManagerFactoryBean(
//        @Bean
//        public LocalContainerEntityManagerFactoryBean albums(
                @Qualifier("albumsDataSource") DataSource dataSource,
                HibernateJpaVendorAdapter adapter) {
            LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
            factoryBean.setDataSource(dataSource);
            factoryBean.setJpaVendorAdapter(adapter);
            factoryBean.setPackagesToScan(DbConfig.class.getPackage().toString());
            factoryBean.setPersistenceUnitName("albums");

            return factoryBean;
        }


        @Bean("albumsTM")
        public PlatformTransactionManager getPlatformTransactionManager(@Qualifier("albums") LocalContainerEntityManagerFactoryBean localBean) {
            JpaTransactionManager jpaManager = new JpaTransactionManager(localBean.getObject());
            return jpaManager;
        }


    }


    @Configuration
    private class MovieDS {

        public MovieDS() {
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
            return dataSource;
        }

        @Bean("movies")
        public LocalContainerEntityManagerFactoryBean getLocalContainerEntityManagerFactoryBean(
//              can also use belowVVV
//            @Bean
//            public LocalContainerEntityManagerFactoryBean movies(
                    @Qualifier("moviesDataSource") DataSource dataSource,
                HibernateJpaVendorAdapter adapter) {
            LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
            factoryBean.setDataSource(dataSource);
            factoryBean.setJpaVendorAdapter(adapter);
            factoryBean.setPackagesToScan(DbConfig.class.getPackage().toString());
            factoryBean.setPersistenceUnitName("movies");

            return factoryBean;
        }
        @Bean("moviesTM")
        public PlatformTransactionManager getPlatformTransactionManager(@Qualifier("movies") LocalContainerEntityManagerFactoryBean localBean) {
            JpaTransactionManager jpaManager = new JpaTransactionManager(localBean.getObject());

            return jpaManager;
        }
    }

}
