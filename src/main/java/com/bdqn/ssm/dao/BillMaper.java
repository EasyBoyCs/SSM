package com.bdqn.ssm.dao;

import com.bdqn.ssm.entity.Bill;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BillMaper {

    /**
     *
     * @param productName 商品名称
     * @param providerId  供应商id
     * @return
     */
   //获取总记录数
    public int count(@Param("productName")String productName,
                     @Param("providerId")int providerId);

    /**
     *
     * @param productName  商品名称
     * @param providerId   供应商id
     * @param IsPayment    是否付款
     * @param pageNo       当前页
     * @param pageSize     总页数
     * @return
     */
    //分页模糊查询
    public List<Bill> BillList(@Param("productName")String productName,
                               @Param("providerId")int providerId,
                               @Param("IsPayment")int IsPayment,
                               @Param("pageNo")int pageNo,
                               @Param("pageSize")int pageSize);
    /**
     *
     * @param bill  实体类
     * @return
     */
    //添加
    public int addBill(Bill bill);

    /**
     *
     * @param billId
     * @return
     */
    //根据ID查询
    public Bill billById(Integer billId);

    /**
     *
     * @param bill
     * @return
     */
    //修改
    public int modifysave(Bill bill);

    /**
     *
     * @param billid
     * @return
     */
    //删除
    public int delBill(Integer billid);
}
