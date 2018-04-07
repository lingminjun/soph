package ssn.lmj.soph.biz;

import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.JaroWinkler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lmj.stone.core.Dispatcher;
import ssn.lmj.soph.db.dao.*;
import ssn.lmj.soph.db.dobj.SDataDO;
import ssn.lmj.soph.db.dobj.SDataDetailDO;
import ssn.lmj.soph.service.SophTalkService;
import ssn.lmj.soph.service.entities.Dialog;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.*;

@Service
public class SophTalkServiceImp implements SophTalkService {

    //相似性采用余弦相似性或则Dice
    //相似性实际可以抽象为原数据（现在简化为字符）集，原数据则是机器识别输入的单个单元，
    // 机器很难判别一个输入是复合还是原数据，这里会引入一个，记忆最小单位，也就是输入二级制流，
    // 小于记忆单位，统统算最原数据
    final static int MEMORY_BITS_LEN = 1;//暂时简化为一个字符

    final static int DISCERN_LEN = 5;//识别范围，也就是判断两个字符相似性事，长度差，与天赋相关
    final static int ABSTRACT_RANGE = 10;//抽象能力，也就是多少个相似后，开始对元素进行归类，

    final static double SIMILARITY_VALUE = 0.78666667f;
    final static double APPROXIMATE_VALUE = 0.91999996f;
    final static double APPROXIMATE_COSINE_VALUE = 0.66666666f;
    // 生物意识
    // 自我个体意识 "我","你","这里","那里"
    // 确定意识 "是","等于"
    // 学习冲动 变换数据（当你拿到一个新兴玩意时，你会摆弄），等号左右替换，方向等等

    // 智慧冲动
    // 自我论证，抽象重组数据后，得到很多的可能数据，但是每一个都需要加以论证，才能给出权重（以下不实现）

    @Autowired
    private SDataDAO sDataDAO;

    @Autowired
    private SDataDetailDAO sDataDetailDAO;

    @Autowired
    private SDataGroupDAO sDataGroupDAO;

    @Autowired
    private SGroupAsDAO sGroupAsDAO;

    @Autowired
    private SGroupDAO sGroupDAO;


    @Override
    public Dialog say(String text) {
        Dialog dialog = new Dialog();
        dialog.text = text;
        dialog.reply = "不知道";

        //寻找答案过程：
        //  1、全匹配，直接作答（查看最小匹配者）
        SDataDO dataDO = calculateDirectReply(text);
        if (dataDO != null) {
            dialog.reply = dataDO.bites;
            return dialog;
        }

        //  2、重组结果，并返回
        dataDO = calculateSimilarityReply(text);
        if (dataDO != null) {
            dialog.reply = dataDO.bites;
            return dialog;
        }

        return dialog;
    }



    public SDataDO calculateDirectReply(String text) {
        if (text.length() == 0) {
            return null;
        }
        int hcode = text.hashCode();
        String md5 = MD5(text);
        int len = text.length();

        SDataDO dataDO = sDataDAO.findByBites(hcode,md5,len,text);
        if (dataDO == null) {
            return null;
        }

        Set<Long> set = new HashSet<Long>();
        set.add(dataDO.id);

        List<SDataDetailDO> dids = sDataDetailDAO.findByMinSubId(dataDO.id);
        for (SDataDetailDO detailDO : dids) {
            set.add(detailDO.did);
        }
        List<SDataDetailDO> sids = sDataDetailDAO.findByMaxDId(dataDO.id);
        for (SDataDetailDO detailDO : sids) {
            set.add(detailDO.subid);
        }

        //计算得分最高的
        List<SDataDO> maybe = sDataDAO.queryMaxScoreByIds(new ArrayList<Long>(set));

        if (maybe.size() > 0) {
            return maybe.get(0);
        } else {
            return null;
        }

    }

