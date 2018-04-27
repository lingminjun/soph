package ssn.lmj;

import com.alibaba.fastjson.JSON;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Spring Boot 应用启动类
 *
 * Created by bysocket on 16/4/26.
 */
// Spring Boot 应用的标识
@SpringBootApplication
// mapper 接口类扫描包配置
//@MapperScan("ssn.lmj.demo.dao")
//@MapperScan("ssn.lmj.soph.db")
public class Application {

//    static class TObj {
//        public String val1;
//        public int val2;
//        public String val3;
//        public int val4;
//        public float val5;
//        public long val6;
//        public boolean val7;
//    }

    public static void main(String[] args) {

//        String json = "{\"val1\":123,\"val2\":123.1,\"val3\":123,\"val4\":\"453\"," +
//                "\"val5\":123,\"val6\":\"44\",\"val7\":\"0\"}";
//        TObj obj = JSON.parseObject(json,TObj.class);
//
//        System.out.println(">>>>>\n" + JSON.toJSONString(obj));

        // 程序启动入口
        // 启动嵌入式的 Tomcat 并初始化 Spring 环境及其各 Spring 组件
        SpringApplication.run(Application.class,args);
    }
}
