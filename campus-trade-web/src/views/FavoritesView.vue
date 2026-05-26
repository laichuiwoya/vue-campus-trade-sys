<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { favoriteApi } from '../api/modules'

const rows = ref([])
const loading = ref(false)

async function loadData() {
  loading.value = true
  try {
    const res = await favoriteApi.list()
    rows.value = res.data || []
  } finally {
    loading.value = false
  }
}

async function remove(id) {
  await favoriteApi.remove(id)
  ElMessage.success('已取消收藏')
  await loadData()
}

onMounted(loadData)
</script>

<template>
  <div class="page">
    <div class="toolbar">
      <div>
        <h2>我的收藏</h2>
        <p class="muted">展示当前登录用户收藏过的商品编号。</p>
      </div>
      <el-button @click="loadData">刷新</el-button>
    </div>
    <el-table v-loading="loading" :data="rows" border>
      <el-table-column prop="id" label="收藏ID" width="100" />
      <el-table-column prop="goodsId" label="商品ID" width="120" />
      <el-table-column prop="createTime" label="收藏时间" />
      <el-table-column label="操作" width="140">
        <template #default="{ row }">
          <el-button type="danger" size="small" @click="remove(row.id)">取消收藏</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
