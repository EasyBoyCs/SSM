package com.bdqn.ssm.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bdqn.ssm.entity.Role;
import com.bdqn.ssm.entity.User;
import com.bdqn.ssm.service.RoleService;
import com.bdqn.ssm.service.UserService;
import com.bdqn.ssm.util.Constants;
import com.bdqn.ssm.util.PageSupport;
import com.mysql.jdbc.StringUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/sys/user")
public class UserController extends BaseController {


  //UserService
   @Resource
   private UserService us;

    @Resource
    private RoleService roleService;


    Logger logger=Logger.getLogger(UserController.class);

    //登录
    @RequestMapping(value = "/login",method =RequestMethod.GET)
    public String login(){
        logger.debug("进入登录页面");
        return "login";
    }
    //登录查询
    @RequestMapping(value = "dologin",method = RequestMethod.POST)
    public String doLogin(@RequestParam("userCode")String useCode,
                          @RequestParam("userPassword") String userPassword,
                          HttpSession session,
                          HttpServletRequest request){
        User user=us.getLoginUser(useCode,userPassword);
        if (user!=null){  //登陆成功
            session.setAttribute(Constants.USER_SESSION,user);
            return "frame";
        }else {
            //登录失败后 页面跳转  带出提示信息 放到作用域 error
            request.setAttribute("error","用户名或密码不正确");
            return "login";
        }
    }
    //登陆验证
    @RequestMapping(value = "/ucexist.html",method = RequestMethod.GET)
    @ResponseBody
    public Object userCodeIsExit(@RequestParam String userCode){
        HashMap<String,String> resultMap=new HashMap<String, String>();
        if (StringUtils.isNullOrEmpty(userCode)){
            resultMap.put("userCode","exist");
        }else {
            User user=us.LoginUser(userCode);
            if (null!=user){
                resultMap.put("userCode","exist");
            }else {
                resultMap.put("userCode","noexist");
            }
        }
        return JSONArray.toJSONString(resultMap);
    }
    //分页查询
    @RequestMapping("/userlist.html")
    public String FenY(@RequestParam(value = "queryname",required = false) String queryUserName,
                       @RequestParam(value = "queryUserRole",required = false) String queryUserRole,
                       @RequestParam(value = "pageIndex",required = false) String pageIndex, Model model){
        int _queryUserRole=0;
        List<User>userList=null;
        int pageSize=Constants.pageSize;
        int currentPageNo=1;
        if (queryUserName==null){
            queryUserName="";
        }
        if (queryUserRole!=null && !queryUserRole.equals("")){
            _queryUserRole=Integer.parseInt(queryUserRole);
        }
        if (pageIndex!=null){
            try {
                currentPageNo=Integer.valueOf(pageIndex);
            } catch (NumberFormatException e) {
                return "redirect:/user/error";
            }
        }
        int count=us.getUserCount(queryUserName,_queryUserRole);
        PageSupport page=new PageSupport();
        page.setCurrentPageNo(currentPageNo);
        page.setPageSize(pageSize);
        page.setTotalCount(count);
        int totalPage=page.getTotalPageCount();
        if (currentPageNo<1){
            currentPageNo=1;
        }else if (currentPageNo>totalPage){
            currentPageNo=totalPage;
        }
        userList=us.getUserList(queryUserName,_queryUserRole, (currentPageNo-1)*pageSize,pageSize);
        logger.debug("");
        model.addAttribute("userList",userList);
        List<Role>roleList=null;
        roleList=roleService.getRoleList();
        model.addAttribute("roleList",roleList);
        model.addAttribute("queryname",queryUserName);
        model.addAttribute("queryUserRole",queryUserRole);

        model.addAttribute("totalPageCount",totalPage);
        model.addAttribute("totalCount",count);
        model.addAttribute("currentPageNo",currentPageNo);
        logger.debug(roleList);
        return "userlist";
    }
    //进入增加页面
    @RequestMapping(value = "useradd",method = RequestMethod.GET)
    public String userAdd(@ModelAttribute("user") User user){
        return "useradd";
    }
    //多文件上传
    @RequestMapping(value="useradd",method=RequestMethod.POST)
    public String addUserSave(User user,HttpSession session,HttpServletRequest request,
                              @RequestParam(value ="attachs", required = false) MultipartFile[] attachs){
        String idPicPath = null;
        String workPicPath = null;
        String errorInfo = null;
        boolean flag = true;
        String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
        logger.info("uploadFile path ============== > "+path);
        for(int i = 0;i < attachs.length ;i++){
            MultipartFile attach = attachs[i];
            if(!attach.isEmpty()){
                if(i == 0){
                    errorInfo = "uploadFileError";
                }else if(i == 1){
                    errorInfo = "uploadWpError";
                }
                String oldFileName = attach.getOriginalFilename();//原文件名
                String prefix=FilenameUtils.getExtension(oldFileName);//原文件后缀
                int filesize = 500000;
                if(attach.getSize() >  filesize){//上传大小不得超过 500k
                    request.setAttribute("e", " * 上传大小不得超过 500k");
                    flag = false;
                }else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
                        || prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")){//上传图片格式不正确
                    String fileName = System.currentTimeMillis()+RandomUtils.nextInt(1000000)+"_Personal.jpg";
                    logger.debug("new fileName======== " + attach.getName());
                    File targetFile = new File(path, fileName);
                    if(!targetFile.exists()){
                        targetFile.mkdirs();
                    }
                    //保存
                    try {
                        attach.transferTo(targetFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                        request.setAttribute(errorInfo, " * 上传失败！");
                        flag = false;
                    }
                    if(i == 0){
                        idPicPath = path+File.separator+fileName;
                    }else if(i == 1){
                        workPicPath = path+File.separator+fileName;
                    }
                    logger.debug("idPicPath: " + idPicPath);
                    logger.debug("workPicPath: " + workPicPath);

                }else{
                    request.setAttribute(errorInfo, " * 上传图片格式不正确");
                    flag = false;
                }
            }
        }
        if(flag){
            user.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
            user.setCreationDate(new Date());
            user.setIdPicPath(idPicPath);
            user.setWorkPicPath(workPicPath);
            try {
                if(us.addUserSave(user)>0){
                    return "redirect:/sys/user/userlist.html";
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return "useradd";
    }
    //修改
    @RequestMapping(value = "/usermodify.html",method = RequestMethod.GET)
    public String getUserById(@RequestParam("userId") int userId,Model model){
        User user=us.getUserById(userId);
        model.addAttribute("user",user);
        return "usermodify";
    }
    @RequestMapping(value = "/usermodifysave",method = RequestMethod.POST)
    public String updateUser(User user,HttpSession session){
        user.setModifyBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        user.setModifyDate(new
                    Date());
        if (us.updateUser(user)>0){
            return "redirect:/user/userlist.html";
        }
        return "usermodify";
    }
    //使用json返回数据格式查询用户信息详情, produces={"application/json;charset=UTF-8"}
    //该配置处理返回json对象, 页面中文乱码问题
    @RequestMapping(value = "/view",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public Object view(@RequestParam("id") String id){
        logger.debug("============>"+id);
        String cjson="";
        if (null==id||"".equals(id)){
            return "nodata";
        }else {
            try {
                User user=us.getUserById(Integer.parseInt(id));
                cjson= JSON.toJSONString(user);
                logger.debug("============>"+cjson);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return "failed";
            }
            return cjson;
        }
    }
    //删除
    //删除验证
    @RequestMapping(value = "/delete.html/{id}",method = RequestMethod.GET)
    public String deleteUser(@PathVariable int id){
        if (us.deleteUser(id)>0){
            return "redirect:/user/userlist.html";
        }
        return "userlist";
    }

    @RequestMapping(value = "/deUser",method = RequestMethod.GET)
    @ResponseBody
    public Object deUser(@RequestParam("uid") String uid){
        HashMap<String,String>resultMap=new HashMap<String, String>();
        System.out.println("------------------------->>>>>>"+uid);
        if (us.deleteUser(Integer.parseInt(uid))>0){
            resultMap.put("delResult","true");
        }else {
            resultMap.put("delResult","false");
        }
        return JSONArray.toJSONString(resultMap);
    }

    //跳转到修改密码页
    @RequestMapping(value = "/pwdmodify")
    public String re(){
        return "pwdmodify";
    }
    //密码修改
    @RequestMapping(value = "/pwdModify")
    @ResponseBody
    public Object pwdmodify(@RequestParam(value = "oldpassword")String oldpassword,HttpSession session){
        HashMap<String,String>resultMap=new HashMap<String, String>();
        int id=((User) session.getAttribute(Constants.USER_SESSION)).getId();
        if (us.pwdTorF(id,oldpassword)!=null){
            resultMap.put("result","true");
        }else {
            resultMap.put("result","false");
        }
        return JSONArray.toJSONString(resultMap);
    }
    @RequestMapping(value = "/pwdsave.html",method = RequestMethod.POST)
    public String pwdSave(@RequestParam("newpassword")String password,HttpSession session){
        User user=new User();
        user.setId(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        user.setUserPassword(password);
        if (us.pwdModify(user)>0){
            return "redirect:/sys/user/userlist.html";
        }
        return "pwdmodify";
    }
}
