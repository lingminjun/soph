package ssn.lmj.soph.db.dobj;

import java.io.Serializable;

/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sun Jun 17 22:49:13 CST 2018
 * Table: s_data_group
 */
public final class SDataGroupDO implements Serializable {
    private static final long serialVersionUID = 1L;
    public Long    id; // id
    public Long    gid; // 抽象类型id
    public Long    did; // 数据id
}

