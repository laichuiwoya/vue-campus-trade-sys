package com.campus.trade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.trade.entity.OrderLog;
import com.campus.trade.mapper.OrderLogMapper;
import com.campus.trade.service.OrderLogService;
import org.springframework.stereotype.Service;

@Service
public class OrderLogServiceImpl extends ServiceImpl<OrderLogMapper, OrderLog> implements OrderLogService {
}
