package com.epic.followup.interceptor;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminLoginInterceptor implements HandlerInterceptor {

    private static final Integer[] TYPE = new Integer[]{0,1,2};
    private static Set<Integer> typeSet = new HashSet<>();
    private org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

    public AdminLoginInterceptor(){
        super();
        typeSet.addAll(Arrays.asList(TYPE));
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception{

        log.info("[拦截]"+ new Date().toString() + ": ");
        log.info(request.getRequestURI());

        // 验证session 是否存在
        Object obj = request.getSession().getAttribute("userName");
        if(obj == null){
            response.sendRedirect("/aidoctor/admin/index/login.html");
            return false;
        }else {
            Object objTpye = request.getSession().getAttribute("type");
            if (objTpye != null){
                int t = Integer.parseInt(String.valueOf(objTpye));
                return typeSet.contains(t);
            }else {
                return false;
            }
        }
    }
}
