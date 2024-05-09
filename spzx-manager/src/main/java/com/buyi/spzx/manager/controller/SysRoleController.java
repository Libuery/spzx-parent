package com.buyi.spzx.manager.controller;

import com.buyi.spzx.manager.service.SysRoleService;
import com.buyi.spzx.model.dto.system.SysRoleDto;
import com.buyi.spzx.model.entity.system.SysRole;
import com.buyi.spzx.model.vo.common.Result;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "角色管理接口")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;

    @Operation(summary = "分页查询角色信息")
    @PostMapping("/findByPage/{pageNum}/{pageSize}")
    public Result<PageInfo<SysRole>> findByPage(@RequestBody SysRoleDto sysRoleDto,
                                                @PathVariable("pageNum") Integer pageNum,
                                                @PathVariable("pageSize") Integer pageSize) {
        PageInfo<SysRole> pageInfo = sysRoleService.findByPage(sysRoleDto, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
