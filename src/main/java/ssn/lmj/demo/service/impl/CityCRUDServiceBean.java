package ssn.lmj.demo.service.impl;

import com.lmj.stone.cache.AutoCache;
import com.lmj.stone.service.Injects;
import com.lmj.stone.service.BlockUtil;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import ssn.lmj.user.service.Exceptions;
import com.lmj.stone.idl.IDLAPISecurity;
import com.lmj.stone.idl.IDLException;
import com.lmj.stone.idl.annotation.IDLError;
import com.lmj.stone.idl.annotation.IDLParam;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssn.lmj.demo.db.dao.CityDAO;
import ssn.lmj.demo.db.dobj.CityDO;
import ssn.lmj.demo.service.entities.CityPOJO;
import ssn.lmj.demo.service.entities.CityResults;
import ssn.lmj.demo.service.CityCRUDService;
import javax.annotation.Resource;


/**
 * Owner: Minjun Ling
 * Creator: Robot
 * Version: 1.0.0
 * Since: Mon Jun 25 09:42:13 CST 2018
 * SQLFile: sqls/city.sql
 */
@Service
public class CityCRUDServiceBean implements CityCRUDService {

    @Resource(name = "demoTransactionManager")
    protected DataSourceTransactionManager transactionManager;

    @Autowired
    private CityDAO cityDAO;

    /**
     * CityDAO
     * @return 
     */
    @Override
    public CityDAO getCityDAO() {
        return this.cityDAO;
    }

    /**
     * insert CityPOJO
     * @return 
     */
    @Override
    public long addCity(@IDLParam(name = "city", desc = "实体对象", required = true) final CityPOJO city
) throws IDLException {
        return BlockUtil.en(transactionManager, new BlockUtil.Call<Long>() {
            @Override
            public Long run() throws Throwable {
                CityDO dobj = new CityDO();
                Injects.fill(city,dobj);
                if (getCityDAO().insert(dobj) > 0) {
                    return (Long)dobj.id;
                } else {
                    return -1l;
                }
            }
        });
    }

    /**
     * batch insert CityPOJO
     * @return 
     */
    @Override
    public boolean batchAddCity(@IDLParam(name = "models", desc = "实体对象", required = true) final List<CityPOJO> models,
                                @IDLParam(name = "ignoreError", desc = "忽略错误，单个插入，但是效率低；若不忽略错误，批量提交，效率高", required = true) final boolean ignoreError) throws IDLException {
        if (ignoreError) {
            for (final CityPOJO  pojo : models) {
                BlockUtil.en(transactionManager, new BlockUtil.Call<Boolean>() {
                    @Override
                    public Boolean run() throws Throwable {
                        CityDO dobj = new CityDO();
                        Injects.fill(pojo,dobj);
                        getCityDAO().insert(dobj);
                        return true;
                    }
                });
            }
            return true;
        } else {
           return BlockUtil.en(transactionManager, new BlockUtil.Call<Boolean>() {
                @Override
                public Boolean run() throws Throwable {
                    for (CityPOJO pojo : models) {
                        CityDO dobj = new CityDO();
                        Injects.fill(pojo,dobj);
                        getCityDAO().insert(dobj);
                    }
                    return true;
                }
            });
        }

    }

    /**
     * remove CityPOJO
     * @return 
     */
    @Override
    @AutoCache(key = "CITY_#{id}", evict = true)
    public boolean removeTheCity(@IDLParam(name = "id", desc = "对象id", required = true) final long id) throws IDLException {
        return BlockUtil.en(transactionManager, new BlockUtil.Call<Boolean>() {
            @Override
            public Boolean run() throws Throwable {
                getCityDAO().deleteById(id);
                return true;
           }
        });
    }

    /**
     * update CityPOJO
     * @return 
     */
    @Override
    @AutoCache(key = "CITY_#{id}", evict = true)
    public boolean updateTheCity(@IDLParam(name = "id", desc = "更新对象的id", required = true) final long id,
                                 @IDLParam(name = "provinceId", desc = "省份编号", required = false) final Integer provinceId,
                                 @IDLParam(name = "cityName", desc = "城市名称", required = false) final String cityName,
                                 @IDLParam(name = "description", desc = "描述", required = false) final String description) throws IDLException {
        return BlockUtil.en(transactionManager, new BlockUtil.Call<Boolean>() {
            @Override
            public Boolean run() throws Throwable {
                CityDO dobj = new CityDO();
                dobj.id = (long)id;
                dobj.provinceId = provinceId;
                dobj.cityName = cityName;
                dobj.description = description;
                getCityDAO().update(dobj);
                return true;
            }
        });
    }

