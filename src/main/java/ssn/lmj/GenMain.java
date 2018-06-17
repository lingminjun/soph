package ssn.lmj;


import com.lmj.stone.dao.gen.MybatisGenerator;

public class GenMain {
    public static void main(String[] args) {
        //生成数据相关对象
        MybatisGenerator.gen("ssn.lmj.demo.db","sqls/city.sql");
        MybatisGenerator.gen("ssn.lmj.soph.db","sqls/soph.sql");
        MybatisGenerator.gen("ssn.lmj.user.db","sqls/user.sql");
    }
}