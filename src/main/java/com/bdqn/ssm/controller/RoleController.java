package com.bdqn.ssm.controller;

import com.bdqn.ssm.entity.Role;
import com.bdqn.ssm.entity.User;
import com.bdqn.ssm.service.RoleService;
import com.bdqn.ssm.util.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/sys/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @RequestMapping(value = "/list")
    public String allRole(Model model){
        List<Role>roleList=roleService.getRoleList();
        model.addAttribute("roleList",roleList);
     return "rolelist";
  }

  @RequestMapping(value = "/add")
    public String addRole(@ModelAttribute("role")Role role){
        return "roleadd";
  }
  @RequestMapping(value = "roleSave")
    public String roleSave(Role role, HttpSession session){
        role.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        role.setCreationDate(new Date());
        if (roleService.addRole(role)>0){
            return "/sys/role/list";
        }
        return "roleadd";
  }
}
