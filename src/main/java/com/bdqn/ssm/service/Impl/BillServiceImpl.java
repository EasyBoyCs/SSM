package com.bdqn.ssm.service.Impl;

import com.bdqn.ssm.dao.BillMaper;
import com.bdqn.ssm.entity.Bill;
import com.bdqn.ssm.service.BillService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service("billService")
public class BillServiceImpl implements BillService {

    @Resource
    private BillMaper billMaper;

   //获取总记录数
    public int count(String productName, int providerId) {
        return billMaper.count(productName,providerId);
    }
   //分页模糊查询
    public List<Bill> BillList(String productName, int providerId, int IsPayment, int pageNo, int pageSize) {
        return billMaper.BillList(productName, providerId, IsPayment, pageNo, pageSize);
    }
    //添加
    public int addBill(Bill bill) {
        return billMaper.addBill(bill);
    }
    //根据ID查询
    public Bill billById(Integer billId) {
        return billMaper.billById(billId);
    }
    //修改
    public int modifysave(Bill bill) {
        return billMaper.modifysave(bill);
    }
    //删除
    public int delBill(Integer billid) {
        return billMaper.delBill(billid);
    }
}
