package com.bdqn.ssm.service;

import com.bdqn.ssm.entity.User;

import java.util.List;

public interface UserService {

    public User getLoginUser(String userCode, String userPassword);

    public int getUserCount(String userName,int userRole);

    public List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize);

    public int addUserSave(User user);

    public User getUserById(int userId);

    public int updateUser(User user);

    public int deleteUser(int userid);

    public User LoginUser(String userCode);

    //检验密码是否正确
    public User pwdTorF(Integer id,String passWord);

    //修改密码
    public int pwdModify(User user);
}
