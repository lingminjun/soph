package ssn.lmj.soph.db.dobj;

import java.io.Serializable;

/**
 * Owner: Robot
 * Creator: lingminjun
 * Version: 1.0.0
 * Since: Thu Apr 05 17:25:20 CST 2018
 * Table: s_data_group
 */
public final class SDataGroupDO implements Serializable {
    private static final long serialVersionUID = 1L;
    public Long    id; // id
    public Long    gid; // 抽象类型id
    public Long    did; // 数据id
}

