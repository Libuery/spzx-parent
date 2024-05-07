package com.buyi.spzx.manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.buyi.spzx.common.exception.MyException;
import com.buyi.spzx.manager.mapper.SysUserMapper;
import com.buyi.spzx.manager.service.SysUserService;
import com.buyi.spzx.model.dto.system.LoginDto;
import com.buyi.spzx.model.entity.system.SysUser;
import com.buyi.spzx.model.vo.common.ResultCodeEnum;
import com.buyi.spzx.model.vo.system.LoginVo;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SysUserServiceImpl implements SysUserService {

    private static final String LOGIN_KEY = "user:login:";

    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public LoginVo login(LoginDto loginDto) {
        // 根据用户名查询用户
        SysUser sysUser = sysUserMapper.getByUsername(loginDto.getUserName());
        if (sysUser == null) {
//            throw new RuntimeException("用户名或密码错误");
            throw new MyException(ResultCodeEnum.LOGIN_ERROR);
        }

        // 验证密码是否正确
        String database_password = sysUser.getPassword();
        String input_password = DigestUtils.md5DigestAsHex(loginDto.getPassword().getBytes());
        if (!input_password.equals(database_password)) {
//            throw new RuntimeException("用户名或密码错误");
            throw new MyException(ResultCodeEnum.LOGIN_ERROR);
        }

        // 生成令牌，保存数据到Redis中
        String token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue()
                .set(LOGIN_KEY + token, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);

        // 生成返回对象
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);
        loginVo.setRefresh_token("");

        return loginVo;
    }
}
