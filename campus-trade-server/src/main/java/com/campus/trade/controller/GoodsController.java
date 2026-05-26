package com.campus.trade.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.trade.common.ApiResponse;
import com.campus.trade.entity.Goods;
import com.campus.trade.service.GoodsService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goods")
public class GoodsController {

    private final GoodsService goodsService;

    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @GetMapping
    public ApiResponse<List<Goods>> list(@RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<Goods>()
                .eq(Goods::getStatus, "ON_SALE")
                .orderByDesc(Goods::getCreateTime);
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Goods::getTitle, keyword);
        }
        return ApiResponse.success(goodsService.list(wrapper));
    }

    @GetMapping("/{id}")
    public ApiResponse<Goods> detail(@PathVariable Long id) {
        return ApiResponse.success(goodsService.getById(id));
    }

    @PostMapping
    public ApiResponse<Boolean> save(@RequestBody Goods goods, @RequestAttribute("userId") Long userId) {
        goods.setUserId(userId);
        if (goods.getQuantity() == null || goods.getQuantity() < 1) {
            return ApiResponse.fail(400, "Quantity must be greater than 0");
        }
        if (!StringUtils.hasText(goods.getStatus())) {
            goods.setStatus("ON_SALE");
        }
        return ApiResponse.success(goodsService.save(goods));
    }

    @PutMapping
    public ApiResponse<Boolean> update(@RequestBody Goods goods, @RequestAttribute("userId") Long userId) {
        Goods old = goodsService.getById(goods.getId());
        if (old == null) {
            return ApiResponse.fail(404, "Goods not found");
        }
        if (!old.getUserId().equals(userId)) {
            return ApiResponse.fail(403, "You can only update your own goods");
        }
        goods.setUserId(old.getUserId());
        return ApiResponse.success(goodsService.updateById(goods));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> delete(@PathVariable Long id, @RequestAttribute("userId") Long userId) {
        Goods old = goodsService.getById(id);
        if (old == null) {
            return ApiResponse.fail(404, "Goods not found");
        }
        if (!old.getUserId().equals(userId)) {
            return ApiResponse.fail(403, "You can only delete your own goods");
        }
        return ApiResponse.success(goodsService.removeById(id));
    }
}
