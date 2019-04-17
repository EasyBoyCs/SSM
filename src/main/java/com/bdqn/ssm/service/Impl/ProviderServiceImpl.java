package com.bdqn.ssm.service.Impl;

import com.bdqn.ssm.dao.ProviderMapper;
import com.bdqn.ssm.entity.Provider;
import com.bdqn.ssm.service.ProviderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service("providerService")
public class ProviderServiceImpl implements ProviderService {

    @Resource
    private ProviderMapper providerMapper;

   //查询所有
    public List<Provider> ProviderList() {
        return providerMapper.ProviderList();
    }
   //获取总记录数
    public int proCount(String proCode, String proName) {
        return providerMapper.proCount(proCode, proName);
    }
    //分页查询
    public List<Provider> proList(String proCode, String proName, Integer pageNo, Integer pageSize) {
        return providerMapper.proList(proCode, proName, pageNo, pageSize);
    }
    //增加
    public int addPro(Provider provider) {
        return providerMapper.addPro(provider);
    }
    //根据ID查询
    public Provider proById(Integer proId) {
        return providerMapper.proById(proId);
    }
    //修改
    public int proModify(Provider provider) {
        return providerMapper.proModify(provider);
    }
    //删除
    public int delPro(Integer proid) {
        return providerMapper.delPro(proid);
    }

}
