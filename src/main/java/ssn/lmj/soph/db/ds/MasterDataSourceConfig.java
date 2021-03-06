package ssn.lmj.soph.db.ds;

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
        basePackages = "ssn.lmj.soph.db.dao",
        sqlSessionFactoryRef = MasterDataSourceConfig.SQL_SESSION_FACTORY
)
public class MasterDataSourceConfig extends com.lmj.stone.dao.DataSource {

    final static String SQL_SESSION_FACTORY = "masterSqlSessionFactory";
    final static String DATA_SOURCE = "masterDataSource";
    final static String TRANSACTION_MANAGER = "masterTransactionManager";

    @Override
    @Primary
    @Bean(name = MasterDataSourceConfig.DATA_SOURCE)
    public DataSource dataSource() {
        return genDataSource();
    }

    @Override
    @Primary
    @Bean(name = MasterDataSourceConfig.TRANSACTION_MANAGER)
    public DataSourceTransactionManager transactionManager() {
        return genTransactionManager();
    }

    @Override
    @Primary
    @Bean(name = MasterDataSourceConfig.SQL_SESSION_FACTORY)
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        return genSqlSessionFactory();
    }
}