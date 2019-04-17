package com.bdqn.ssm.dao;

import com.bdqn.ssm.entity.Provider;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProviderMapper {

    /**
     *
     * @return
     */
   //查询所有
    public List<Provider> ProviderList();

    /**
     *
     * @param proCode
     * @param proName
     * @return
     */
    //获取总记录数
    public int proCount(@Param("proCode")String proCode,@Param("proName")String proName);
    /**
     *
     * @param proCode
     * @param proName
     * @param pageNo
     * @param pageSize
     * @return
     */
   //分页查询
    public List<Provider> proList(@Param("proCode")String proCode,
                                  @Param("proName")String proName,
                                  @Param("pageNo")Integer pageNo,
                                  @Param("pageSize")Integer pageSize);

    /**
     *
     * @param provider  供应商实体类
     * @return
     */
    //增加
    public int addPro(Provider provider);

    /**
     *
     * @param proId  供货商id
     * @return
     */
    //根据ID查询
    public Provider proById(Integer proId);

    /**
     *
     * @param provider
     * @return
     */
    //修改
    public int proModify(Provider provider);

    /**
     *
     * @param proid
     * @return
     */
    //删除
    public int delPro(Integer proid);
}
