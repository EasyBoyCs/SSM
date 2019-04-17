package com.bdqn.ssm.service;

import com.bdqn.ssm.entity.Provider;

import java.util.List;

public interface ProviderService {

    //查询所有
    public List<Provider> ProviderList();
    //获取总记录数
    public int proCount(String proCode,String proName);
    //分页查询
    public List<Provider> proList(String proCode,String proName,Integer pageNo,Integer pageSize);
    //增加
    public int addPro(Provider provider);
    //根据ID查询
    public Provider proById(Integer proId);
    //修改
    public int proModify(Provider provider);
    //删除
    public int delPro(Integer proid);
}
