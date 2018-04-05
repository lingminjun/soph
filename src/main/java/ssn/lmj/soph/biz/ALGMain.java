package ssn.lmj.soph.biz;


import com.sun.org.apache.xerces.internal.xs.StringList;
import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.Jaccard;
import info.debatty.java.stringsimilarity.JaroWinkler;
import info.debatty.java.stringsimilarity.SorensenDice;

import java.lang.reflect.Array;
import java.util.*;

//专门用来测试算法
public class ALGMain {
    public static void main(String[] args) {
        String str1 = "1+1=2";

        String[] strs = {"1+2=3","1+3=4","1+4=5","3-1=2","2=1+1","1+1=3","1+1=4"};

        JaroWinkler jw = new JaroWinkler();
        Cosine cs = new Cosine();
        Jaccard ja = new Jaccard();
        SorensenDice sn = new SorensenDice();
        List<String> filter = new ArrayList<String>();
        for (String str : strs) {
            System.out.println(str + " jw="+jw.similarity(str1, str));
            System.out.println(str + " cs="+cs.similarity(str1, str));
            System.out.println(str + " ja="+ja.similarity(str1, str));
            System.out.println(str + " sn="+sn.similarity(str1, str));
            System.out.println("");
            double v = jw.similarity(str1, str);
            if (v >= 0.78666667f) {
                filter.add(str);
            }
        }

        //按照组开始拆分
        Set<String> dic = new HashSet<String>();
        dic.add("今天");
        dic.add("明天");
        dic.add("天气");
        dic.add("怎么样");
        dic.add("一起去");
        dic.add("我");
        dic.add("我们");
        dic.add("天气不错");
        System.out.println(forwardSeg("今天天气不错，我们一起去打球吧",4,dic));

    }

    public static List<String> forwardSeg(String text, int MAX_LENGTH, Set<String> DIC){
        List<String> result=new ArrayList<String>();
        while(text.length()>0){
            int len=MAX_LENGTH;
            if(text.length()<MAX_LENGTH){
                len=text.length();
            }
            String tryWord=text.substring(0, len);
            while(!DIC.contains(tryWord)){
                if(tryWord.length()==1)
                    break;
                tryWord=tryWord.substring(0, tryWord.length()-1);
            }
            result.add(tryWord+"/");
            text=text.substring(tryWord.length());
        }
        return result;
    }
}
