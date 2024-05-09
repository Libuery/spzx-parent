package com.buyi.spzx.manager.mapper;

import com.buyi.spzx.model.dto.system.SysRoleDto;
import com.buyi.spzx.model.entity.system.SysRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysRoleMapper {
    List<SysRole> findByPage(SysRoleDto sysRoleDto);
}