    public SDataDO calculateSimilarityReply(String text) {
        int from = text.length() - DISCERN_LEN;
        if (from <= 1) {
            from = text.length();
        }
        int to = text.length() + (DISCERN_LEN * 2 <= text.length() ? text.length()/2 : DISCERN_LEN);
        List<SDataDO> maybe = sDataDAO.querySimilarityByLen(from,to,100);
        List<SDataDO> filter = new ArrayList<SDataDO>();
        Set<Long> fids = new HashSet<Long>();
        JaroWinkler jw = new JaroWinkler();
        Cosine cs = new Cosine();
        for (SDataDO dt : maybe) {
            double v = jw.similarity(text,dt.bites);

            //基本相等，说明无需继续寻找
            if (v >= APPROXIMATE_VALUE && cs.similarity(text,dt.bites) >= APPROXIMATE_COSINE_VALUE) {
                return dt;
            }

            if (v >= SIMILARITY_VALUE) {
                filter.add(dt);
                fids.add(dt.id);
            }
        }

        //以现在的容量，无法找到可能的答案
        if (fids.size() == 0) {
            System.out.println("以现在的容量，无法找到可能的答案");
            return null;
        }

        //计算分词字典（实际是元数据概念）
        List<SDataDetailDO> wds = sDataDetailDAO.queryByIds(new ArrayList<Long>(fids));
        int maxLen = 1;
        Map<String,SDataDetailDO> dic = new HashMap<String, SDataDetailDO>();
        for (SDataDetailDO d : wds) {
            dic.put(d.bites,d);
            if (maxLen < d.slen) {
                maxLen = d.slen;
            }
        }

        //每一个可能的对象开始使用正向最大分词
        List<List<SDataDetailDO>> sentence = new ArrayList<List<SDataDetailDO>>();

        //将原始数据放到一起，进行分词
        SDataDO txtData = new SDataDO();
        txtData.id = -1l;
        txtData.bites = text;
        txtData.len = text.length();
        filter.add(txtData);

        int count = 1;//分词个数
        for (SDataDO dt : filter) {
            List<SDataDetailDO> words = calculateMMWords(dt,dic,maxLen);
            sentence.add(words);
            if (count < words.size()) {
                count = words.size();
            }
        }

        //开始抽象分类
        List<Map<String,SDataDetailDO>> groups = new ArrayList<Map<String, SDataDetailDO>>();
        for (int i = 0; i < count; i++) {
            Map<String, SDataDetailDO> group = new HashMap<String, SDataDetailDO>();
            groups.add(group);
            for (int g = 0; g < sentence.size(); g++) {
                List<SDataDetailDO> words = sentence.get(g);
                if (words.size() < i) {
                    continue;
                }

                //第i组
                SDataDetailDO word = words.get(i);
                group.put(word.bites,word);
            }
        }

        /*
        1 + 1 = 2
        1 + 2 = 3
        1 + 3 = 4
        1 + 4 = 5
        1 + 5 = 6
        2 + 3 =

        [1,2]  =  g0，两个数据，必须重新定义分组
        [+]
        [1,2,3,4,5] = g0，超过两个数据，如果内部已有部分数据都属于一个分组，则其他数据也归纳到一个分组
        [=]
        [2,3,4,5,6] = g0，超过两个数据，如果内部已有部分数据都属于一个分组，则其他数据也归纳到一个分组

        */

        // 分好组后，开始归纳，归纳原则
        for (int i = 0; i < count; i++) {
            //第i组
            Map<String, SDataDetailDO> group = groups.get(i);

        }

        // 结果思考，与论证结果的可能性

        return null;
    }

    public List<SDataDetailDO> calculateMMWords(SDataDO dt, Map<String,SDataDetailDO> dic, int MAX_LENGTH) {
        String text = dt.bites;

        List<SDataDetailDO> result=new ArrayList<SDataDetailDO>();
        while(text.length()>0){
            int len=MAX_LENGTH;
            if(text.length()<MAX_LENGTH){
                len=text.length();
            }
            String tryWord = text.substring(0, len);
            SDataDetailDO word = dic.get(tryWord);
            while(word == null){
                if(tryWord.length() == 1) {
                    break;
                }
                tryWord = tryWord.substring(0, tryWord.length()-1);
            }

            //说明遇到了新词，应该存储下来
            if (word == null) {
                word = new SDataDetailDO();
                word.did = 0l;
                word.subid = 0l;
                word.len = 0;
                word.slen = 1;
                word.bites = tryWord;
                word.ts = 1;
            }

            result.add(word);
            text = text.substring(tryWord.length());
        }
        return result;
    }

