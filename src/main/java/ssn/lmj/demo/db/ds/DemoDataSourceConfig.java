package ssn.lmj.demo.db.ds;

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
        basePackages = "ssn.lmj.demo.db.dao",
        sqlSessionFactoryRef = DemoDataSourceConfig.SQL_SESSION_FACTORY
)
public class DemoDataSourceConfig extends com.lmj.stone.dao.DataSource {

    final static String SQL_SESSION_FACTORY = "demoSqlSessionFactory";
    final static String DATA_SOURCE = "demoDataSource";
    final static String TRANSACTION_MANAGER = "demoTransactionManager";

    @Override
    @Bean(name = DemoDataSourceConfig.DATA_SOURCE)
    public DataSource dataSource() {
        return genDataSource();
    }

    @Override
    @Bean(name = DemoDataSourceConfig.TRANSACTION_MANAGER)
    public DataSourceTransactionManager transactionManager() {
        return genTransactionManager();
    }

    @Override
    @Bean(name = DemoDataSourceConfig.SQL_SESSION_FACTORY)
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        return genSqlSessionFactory();
    }
}