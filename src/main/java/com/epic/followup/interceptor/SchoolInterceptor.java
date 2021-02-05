package com.epic.followup.interceptor;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SchoolInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        JSONObject ret = new JSONObject();
        OutputStream os = response.getOutputStream();
        do {
            HttpSession session = request.getSession();
            Integer userType = (Integer) session.getAttribute("userType");

            // 没有登陆直接返回拒绝处理
            if (session.getAttribute("userId") == null || userType == null) {
                ret.put("msg", "Login fisrt, please!");
                ret.put("statusCode", 401);
                response.setStatus(401);
                break;
            }
            
            // 登陆了，但是没有权限
            String uri = request.getRequestURI();
            if (uri.startsWith("/school/scale/template/")) {
                // 只有心理咨询中心管理员/主任能访问 “模板接口”
                if (userType != 1 && userType != 2) { // TODO 判断条件待改
                    ret.put("msg", "permission denied!");
                    ret.put("statusCode", 400);
                    response.setStatus(400);
                    break;
                }
            } else if (uri.startsWith("/school/scale/evaluation/")) {
                // 只有心理咨询中心的管理员/主任/咨询老师 “评测任务”
                if (userType != 1 && userType != 2 && userType != 3) { // TODO 判断条件待改
                    ret.put("msg", "permission denied!");
                    ret.put("statusCode", 400);
                    response.setStatus(400);
                    break;
                }
            }

            // 继续访问
            return super.preHandle(request, response, handler);
        } while (false);

        // 未登陆或者权限存在问题，终止访问。
        JSONObject.writeJSONString(os, ret);
        os.close();
        return false;
    }
}