    /**
     * find CityPOJO by id
     * @return 
     */
    @Override
    @AutoCache(key = "CITY_#{id}", async = true, condition="!#{noCache}")
    public CityPOJO findTheCity(@IDLParam(name = "id", desc = "对象id", required = true) final long id,@IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException {
        CityDO dobj = getCityDAO().getById(id);
        CityPOJO pojo = new CityPOJO();
        Injects.fill(dobj,pojo);
        return pojo;
    }

    /**
     * query CityPOJO
     * @return 
     */
    @Override
    @AutoCache(key = "CITY_QUERY_BY_PROVINCE_ID:#{provinceId}_PAGE:#{pageIndex},#{pageSize}_DEL:#{isDeleted}", async = true, condition="!#{noCache} && !#{isDeleted}")
    public CityResults queryCityByProvinceId(@IDLParam(name = "pageIndex", desc = "页索引，从1开始，传入0或负数无数据返回", required = true) final int pageIndex,
                                             @IDLParam(name = "pageSize", desc = "一页最大行数", required = true) final int pageSize,
                                             @IDLParam(name = "provinceId", desc = "省份编号", required = true) final int provinceId,
                                             @IDLParam(name = "isDeleted", desc = "是否已经被标记删除的", required = false) final boolean isDeleted,
                                             @IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException {
        if (pageIndex <= 0 || pageSize <= 0) {
            throw new IDLException("参数错误","city",-1,"翻页参数传入错误");
        }
        CityResults rlt = new CityResults();
        rlt.index = pageIndex;
        rlt.size = pageSize;
        rlt.total = getCityDAO().countByProvinceId(provinceId,(isDeleted ? 1 : 0));
        List<CityDO> list = getCityDAO().queryByProvinceId(provinceId,(isDeleted ? 1 : 0),null,false,(pageSize * (pageIndex - 1)), pageSize);
        rlt.results = new ArrayList<CityPOJO>();
        for (CityDO dobj : list) {
            CityPOJO pojo = new CityPOJO();
            Injects.fill(dobj,pojo);
            rlt.results.add(pojo);
        }
       return rlt;
    }

    /**
     * query CityPOJO
     * @return 
     */
    @Override
    @AutoCache(key = "CITY_QUERY_BY_PROVINCE_ID:#{provinceId}_CITY_NAME:#{cityName}_PAGE:#{pageIndex},#{pageSize}_DEL:#{isDeleted}", async = true, condition="!#{noCache} && !#{isDeleted}")
    public CityResults queryCityByProvinceIdAndCityName(@IDLParam(name = "pageIndex", desc = "页索引，从1开始，传入0或负数无数据返回", required = true) final int pageIndex,
                                                        @IDLParam(name = "pageSize", desc = "一页最大行数", required = true) final int pageSize,
                                                        @IDLParam(name = "provinceId", desc = "省份编号", required = true) final int provinceId,
                                                        @IDLParam(name = "cityName", desc = "城市名称", required = true) final String cityName,
                                                        @IDLParam(name = "isDeleted", desc = "是否已经被标记删除的", required = false) final boolean isDeleted,
                                                        @IDLParam(name = "noCache", desc = "不走缓存", required = false) final boolean noCache) throws IDLException {
        if (pageIndex <= 0 || pageSize <= 0) {
            throw new IDLException("参数错误","city",-1,"翻页参数传入错误");
        }
        CityResults rlt = new CityResults();
        rlt.index = pageIndex;
        rlt.size = pageSize;
        rlt.total = getCityDAO().countByProvinceIdAndCityName(provinceId,cityName,(isDeleted ? 1 : 0));
        List<CityDO> list = getCityDAO().queryByProvinceIdAndCityName(provinceId,cityName,(isDeleted ? 1 : 0),null,false,(pageSize * (pageIndex - 1)), pageSize);
        rlt.results = new ArrayList<CityPOJO>();
        for (CityDO dobj : list) {
            CityPOJO pojo = new CityPOJO();
            Injects.fill(dobj,pojo);
            rlt.results.add(pojo);
        }
       return rlt;
    }

}

