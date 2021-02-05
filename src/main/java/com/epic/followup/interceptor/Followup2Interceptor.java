package com.epic.followup.interceptor;

import com.epic.followup.service.followup2.BaseUserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author : zx
 * @version V1.0
 */
public class Followup2Interceptor implements HandlerInterceptor {

    private org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());
    private BaseUserService baseUserService;

    @Autowired
    public Followup2Interceptor(BaseUserService baseUserService){
        this.baseUserService = baseUserService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception{

        log.info("[拦截]"+ new Date().toString() + ": ");
        log.info(request.getRequestURI());

        // 验证session 是否存在
        if (baseUserService.findBySessionId(request.getHeader("sessionId"))!= null){
            return true;
        }else {
            response.sendError(401, "登录失效");
        }
        return false;
    }
}
