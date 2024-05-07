package com.buyi.spzx.manager.service;

import com.buyi.spzx.model.dto.system.LoginDto;
import com.buyi.spzx.model.entity.system.SysUser;
import com.buyi.spzx.model.vo.system.LoginVo;

public interface SysUserService {
    LoginVo login(LoginDto loginDto);

    SysUser getUserInfo(String token);
}
