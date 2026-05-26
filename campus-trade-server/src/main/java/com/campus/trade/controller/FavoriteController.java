package com.campus.trade.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.trade.common.ApiResponse;
import com.campus.trade.entity.Favorite;
import com.campus.trade.service.FavoriteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public ApiResponse<List<Favorite>> listByUser(@RequestAttribute("userId") Long userId) {
        return ApiResponse.success(favoriteService.list(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId)
                .orderByDesc(Favorite::getCreateTime)));
    }

    @PostMapping
    public ApiResponse<Boolean> save(@RequestBody Favorite favorite, @RequestAttribute("userId") Long userId) {
        favorite.setUserId(userId);
        long count = favoriteService.count(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId)
                .eq(Favorite::getGoodsId, favorite.getGoodsId()));
        if (count > 0) {
            return ApiResponse.fail(400, "Goods already favorited");
        }
        return ApiResponse.success(favoriteService.save(favorite));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> delete(@PathVariable Long id, @RequestAttribute("userId") Long userId) {
        Favorite favorite = favoriteService.getById(id);
        if (favorite == null) {
            return ApiResponse.fail(404, "Favorite not found");
        }
        if (!favorite.getUserId().equals(userId)) {
            return ApiResponse.fail(403, "You can only delete your own favorite");
        }
        return ApiResponse.success(favoriteService.removeById(id));
    }
}
