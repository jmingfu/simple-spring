package com.example.demo.config;

import com.alibaba.druid.support.json.JSONUtils;
import com.example.demo.common.CodeEnum;
import com.example.demo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(JSONUtils.toJSONString(Result.fail(CodeEnum.Forbidden, "请先登录")));
            return false;
        }
        String admin=redisTemplate.opsForValue().get("LOGIN_TOKEN:"+token);
        if(StringUtils.isEmpty(admin)){
            response.getWriter().write(JSONUtils.toJSONString(Result.fail(CodeEnum.Forbidden, "登录已过期")));
            return false;
        }
        return true;
    }
}
