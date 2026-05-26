package com.campus.trade.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.trade.common.ApiResponse;
import com.campus.trade.entity.Goods;
import com.campus.trade.entity.Orders;
import com.campus.trade.service.GoodsService;
import com.campus.trade.service.OrdersService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    private final OrdersService ordersService;
    private final GoodsService goodsService;

    public OrdersController(OrdersService ordersService, GoodsService goodsService) {
        this.ordersService = ordersService;
        this.goodsService = goodsService;
    }

    @GetMapping
    public ApiResponse<List<Orders>> listByBuyer(@RequestAttribute("userId") Long userId,
                                                  @RequestParam(required = false) String status) {
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<Orders>()
                .eq(Orders::getBuyerId, userId)
                .orderByDesc(Orders::getCreateTime);
        if (StringUtils.hasText(status)) {
            wrapper.eq(Orders::getStatus, status);
        }
        return ApiResponse.success(ordersService.list(wrapper));
    }

    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<Boolean> save(@RequestBody Orders orders, @RequestAttribute("userId") Long userId) {
        Goods goods = goodsService.getById(orders.getGoodsId());
        if (goods == null) {
            return ApiResponse.fail(404, "Goods not found");
        }
        if (!"ON_SALE".equals(goods.getStatus())) {
            return ApiResponse.fail(400, "Goods is not on sale");
        }
        if (goods.getQuantity() == null || goods.getQuantity() <= 0) {
            goods.setStatus("SOLD");
            goodsService.updateById(goods);
            return ApiResponse.fail(400, "Goods stock is empty");
        }
        if (goods.getUserId().equals(userId)) {
            return ApiResponse.fail(400, "You cannot buy your own goods");
        }

        orders.setBuyerId(userId);
        orders.setSellerId(goods.getUserId());
        orders.setAmount(goods.getPrice());
        orders.setStatus("UNPAID");
        orders.setOrderNo(generateOrderNo());
        boolean saved = ordersService.save(orders);

        if (saved) {
            int leftQuantity = goods.getQuantity() - 1;
            goods.setQuantity(leftQuantity);
            if (leftQuantity <= 0) {
                goods.setStatus("SOLD");
            } else {
                goods.setStatus("ON_SALE");
            }
            goodsService.updateById(goods);
        }
        return ApiResponse.success(saved);
    }

    @PutMapping
    public ApiResponse<Boolean> update(@RequestBody Orders orders, @RequestAttribute("userId") Long userId) {
        Orders old = ordersService.getById(orders.getId());
        if (old == null) {
            return ApiResponse.fail(404, "Order not found");
        }
        if (!old.getBuyerId().equals(userId) && !old.getSellerId().equals(userId)) {
            return ApiResponse.fail(403, "No permission to update this order");
        }
        orders.setBuyerId(old.getBuyerId());
        orders.setSellerId(old.getSellerId());
        orders.setGoodsId(old.getGoodsId());
        orders.setAmount(old.getAmount());
        orders.setOrderNo(old.getOrderNo());
        return ApiResponse.success(ordersService.updateById(orders));
    }

    private String generateOrderNo() {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int random = ThreadLocalRandom.current().nextInt(1000, 10000);
        return "ORD" + time + random;
    }
}
