package com.campus.trade.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.trade.common.ApiResponse;
import com.campus.trade.entity.Category;
import com.campus.trade.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ApiResponse<List<Category>> list() {
        List<Category> list = categoryService.list(new LambdaQueryWrapper<Category>()
                .eq(Category::getStatus, 1)
                .orderByAsc(Category::getSort));
        return ApiResponse.success(list);
    }

    @PostMapping
    public ApiResponse<Boolean> save(@RequestBody Category category) {
        return ApiResponse.success(categoryService.save(category));
    }

    @PutMapping
    public ApiResponse<Boolean> update(@RequestBody Category category) {
        return ApiResponse.success(categoryService.updateById(category));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        return ApiResponse.success(categoryService.removeById(id));
    }
}
