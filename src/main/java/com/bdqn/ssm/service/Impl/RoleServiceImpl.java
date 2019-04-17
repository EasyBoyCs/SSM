package com.bdqn.ssm.service.Impl;

import com.bdqn.ssm.dao.RoleMapper;
import com.bdqn.ssm.entity.Role;
import com.bdqn.ssm.service.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service("roleService")
public class RoleServiceImpl implements RoleService {

    @Resource
    RoleMapper rm;

    public List<Role> getRoleList() {
        return rm.getRoleList();
    }

    public int addRole(Role role) {
        return rm.addRole(role);
    }
}
