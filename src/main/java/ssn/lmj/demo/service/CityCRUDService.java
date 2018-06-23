package ssn.lmj.demo.service;

import ssn.lmj.user.service.Exceptions;
import com.lmj.stone.idl.IDLAPISecurity;
import com.lmj.stone.idl.IDLException;
import com.lmj.stone.idl.annotation.IDLAPI;
import com.lmj.stone.idl.annotation.IDLError;
import com.lmj.stone.idl.annotation.IDLGroup;
import com.lmj.stone.idl.annotation.IDLParam;
import java.util.List;
import ssn.lmj.demo.db.dao.CityDAO;
import ssn.lmj.demo.service.entities.CityPOJO;
import ssn.lmj.demo.service.entities.CityResults;

/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Sat Jun 23 10:43:15 CST 2018
 * Table: city
 */
@IDLGroup(domain = "city", desc = "City的相关操作", codeDefine = Exceptions.class)
public interface CityCRUDService {

    /**
     * CityDAO
     * @return 
     */
    public CityDAO getCityDAO();

    /**
     * batch insert CityPOJO
     * @return 
     */
    @IDLAPI(module = "city",name = "batchAddCity", desc = "批量插入CityPOJO", security = IDLAPISecurity.UserLogin)
    public boolean batchAddCity(@IDLParam(name = "models", desc = "实体对象", required = true) final List<CityPOJO> models,
                                @IDLParam(name = "ignoreError", desc = "忽略错误，单个插入，但是效率低；若不忽略错误，批量提交，效率高", required = true) final boolean ignoreError) throws IDLException;

    /**
     * remove CityPOJO
     * @return 
     */
    @IDLAPI(module = "city",name = "removeTheCity", desc = "删除CityPOJO", security = IDLAPISecurity.UserLogin)
    public boolean removeTheCity(@IDLParam(name = "id", desc = "对象id", required = true) final long id) throws IDLException;

    /**
     * update CityPOJO
     * @return 
     */
    @IDLAPI(module = "city",name = "updateTheCity", desc = "更新CityPOJO，仅更新不为空的字段", security = IDLAPISecurity.UserLogin)
    public boolean updateTheCity(@IDLParam(name = "id", desc = "更新对象的id", required = true) final long id,
                                 @IDLParam(name = "provinceId", desc = "省份编号", required = false) final Integer provinceId,
                                 @IDLParam(name = "cityName", desc = "城市名称", required = false) final String cityName,
                                 @IDLParam(name = "description", desc = "描述", required = false) final String description) throws IDLException;

    /**
     * find CityPOJO by id
     * @return 
     */
    @IDLAPI(module = "city",name = "findTheCity", desc = "寻找CityPOJO", security = IDLAPISecurity.UserLogin)
    public CityPOJO findTheCity(@IDLParam(name = "id", desc = "对象id", required = true) final long id) throws IDLException;

    /**
     * query CityPOJO
     * @return 
     */
    @IDLAPI(module = "city",name = "queryCityByProvinceId", desc = "批量插入CityPOJO", security = IDLAPISecurity.UserLogin)
    public CityResults queryCityByProvinceId(@IDLParam(name = "pageIndex", desc = "页索引，从1开始，传入0或负数无数据返回", required = true) final int pageIndex,
                                             @IDLParam(name = "pageSize", desc = "一页最大行数", required = true) final int pageSize,
                                             @IDLParam(name = "provinceId", desc = "省份编号", required = true) final int provinceId,
                                             @IDLParam(name = "isDeleted", desc = "是否已经被标记删除的", required = false) final boolean isDeleted) throws IDLException;

    /**
     * query CityPOJO
     * @return 
     */
    @IDLAPI(module = "city",name = "queryCityByProvinceIdAndCityName", desc = "批量插入CityPOJO", security = IDLAPISecurity.UserLogin)
    public CityResults queryCityByProvinceIdAndCityName(@IDLParam(name = "pageIndex", desc = "页索引，从1开始，传入0或负数无数据返回", required = true) final int pageIndex,
                                                        @IDLParam(name = "pageSize", desc = "一页最大行数", required = true) final int pageSize,
                                                        @IDLParam(name = "provinceId", desc = "省份编号", required = true) final int provinceId,
                                                        @IDLParam(name = "cityName", desc = "城市名称", required = true) final String cityName,
                                                        @IDLParam(name = "isDeleted", desc = "是否已经被标记删除的", required = false) final boolean isDeleted) throws IDLException;

}

