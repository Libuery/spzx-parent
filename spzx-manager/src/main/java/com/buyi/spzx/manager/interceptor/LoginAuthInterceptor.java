package com.buyi.spzx.manager.interceptor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.buyi.spzx.model.entity.system.SysUser;
import com.buyi.spzx.model.vo.common.Result;
import com.buyi.spzx.model.vo.common.ResultCodeEnum;
import com.buyi.spzx.utils.AuthContextUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import static com.buyi.spzx.common.constant.RedisKey.LOGIN_KEY;

@Component
public class LoginAuthInterceptor implements HandlerInterceptor {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.如果是跨域预检请求，直接放行
        if ("OPTIONS".equals(request.getMethod())){
            return true;
        }
        // 2.如果token为空，返回错误提示
        String token = request.getHeader("token");
        if (StrUtil.isEmpty(token)) {
            responseNoLoginInfo(response);
            return false;
        }
        // 3.拿着token查询redis
        String key = LOGIN_KEY + token;
        String userJSON = redisTemplate.opsForValue().get(key);
        // 4.如果redis查不到数据，返回错误信息
        if (StrUtil.isEmpty(userJSON)) {
            responseNoLoginInfo(response);
            return false;
        }
        // 5.把用户信息放到ThreadLocal中
        SysUser sysUser = JSON.parseObject(userJSON, SysUser.class);
        AuthContextUtil.set(sysUser);
        // 延长redis用户信息数据的过期时间
        redisTemplate.expire(key, 30, TimeUnit.MINUTES);
        // 6.放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AuthContextUtil.remove();
    }


    /**
     * 响应208的状态码给前端
     *
     * @param response res
     */
    private void responseNoLoginInfo(HttpServletResponse response) {
        Result<String> result = Result.error(ResultCodeEnum.LOGIN_AUTH);
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(JSON.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
