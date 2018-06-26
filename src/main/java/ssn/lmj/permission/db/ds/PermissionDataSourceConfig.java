package ssn.lmj.permission.db.ds;

import com.lmj.stone.dao.DataSourceConfiguration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@DataSourceConfiguration(url = @Value("${master.datasource.url}"),
        user = @Value("${master.datasource.username}"),
        password = @Value("${master.datasource.password}"),
        basePackages = "ssn.lmj.permission.db.dao",
        sqlSessionFactoryRef = PermissionDataSourceConfig.SQL_SESSION_FACTORY

)
public class PermissionDataSourceConfig extends com.lmj.stone.dao.DataSource {

    public final static String SQL_SESSION_FACTORY = "permissionSqlSessionFactory";
    public final static String DATA_SOURCE = "permissionDataSource";
    public final static String TRANSACTION_MANAGER = "permissionTransactionManager";

    @Override
    @Bean(name = PermissionDataSourceConfig.DATA_SOURCE)
    public DataSource dataSource() {
        return genDataSource();
    }

    @Override
    @Bean(name = PermissionDataSourceConfig.TRANSACTION_MANAGER)
    public DataSourceTransactionManager transactionManager() {
        return genTransactionManager();
    }

    @Override
    @Bean(name = PermissionDataSourceConfig.SQL_SESSION_FACTORY)
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        return genSqlSessionFactory();
    }
}