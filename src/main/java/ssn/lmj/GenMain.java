package ssn.lmj;


import com.lmj.stone.dao.gen.MybatisGenerator;
import com.lmj.stone.service.gen.RestfulControllerGenerator;
import com.lmj.stone.service.gen.ServiceGenerator;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import ssn.lmj.user.service.Exceptions;

import javax.xml.ws.Service;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenMain {
    public static void main(String[] args) {
        System.out.println("开始生产代码");


        //生成数据相关对象
//        MybatisGenerator.gen("ssn.lmj.demo.db","sqls/city.sql");

//        MybatisGenerator.gen("ssn.lmj.soph.db","sqls/soph.sql");

//        {
//            MybatisGenerator generator = new MybatisGenerator("ssn.lmj.demo.db", "sqls/city.sql", "s_");
//            generator.gen();
//        }

        {
            MybatisGenerator generator = new MybatisGenerator("ssn.lmj.user.db", "sqls/user.sql", "s_");
            generator.gen();
        }

//        {
//            ServiceGenerator generator = new ServiceGenerator("ssn.lmj.user.service", "sqls/user.sql", "demoTransactionManager",Exceptions.class,"s_");
//            generator.gen();
//        }


        RestfulControllerGenerator generator = new RestfulControllerGenerator("ssn.lmj.permission.controller","api/permission/","sqls/permission.sql","demoTransactionManager", Exceptions.class, "s_");
        generator.gen();

    }
}
