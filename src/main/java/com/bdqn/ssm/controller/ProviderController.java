package com.bdqn.ssm.controller;

import com.alibaba.fastjson.JSONArray;
import com.bdqn.ssm.entity.Provider;
import com.bdqn.ssm.entity.User;
import com.bdqn.ssm.service.ProviderService;
import com.bdqn.ssm.util.Constants;
import com.bdqn.ssm.util.PageSupport;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/sys/provider")
public class ProviderController {


    @Resource
    private ProviderService providerService;

    Logger logger=Logger.getLogger(ProviderController.class);

   //分页查询
    @RequestMapping(value = "/list")
    public String proList(@RequestParam(value = "queryProCode",required = false)String proCode,
                          @RequestParam(value = "queryProName",required = false)String proName,
                          @RequestParam(value = "pageIndex",required = false)String pageNo, Model model) {
        List<Provider> providers = null;
        int currentPage=1;
        if (proCode == null) {
            proCode = "";
        }
        if (proName == null){
            proName = "";
        }
        if (pageNo!=null){
            try {
                currentPage=Integer.valueOf(pageNo);
            } catch (NumberFormatException e) {
                return "redirect:/user/error";
            }
        }
        int pageSize=5;
        int count=providerService.proCount(proCode,proName);
        PageSupport page=new PageSupport();
        page.setCurrentPageNo(currentPage);
        page.setPageSize(pageSize);
        page.setTotalCount(count);
        int totalPage=page.getTotalPageCount();
        if (currentPage<1){
            currentPage=1;
        }else if (currentPage>totalPage){
            currentPage=totalPage;
        }
        providers=providerService.proList(proCode,proName,(currentPage-1)*pageSize,pageSize);
        model.addAttribute("queryProCode",proCode);
        model.addAttribute("queryProName",proName);
        model.addAttribute("providerList",providers);
        model.addAttribute("totalPageCount",totalPage);
        model.addAttribute("totalCount",count);
        model.addAttribute("currentPageNo",currentPage);
        return "providerlist";
    }
    //增加
    @RequestMapping(value = "/addPro",method = RequestMethod.GET)
    public String proAdd(@ModelAttribute("provider")Provider provider){
        return "provideradd";
    }
    //增加保存操作
    @RequestMapping(value = "/addProSave",method = RequestMethod.POST)
    public String proAdd(Provider provider,@RequestParam("attachs")MultipartFile[] attachs,
                         HttpSession session, HttpServletRequest request){
        String companyLicPicPath = null;
        String orgCodePicPath = null;
        String errorInfo = null;
        boolean flag = true;
        String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
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
                    } catch (IOException e) {
                        e.printStackTrace();
                        e.printStackTrace();
                        request.setAttribute(errorInfo, " * 上传失败！");
                        flag = false;
                    }
                    if(i == 0){
                        companyLicPicPath = path+File.separator+fileName;
                    }else if(i == 1){
                        orgCodePicPath = path+File.separator+fileName;
                    }

                }else{
                    request.setAttribute(errorInfo, " * 上传图片格式不正确");
                    flag = false;
                }
            }
        }
        if (flag){
            provider.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
            provider.setCreationDate(new Date());
            provider.setCompanyLicPicPath(companyLicPicPath);
            provider.setOrgCodePicPath(orgCodePicPath);
            if (providerService.addPro(provider)>0){
                return "redirect:/sys/provider/list";
            }
        }
        return "provideradd";
    }
    //修改
    @RequestMapping(value = "/modify",method = RequestMethod.GET)
    public String proById(@RequestParam("proId")Integer proId,Model model){
        Provider provider=providerService.proById(proId);
        model.addAttribute("provider",provider);
        return "providermodify";
    }
    //修改保存操作
    @RequestMapping(value = "/modifysave",method = RequestMethod.POST)
    public String modifysave(Provider provider,@RequestParam("attachs") MultipartFile [] attach,
                             HttpServletRequest request,HttpSession session){
        String companyLicPicPath = null;
        String orgCodePicPath = null;
        String errorInfo = null;
        boolean flag = true;
        String pat= request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
        for(int i = 0;i < attach.length ;i++){
            MultipartFile attachs = attach[i];
            if(!attachs.isEmpty()){
                if(i == 0){
                    errorInfo = "uploadFileError";
                }else if(i == 1){
                    errorInfo = "uploadWpError";
                }
                String oldFileName = attachs.getOriginalFilename();//原文件名
                String prefix=FilenameUtils.getExtension(oldFileName);//原文件后缀
                String fileName = System.currentTimeMillis()+RandomUtils.nextInt(1000000)+"_Personal.jpg";
                File targetFile = new File(pat, fileName);
                //保存
                try {
                        attachs.transferTo(targetFile);
                } catch (IOException e) {
                        e.printStackTrace();
                        e.printStackTrace();
                        request.setAttribute(errorInfo, " * 上传失败！");
                        flag = false;
                }
                    if(i == 0){
                        companyLicPicPath = pat+File.separator+fileName;
                    }else if(i == 1){
                        orgCodePicPath = pat+File.separator+fileName;
                    }

                }else{
                    request.setAttribute(errorInfo, " * 上传图片格式不正确");
                    flag = false;
                }
            }
        if (flag){
            provider.setCompanyLicPicPath(companyLicPicPath);
            provider.setOrgCodePicPath(orgCodePicPath);
            provider.setModifyBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
            provider.setModifyDate(new Date());
            if (providerService.proModify(provider)>0){
                return "redirect:/sys/provider/list";
            }
        }
        return "providermodify";
    }
    //点击查看信息
    @RequestMapping(value = "/view")
    public String proView(@RequestParam("proid")Integer proid,Model model){
        Provider provider=providerService.proById(proid);
        model.addAttribute(provider);
        return "providerview";
    }
    //删除
    @RequestMapping(value = "/delPro")
    @ResponseBody
    public Object delPro(@RequestParam("proid")Integer proid){
        HashMap<String,String> resultMap=new HashMap<String, String>();
        logger.debug("===========>>"+proid);
        if (providerService.delPro(proid)>0){
            resultMap.put("delResult","true");
        }else {
            resultMap.put("delResult","false");
        }
        return JSONArray.toJSONString(resultMap);
    }
}
