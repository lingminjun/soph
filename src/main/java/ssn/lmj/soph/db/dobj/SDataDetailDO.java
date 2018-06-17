package ssn.lmj.soph.db.dobj;

import java.io.Serializable;

/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sun Jun 17 17:28:45 CST 2018
 * Table: s_data_detail
 */
public final class SDataDetailDO implements Serializable {
    private static final long serialVersionUID = 1L;
    public Long    id; // id
    public Long    did; // data id
    public Long    subid; // sub data id
    public Integer len; // data 长度
    public Integer slen; // sub data 长度
    public String  bites; // 冗余存储sub data的内容
    public Integer ts; // 匹配次数
}

