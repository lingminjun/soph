package ssn.lmj;

import com.alibaba.fastjson.JSON;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.ParameterizedType;


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
//
//    abstract static class TType<T> {
//        private Class<T> _clazz;
//        public TType() {
//            //擦拭法站台
//            this._clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//        }
//
//        public Class<T> getTypeClass() {
//            return _clazz;
//        }
//    }



    public static void main(String[] args) {

        // 程序启动入口
        // 启动嵌入式的 Tomcat 并初始化 Spring 环境及其各 Spring 组件
        SpringApplication.run(Application.class,args);
    }
}
