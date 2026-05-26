package com.campus.trade.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.trade.common.ApiResponse;
import com.campus.trade.entity.SysUser;
import com.campus.trade.service.SysUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class SysUserController {

    private final SysUserService sysUserService;

    public SysUserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @GetMapping
    public ApiResponse<List<SysUser>> list() {
        List<SysUser> list = sysUserService.list(new LambdaQueryWrapper<SysUser>().orderByDesc(SysUser::getId));
        return ApiResponse.success(list);
    }

    @GetMapping("/{id}")
    public ApiResponse<SysUser> detail(@PathVariable Long id) {
        return ApiResponse.success(sysUserService.getById(id));
    }

    @PostMapping
    public ApiResponse<Boolean> save(@RequestBody SysUser sysUser) {
        return ApiResponse.success(sysUserService.save(sysUser));
    }

    @PutMapping
    public ApiResponse<Boolean> update(@RequestBody SysUser sysUser) {
        return ApiResponse.success(sysUserService.updateById(sysUser));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        return ApiResponse.success(sysUserService.removeById(id));
    }
}
