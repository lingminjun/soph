package ssn.lmj.user.db.ds;

import com.lmj.stone.dao.DataSourceConfiguration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
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

    public final static String SQL_SESSION_FACTORY = "userSqlSessionFactory";
    public final static String DATA_SOURCE = "userDataSource";
    public final static String TRANSACTION_MANAGER = "userTransactionManager";

    @Override
    @Bean(name = UserDataSourceConfig.DATA_SOURCE)
    public DataSource dataSource() {
        return genDataSource();
    }

    @Override
    @Bean(name = UserDataSourceConfig.TRANSACTION_MANAGER)
    public DataSourceTransactionManager transactionManager() {
        return genTransactionManager();
    }

    @Override
    @Bean(name = UserDataSourceConfig.SQL_SESSION_FACTORY)
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        return genSqlSessionFactory();
    }
}