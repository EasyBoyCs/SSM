package com.bdqn.ssm.interceptor;

import com.bdqn.ssm.entity.User;
import com.bdqn.ssm.util.Constants;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SysInterceptor extends HandlerInterceptorAdapter {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler)throws  Exception{
        HttpSession session=request.getSession();
        User user=(User) session.getAttribute(Constants.USER_SESSION);
        if (user==null){
            response.sendRedirect(request.getContextPath()+"/401.jsp");
            return  false;
        }
        return true;
    }
}
