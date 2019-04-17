package com.bdqn.ssm.service.Impl;

import com.bdqn.ssm.dao.UserMapper;
import com.bdqn.ssm.entity.User;
import com.bdqn.ssm.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper um;

    public User getLoginUser(String userCode, String userPassword) {
        User user=um.getLoginUser(userCode);
        if (user!=null){
            if (user.getUserPassword().equals(userPassword)){
                return user;
            }
        }
        return null;
    }

    public int getUserCount(String userName, int userRole) {
        return um.getUserCount(userName,userRole);
    }

    public List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize) {
        return um.getUserList(userName,userRole,currentPageNo,pageSize);
    }

    public int addUserSave(User user) {
        return um.addUserSave(user);
    }

    public User getUserById(int userId) {
        return um.getUserById(userId);
    }

    public int updateUser(User user) {
        return um.updateUser(user);
    }

    public int deleteUser(int userid) {
        return um.deleteUser(userid);
    }

    public User LoginUser(String userCode) {
        return um.LoginUser(userCode);
    }

    public User pwdTorF(Integer id,String passWord) {
        return um.pwdTorF(id,passWord);
    }

    public int pwdModify(User user) {
        return um.pwdModify(user);
    }
}
