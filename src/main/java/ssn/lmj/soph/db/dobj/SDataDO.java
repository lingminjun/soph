package ssn.lmj.soph.db.dobj;

import java.io.Serializable;

/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sun Jun 17 13:35:19 CST 2018
 * Table: s_data
 */
public final class SDataDO implements Serializable {
    private static final long serialVersionUID = 1L;
    public Long    id; // 数据id
    public Integer hcode; // 内容 hash code，这里取java hash code
    public String  md5; // 内容 md5
    public Integer len; // 内容长度
    public String  bites; // 内容 数据集(暂时先用varchar)
    public Integer ts; // 提及次数，每次被告知，或者每次被匹配上
    public Long    createAt; // 创建时间
}

