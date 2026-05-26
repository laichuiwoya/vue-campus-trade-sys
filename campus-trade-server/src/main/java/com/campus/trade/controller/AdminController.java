package com.campus.trade.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.trade.common.ApiResponse;
import com.campus.trade.entity.Category;
import com.campus.trade.entity.Goods;
import com.campus.trade.entity.Orders;
import com.campus.trade.entity.SysUser;
import com.campus.trade.service.CategoryService;
import com.campus.trade.service.GoodsService;
import com.campus.trade.service.OrdersService;
import com.campus.trade.service.SysUserService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final SysUserService sysUserService;
    private final GoodsService goodsService;
    private final OrdersService ordersService;
    private final CategoryService categoryService;

    public AdminController(SysUserService sysUserService,
                           GoodsService goodsService,
                           OrdersService ordersService,
                           CategoryService categoryService) {
        this.sysUserService = sysUserService;
        this.goodsService = goodsService;
        this.ordersService = ordersService;
        this.categoryService = categoryService;
    }

    @GetMapping("/users")
    public ApiResponse<List<SysUser>> users(@RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<SysUser>()
                .orderByDesc(SysUser::getCreateTime);
        if (StringUtils.hasText(keyword)) {
            wrapper.like(SysUser::getUsername, keyword).or().like(SysUser::getNickname, keyword);
        }
        return ApiResponse.success(sysUserService.list(wrapper));
    }

    @PutMapping("/users/{id}/status")
    public ApiResponse<Boolean> updateUserStatus(@PathVariable Long id, @RequestParam Integer status) {
        SysUser user = sysUserService.getById(id);
        if (user == null) {
            return ApiResponse.fail(404, "User not found");
        }
        user.setStatus(status);
        return ApiResponse.success(sysUserService.updateById(user));
    }

    @GetMapping("/goods")
    public ApiResponse<List<Goods>> goods(@RequestParam(required = false) String status,
                                          @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<Goods>()
                .orderByDesc(Goods::getCreateTime);
        if (StringUtils.hasText(status)) {
            wrapper.eq(Goods::getStatus, status);
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Goods::getTitle, keyword);
        }
        return ApiResponse.success(goodsService.list(wrapper));
    }

    @PutMapping("/goods/{id}/status")
    public ApiResponse<Boolean> updateGoodsStatus(@PathVariable Long id, @RequestParam String status) {
        Goods goods = goodsService.getById(id);
        if (goods == null) {
            return ApiResponse.fail(404, "Goods not found");
        }
        goods.setStatus(status);
        return ApiResponse.success(goodsService.updateById(goods));
    }

    @GetMapping("/orders")
    public ApiResponse<List<Orders>> orders(@RequestParam(required = false) String status) {
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<Orders>()
                .orderByDesc(Orders::getCreateTime);
        if (StringUtils.hasText(status)) {
            wrapper.eq(Orders::getStatus, status);
        }
        return ApiResponse.success(ordersService.list(wrapper));
    }

    @GetMapping("/categories")
    public ApiResponse<List<Category>> categories() {
        return ApiResponse.success(categoryService.list(new LambdaQueryWrapper<Category>()
                .orderByAsc(Category::getSort)));
    }

    @PostMapping("/categories")
    public ApiResponse<Boolean> saveCategory(@RequestBody Category category) {
        return ApiResponse.success(categoryService.save(category));
    }

    @PutMapping("/categories")
    public ApiResponse<Boolean> updateCategory(@RequestBody Category category) {
        return ApiResponse.success(categoryService.updateById(category));
    }

    @DeleteMapping("/categories/{id}")
    public ApiResponse<Boolean> deleteCategory(@PathVariable Long id) {
        return ApiResponse.success(categoryService.removeById(id));
    }
}
