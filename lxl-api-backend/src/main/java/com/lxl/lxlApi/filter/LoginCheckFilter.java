//package com.lxl.lxlApi.filter;
//
//import com.lxl.lxlApi.common.BaseContext;
//import com.lxl.lxlcommon.model.entity.User;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.util.AntPathMatcher;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//import static com.lxl.lxlApi.constant.UserConstant.USER_LOGIN_STATE;
//
//@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
//@Slf4j
//public class LoginCheckFilter implements Filter {
//
//    //路由匹配器
//    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
//            throws IOException, ServletException {
//
//        HttpServletRequest request=(HttpServletRequest) servletRequest;
//        HttpServletResponse response=(HttpServletResponse) servletResponse;
//
//        //1，获取本次请求的URI路径
//        String requestURI=request.getRequestURI();
//
//        String[] urls=new String[]{
//                "/api/user/login","/api/user/register,/api/v3/api-docs"
//        };
//
//        //2.判断是否需要拦截该web请求
//        boolean check = check(urls, requestURI);
//
//        if (check){
//            filterChain.doFilter(request,response);
//        }
//
//        //判断管理员端的登陆的状态
//        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
//        if(user!=null){
//            log.info("线程id为：{}",Thread.currentThread().getId());
//            log.info("用户id为: {}",user.getId());
//            //通过线程传递id值
//            BaseContext.setCurrentId(user.getId());
//            filterChain.doFilter(request,response);
//            return;
//        }
//    }
//
//    public boolean check(String[] urls,String requestURI){
//        for (String url : urls) {
//            boolean match = PATH_MATCHER.match(url, requestURI);
//
//            if(match){
//                return true;
//            }
//        }
//        return false;
//    }
//}
