package com.campus.trade.controller;

import com.campus.trade.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/ping")
    public ApiResponse<Map<String, Object>> ping() {
        Map<String, Object> result = new HashMap<>();
        result.put("project", "campus-trade-server");
        result.put("time", LocalDateTime.now().toString());
        return ApiResponse.success("backend is running", result);
    }
}
