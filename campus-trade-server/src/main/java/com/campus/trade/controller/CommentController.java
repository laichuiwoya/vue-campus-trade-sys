package com.campus.trade.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.trade.common.ApiResponse;
import com.campus.trade.entity.Comment;
import com.campus.trade.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ApiResponse<List<Comment>> listByGoods(@RequestParam Long goodsId) {
        return ApiResponse.success(commentService.list(new LambdaQueryWrapper<Comment>()
                .eq(Comment::getGoodsId, goodsId)
                .orderByAsc(Comment::getCreateTime)));
    }

    @PostMapping
    public ApiResponse<Boolean> save(@RequestBody Comment comment, @RequestAttribute("userId") Long userId) {
        comment.setUserId(userId);
        return ApiResponse.success(commentService.save(comment));
    }
}

