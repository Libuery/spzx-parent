package com.buyi.spzx.manager.mapper;

import com.buyi.spzx.model.entity.system.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserMapper {
    SysUser getByUsername(String userName);
}
