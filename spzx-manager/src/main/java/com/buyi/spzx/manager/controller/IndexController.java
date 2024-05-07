package com.buyi.spzx.manager.controller;

import com.buyi.spzx.manager.service.SysUserService;
import com.buyi.spzx.manager.service.ValidateCodeService;
import com.buyi.spzx.model.dto.system.LoginDto;
import com.buyi.spzx.model.vo.common.Result;
import com.buyi.spzx.model.vo.system.LoginVo;
import com.buyi.spzx.model.vo.system.ValidateCodeVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


@Tag(name = "用户接口")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {

    @Resource
    private SysUserService sysUserService;
    @Resource
    private ValidateCodeService validateCodeService;

    @Operation(summary = "登录接口")
    @PostMapping("/login")
    public Result<LoginVo> login(@RequestBody LoginDto loginDto) {
        LoginVo loginVo = sysUserService.login(loginDto);
        return Result.success(loginVo);
    }

    @Operation(summary = "生成验证码")
    @GetMapping(value = "/generateValidateCode")
    public Result<ValidateCodeVo> generateValidateCode() {
        ValidateCodeVo validateCodeVo = validateCodeService.generateValidateCode();
        return Result.success(validateCodeVo) ;
    }

}
