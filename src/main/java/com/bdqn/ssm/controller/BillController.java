package com.bdqn.ssm.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bdqn.ssm.entity.Bill;
import com.bdqn.ssm.entity.Provider;
import com.bdqn.ssm.entity.Role;
import com.bdqn.ssm.entity.User;
import com.bdqn.ssm.service.BillService;
import com.bdqn.ssm.service.ProviderService;
import com.bdqn.ssm.util.Constants;
import com.bdqn.ssm.util.PageSupport;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/sys/bill")
public class BillController {

    @Resource
    private BillService billService;

    @Resource
    private ProviderService providerService;

    Logger logger=Logger.getLogger(BillController.class);

    //分页查询
    @RequestMapping(value = "/list")
    public String list(@RequestParam(value = "queryProductName",required = false)String productName,
                       @RequestParam(value = "queryProviderId",required = false)String providerId,
                       @RequestParam(value = "queryIsPayment",required = false)String isPayment,
                       @RequestParam(value = "pageIndex",required = false)Integer pageNo, Model model){
        int isPay=0;
        int proId=0;
        List<Bill> bills=null;
        int pageSize=5; //获取总页数
        if (productName==null){
            productName="";
        }
        logger.debug("=======isPay==>"+isPayment);
        if (isPayment!=null || "".equals(isPayment)){
            isPay=Integer.parseInt(isPayment);
            logger.debug("======isPay==>"+isPay);
        }
        logger.debug("========providerId==>"+providerId);
        if (providerId!=null || "".equals(providerId)){
            proId=Integer.parseInt(providerId);
        }
        int currentPage=1;
        if (pageNo!=null){
            try {
                currentPage=Integer.valueOf(pageNo);
            } catch (NumberFormatException e) {
                return "redirect:/user/error";
            }
        }
        int count=billService.count(productName,proId);  //通过查询获取总记录数
        logger.debug("========总记录数"+count);
        PageSupport page=new PageSupport();
        page.setPageSize(pageSize);  //总页数
        page.setTotalCount(count); //总记录数据
        page.setCurrentPageNo(currentPage); //当前页
        int totalPage=page.getTotalPageCount();
        if (currentPage<1){
            currentPage=1;
        }else if (currentPage>totalPage){
            currentPage=totalPage;
        }
        bills=billService.BillList(productName,proId,isPay,(currentPage-1)*pageSize,pageSize);
        model.addAttribute("billList",bills);
        List<Provider>providers=null;
        providers=providerService.ProviderList();
        model.addAttribute("providerList",providers);  //供应商表查询结果放到model
        model.addAttribute("queryProductName",productName);
        model.addAttribute("queryProviderId",providerId);
        model.addAttribute("queryIsPayment",isPayment);
        model.addAttribute("totalPageCount",totalPage);
        model.addAttribute("totalCount",count); //总记录数
        model.addAttribute("currentPageNo",currentPage);  //当前页
       return "billlist";
    }

    //添加
    @RequestMapping(value = "/add.html",method = RequestMethod.GET)
    public String addBill(@ModelAttribute("bill")Bill bill){
        return "billadd";
    }

    //AJXS
    @RequestMapping(value = "/providers",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public Object prolist(){
        List<Provider> providers=providerService.ProviderList();
        return JSONArray.toJSONString(providers);
    }
    //添加保存
    @RequestMapping(value = "addsave",method = RequestMethod.POST)
    public String addBillSave(Bill bill, HttpSession session){
        bill.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        bill.setCreationDate(new Date());
        int save=billService.addBill(bill);
        if (save>0){
            return "redirect:/sys/bill/list";
        }
        return "billadd";
    }

    //修改
    @RequestMapping(value = "/billById",method = RequestMethod.GET)
    public String billById(@RequestParam("billId")Integer billId,Model model){
        Bill bill=billService.billById(billId);
        model.addAttribute("bill",bill);
        List<Provider>providers=providerService.ProviderList();
        model.addAttribute("providerList",providers);
        return "billmodify";
    }
    //修改保存操作
    @RequestMapping(value = "/modifysave",method = RequestMethod.POST)
    public String billSave(Bill bill,HttpSession session){
        bill.setModifyBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        bill.setModifyDate(new Date());
        int save=billService.modifysave(bill);
        if (save>0){
            return "redirect:/sys/bill/list";
        }
        return "billmodify";
    }
    //点击显示信息
    @RequestMapping(value = "/view",method = RequestMethod.GET)
    public String billview(@RequestParam("billid")Integer billid,Model model){
        Bill bill=billService.billById(billid);
        model.addAttribute("bill",bill);
        return "billview";
    }
    //删除
    @RequestMapping(value = "/del")
    @ResponseBody
    public Object delBill(@RequestParam("billid")Integer billid){
        HashMap<String,String> resultMap=new HashMap<String, String>();
        logger.debug("===========>"+billid);
        if (billService.delBill(billid)>0){
            resultMap.put("delResult","true");
        }else {
            resultMap.put("delResult","false");
        }
        return JSONArray.toJSONString(resultMap);
    }
}
