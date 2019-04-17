package com.bdqn.ssm.service;

import com.bdqn.ssm.entity.Bill;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BillService {

    //获取总记录数
    public int count(String productName,int providerId);
    //分页模糊查询
    public List<Bill> BillList(String productName,int providerId,int IsPayment,int pageNo,int pageSize);
    //添加
    public int addBill(Bill bill);
    //根据ID查询
    public Bill billById(Integer billId);
    //修改
    public int modifysave(Bill bill);
    //删除
    public int delBill(Integer billid);
}
