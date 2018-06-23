package ssn.lmj;


import com.lmj.stone.dao.gen.MybatisGenerator;
import com.lmj.stone.service.gen.ServiceGenerator;
import ssn.lmj.user.service.Exceptions;

import javax.xml.ws.Service;

public class GenMain {
    public static void main(String[] args) {
        System.out.println("开始生产代码");
        //生成数据相关对象
//        MybatisGenerator.gen("ssn.lmj.demo.db","sqls/city.sql");
//        MybatisGenerator.gen("ssn.lmj.soph.db","sqls/soph.sql");
//        MybatisGenerator.gen("ssn.lmj.user.db","sqls/user.sql");

        ServiceGenerator generator = new ServiceGenerator("ssn.lmj.demo.service","sqls/city.sql","demoTransactionManager", Exceptions.class);
        generator.gen();
    }
}
