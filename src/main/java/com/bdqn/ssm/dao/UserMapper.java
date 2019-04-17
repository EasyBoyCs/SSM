package com.bdqn.ssm.dao;

import com.bdqn.ssm.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {

    //通过用户名与密码查询
    /**
     *
     * @param userCode     用户编码
     * @return
     */
    public User getLoginUser(@Param("userCode") String userCode);
    //获取总记录数
    /**
     *
     * @param userName 用户名称
     * @param userRole 用户角色
     * @return
     */
    public int getUserCount(@Param("userName") String userName,@Param("userRole") int userRole);
    //分页查询
    /**
     *
     * @param userName 用户名称
     * @param userRole 用户角色
     * @param pageSize 页面大小
     * @return
     */
    public List<User> getUserList(@Param("userName") String userName,
                                  @Param("userRole") int userRole,
                                  @Param("currentPageNo") int currentPageNo,
                                  @Param("pageSize") int pageSize);
    //添加用户信息
    /**
     *
     * @param user User实体类
     * @return
     */
    public int addUserSave(User user);
    //根据id查询
    /**
     *
     * @param userId User表id
     * @return
     */
    public User getUserById(int userId);

    //修改
    /**
     *
     * @param user User实体类对象
     * @return
     */
    public int updateUser(User user);
    //删除
    /**
     *
     * @param userid
     * @return
     */
    public int deleteUser(int userid);

    /**
     *
     * @param userCode
     * @return
     */
    public User LoginUser(String userCode);

    /**
     *
     * @param id
     * @param passWord
     * @return
     */
    //检验密码是否正确
    public User pwdTorF(@Param("id") Integer id,@Param("userPassword") String passWord);

    /**
     *
     * @param user
     * @return
     */
    //修改密码
    public int pwdModify(User user);
}
