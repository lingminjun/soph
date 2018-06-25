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

public class GenMain {
    public static void main(String[] args) {
        System.out.println("开始生产代码");
        //生成数据相关对象
//        MybatisGenerator.gen("ssn.lmj.demo.db","sqls/city.sql");
//        MybatisGenerator.gen("ssn.lmj.soph.db","sqls/soph.sql");
//        MybatisGenerator.gen("ssn.lmj.user.db","sqls/user.sql");

//        ServiceGenerator generator = new ServiceGenerator("ssn.lmj.demo.service","sqls/city.sql","demoTransactionManager", Exceptions.class);
//        generator.gen();

        RestfulControllerGenerator generator = new RestfulControllerGenerator("ssn.lmj.demo.controller","api/","sqls/city.sql","demoTransactionManager", Exceptions.class);
        generator.gen();

//        if (evaluateCondition("!1")) {
//            System.out.println("isOk");
//        }

    }

//    private static boolean evaluateCondition(String condition) {
//        ExpressionParser parser=new SpelExpressionParser();
//        try {
//            Expression exp=parser.parseExpression(condition);
//            //parserExpression("'Hello World'.concat('!')");
//            Boolean v = (Boolean)exp.getValue(Boolean.class);
//            if (v != null && v.booleanValue()) {
//                return true;
//            }
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
}
