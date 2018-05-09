package ssn.lmj.user.db.ds;

import com.lmj.stone.dao.DataSourceConfiguration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@DataSourceConfiguration(url = @Value("${master.datasource.url}"),
        user = @Value("${master.datasource.username}"),
        password = @Value("${master.datasource.password}"),
        basePackages = "ssn.lmj.user.db.dao",
        sqlSessionFactoryRef = UserDataSourceConfig.SQL_SESSION_FACTORY

)
public class UserDataSourceConfig extends com.lmj.stone.dao.DataSource {

    final static String SQL_SESSION_FACTORY = "userSqlSessionFactory";

    @Override
    @Bean(name = "userDataSource")
    @Primary
    public DataSource dataSource() {
        return genDataSource();
    }

    @Override
    @Bean(name = "userTransactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager() {
        return genTransactionManager();
    }

    @Override
    @Bean(name = UserDataSourceConfig.SQL_SESSION_FACTORY)
    @Primary
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        return genSqlSessionFactory();
    }
}