    @Override
    public boolean tell(Dialog dialog) {

        if (dialog.text.isEmpty()) {
            return false;
        }

        int hcode = dialog.text.hashCode();
        String md5 = MD5(dialog.text);
        int len = dialog.text.length();
        SDataDO dataDO = sDataDAO.findByBites(hcode,md5,len,dialog.text);
        if (dataDO != null) {
            dataDO.ts = dataDO.ts + 1;
            sDataDAO.update(dataDO);
            return true;
        }
        dataDO = new SDataDO();
        dataDO.hcode = hcode;
        dataDO.md5 = md5;
        dataDO.len = len;
        dataDO.bites = dialog.text;
        dataDO.ts = 1;

        long id = sDataDAO.insert(dataDO);
        if (id > 0) {//开始异步处理
            final SDataDO finalDataDO = dataDO;
            Dispatcher.commonQueue().execute(new Runnable() {
                @Override
                public void run() {
                    matchDatas(finalDataDO,true);
                }
            });
        }

        return true;
    }

    //修改与他关联的数据
//    private static void changeMatchDatas(SDataDO dataDO) {
//        int len = dataDO.len;
//
//    }


    //匹配已有老数据，计算原始故事匹配可能
    private void matchDatas(SDataDO dataDO, boolean changeRelation) {

        Set<Long> set = new HashSet<Long>();

        int len = dataDO.len - 1;
        while (len > 0) {
            int trys = dataDO.len - len;

            //寻找连续段落
            for (int i = 0; i < trys; i++) {
                String sub = dataDO.bites.substring(i,i+len);
                int code = sub.hashCode();
                String md5 = MD5(sub);
                SDataDO dt = sDataDAO.findByBites(code,md5,len,sub);
                if (dt == null && len == 1) {
                    dt = new SDataDO();
                    dt.hcode = code;
                    dt.md5 = md5;
                    dt.len = len;
                    dt.bites = sub;
                    dt.id = sDataDAO.insert(dt);
                }

                if (dt != null) {

                    if (len != 1) {
                        set.add(dt.id);
                    }

                    SDataDetailDO detailDO = sDataDetailDAO.findByDidAndSubId(dataDO.id,dt.id);
                    //插入关系表
                    if (detailDO != null) {
                        detailDO.ts = detailDO.ts + 1;
                        sDataDetailDAO.update(detailDO);
                    } else  {
                        detailDO = new SDataDetailDO();
                        detailDO.did = dataDO.id;
                        detailDO.subid = dt.id;
                        detailDO.len = dataDO.len;
                        detailDO.slen = len;
                        detailDO.bites = sub;
                        detailDO.ts = 1;
                        sDataDetailDAO.insert(detailDO);
                    }
                }
            }

            len--;
        }

        if (changeRelation) {
            System.out.println("数据第一步分解完成");
        }

        //寻找与增加匹配度的数据有关联的原始数据
        if (changeRelation && set.size() > 0) {
            List<SDataDetailDO> rls = sDataDetailDAO.queryBySuIds(new ArrayList<Long>(set));
            set.clear();
            for (SDataDetailDO dobj : rls) {
                if (dobj.did != dataDO.id) {
                    set.add(dobj.did);
                }
            }


            if (set.size() > 0) {
                List<SDataDO> dts = sDataDAO.queryByIds(new ArrayList<Long>(set));
                for (SDataDO dobj : dts) {
                    matchDatas(dobj,false);
                }
            }
        }

        if (changeRelation) {
            System.out.println("数据第二步关联原始故事完成");
        }
    }

    /**
     * 对字符串md5加密
     *
     * @param str
     * @return
     */
    private static String MD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            throw new RuntimeException("MD5加密出现错误");
        }
    }
}
