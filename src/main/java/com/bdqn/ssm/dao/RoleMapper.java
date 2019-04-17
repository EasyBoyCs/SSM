package com.bdqn.ssm.dao;

import com.bdqn.ssm.entity.Role;

import java.util.List;

public interface RoleMapper {

    public List<Role> getRoleList();

    public int addRole(Role role);
}
