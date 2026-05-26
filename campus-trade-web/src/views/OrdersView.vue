<script setup>
import { onMounted, ref } from 'vue'
import { orderApi } from '../api/modules'

const rows = ref([])
const status = ref('')
const loading = ref(false)

async function loadData() {
  loading.value = true
  try {
    const res = await orderApi.list({ status: status.value || undefined })
    rows.value = res.data || []
  } finally {
    loading.value = false
  }
}

onMounted(loadData)
</script>

<template>
  <div class="page">
    <div class="toolbar">
      <div>
        <h2>我的订单</h2>
        <p class="muted">这里只显示当前登录用户作为买家的订单。</p>
      </div>
      <el-space>
        <el-select v-model="status" clearable placeholder="订单状态" style="width: 160px" @change="loadData">
          <el-option label="未支付" value="UNPAID" />
          <el-option label="已支付" value="PAID" />
          <el-option label="已完成" value="FINISHED" />
          <el-option label="已取消" value="CANCELLED" />
        </el-select>
        <el-button @click="loadData">刷新</el-button>
      </el-space>
    </div>
    <el-table v-loading="loading" :data="rows" border>
      <el-table-column prop="orderNo" label="订单号" min-width="180" />
      <el-table-column prop="goodsId" label="商品ID" width="100" />
      <el-table-column prop="sellerId" label="卖家ID" width="100" />
      <el-table-column prop="amount" label="金额" width="120" />
      <el-table-column prop="status" label="状态" width="120" />
      <el-table-column prop="remark" label="备注" />
      <el-table-column prop="createTime" label="创建时间" min-width="170" />
    </el-table>
  </div>
</template>
