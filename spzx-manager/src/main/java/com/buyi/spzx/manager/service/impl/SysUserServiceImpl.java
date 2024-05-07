package com.buyi.spzx.manager.service.impl;

import cn.hutool.core.util.StrUtil;
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

import static com.buyi.spzx.common.constant.RedisKey.LOGIN_KEY;
import static com.buyi.spzx.common.constant.RedisKey.VALIDATECODE_KEY;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 登录方法
     *
     * @param loginDto 请求参数
     * @return 返回对象
     */
    @Override
    public LoginVo login(LoginDto loginDto) {
        // 校验验证码
        String input_captcha = loginDto.getCaptcha();
        String codeKey = loginDto.getCodeKey();

        // 根据前端传来的key查询redis找验证码
        String key = VALIDATECODE_KEY + codeKey;
        String redis_captcha = redisTemplate.opsForValue().get(key);

        // 校验验证码是否过期
        if (StrUtil.isEmpty(redis_captcha)) {
            throw new MyException(ResultCodeEnum.VALIDATECODE_EXPIRED);
        }
        // 比对验证码 忽略大小写
        if (!StrUtil.equalsIgnoreCase(redis_captcha, input_captcha)) {
            throw new MyException(ResultCodeEnum.VALIDATECODE_ERROR);
        }

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
                .set(LOGIN_KEY + token, JSON.toJSONString(sysUser), 3, TimeUnit.DAYS);

        // 生成返回对象
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);
        loginVo.setRefresh_token("");

        //登录成功，删除redis中存储的验证码
        redisTemplate.delete(key);

        return loginVo;
    }

    /**
     * 根据token获取当期登录用户的信息
     *
     * @param token token
     * @return 用户对象
     */
    @Override
    public SysUser getUserInfo(String token) {
        // 从redis中获取用户信息
        String userJSON = redisTemplate.opsForValue().get(LOGIN_KEY + token);
        return JSON.parseObject(userJSON, SysUser.class);
    }
}
