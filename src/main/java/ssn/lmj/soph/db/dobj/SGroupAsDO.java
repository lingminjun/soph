package ssn.lmj.soph.db.dobj;

import java.io.Serializable;

/**
 * Owner: Robot
 * Creator: lingminjun
 * Version: 1.0.0
 * Since: Thu Apr 05 17:25:20 CST 2018
 * Table: s_group_as
 */
public final class SGroupAsDO implements Serializable {
    private static final long serialVersionUID = 1L;
    public Long    id; // id
    public Long    gid; // 抽象类型id
    public Long    asid;
}

