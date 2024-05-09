package com.buyi.spzx.manager.service.impl;

import com.buyi.spzx.manager.mapper.SysRoleMapper;
import com.buyi.spzx.manager.service.SysRoleService;
import com.buyi.spzx.model.dto.system.SysRoleDto;
import com.buyi.spzx.model.entity.system.SysRole;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    /**
     * 分页查询角色信息
     *
     * @param sysRoleDto dto
     * @param pageNum num
     * @param pageSize size
     * @return pageInfo
     */
    @Override
    public PageInfo<SysRole> findByPage(SysRoleDto sysRoleDto, Integer pageNum, Integer pageSize) {
        // 开启分页功能
        PageHelper.startPage(pageNum, pageSize);
        List<SysRole> sysRoleList = sysRoleMapper.findByPage(sysRoleDto);
        return new PageInfo<>(sysRoleList);
    }
}
