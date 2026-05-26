<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { adminApi } from '../api/modules'

const active = ref('users')
const users = ref([])
const goods = ref([])
const orders = ref([])
const categories = ref([])
const loading = ref(false)
const categoryForm = reactive({ id: null, name: '', sort: 0, status: 1 })

async function loadAll() {
  loading.value = true
  try {
    const [u, g, o, c] = await Promise.all([
      adminApi.users(),
      adminApi.goods(),
      adminApi.orders(),
      adminApi.categories(),
    ])
    users.value = u.data || []
    goods.value = g.data || []
    orders.value = o.data || []
    categories.value = c.data || []
  } finally {
    loading.value = false
  }
}

async function setUserStatus(row, status) {
  await adminApi.updateUserStatus(row.id, status)
  ElMessage.success('用户状态已更新')
  await loadAll()
}

async function setGoodsStatus(row, status) {
  await adminApi.updateGoodsStatus(row.id, status)
  ElMessage.success('商品状态已更新')
  await loadAll()
}

function editCategory(row) {
  Object.assign(categoryForm, row)
}

async function saveCategory() {
  if (categoryForm.id) {
    await adminApi.updateCategory(categoryForm)
  } else {
    await adminApi.createCategory(categoryForm)
  }
  ElMessage.success('分类已保存')
  Object.assign(categoryForm, { id: null, name: '', sort: 0, status: 1 })
  await loadAll()
}

async function removeCategory(id) {
  await adminApi.removeCategory(id)
  ElMessage.success('分类已删除')
  await loadAll()
}

onMounted(loadAll)
</script>

<template>
  <div class="page">
    <div class="toolbar">
      <div>
        <h2>后台管理</h2>
        <p class="muted">管理员可管理用户、商品、订单和分类。</p>
      </div>
      <el-button @click="loadAll">刷新</el-button>
    </div>
    <el-card shadow="never">
      <el-tabs v-model="active" v-loading="loading">
        <el-tab-pane label="用户管理" name="users">
          <el-table :data="users" border>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="username" label="用户名" />
            <el-table-column prop="nickname" label="昵称" />
            <el-table-column prop="role" label="角色" width="100" />
            <el-table-column prop="status" label="状态" width="100" />
            <el-table-column label="操作" width="180">
              <template #default="{ row }">
                <el-button size="small" @click="setUserStatus(row, 1)">启用</el-button>
                <el-button size="small" type="danger" @click="setUserStatus(row, 0)">禁用</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="商品管理" name="goods">
          <el-table :data="goods" border>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="title" label="标题" />
            <el-table-column prop="price" label="价格" width="100" />
            <el-table-column prop="status" label="状态" width="120" />
            <el-table-column label="操作" width="220">
              <template #default="{ row }">
                <el-button size="small" @click="setGoodsStatus(row, 'ON_SALE')">上架</el-button>
                <el-button size="small" type="warning" @click="setGoodsStatus(row, 'OFF_SHELF')">下架</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="订单管理" name="orders">
          <el-table :data="orders" border>
            <el-table-column prop="orderNo" label="订单号" min-width="180" />
            <el-table-column prop="buyerId" label="买家" width="90" />
            <el-table-column prop="sellerId" label="卖家" width="90" />
            <el-table-column prop="amount" label="金额" width="100" />
            <el-table-column prop="status" label="状态" width="120" />
            <el-table-column prop="createTime" label="创建时间" min-width="170" />
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="分类管理" name="categories">
          <el-form inline :model="categoryForm">
            <el-form-item label="名称">
              <el-input v-model="categoryForm.name" />
            </el-form-item>
            <el-form-item label="排序">
              <el-input-number v-model="categoryForm.sort" />
            </el-form-item>
            <el-form-item label="状态">
              <el-select v-model="categoryForm.status" style="width: 100px">
                <el-option label="启用" :value="1" />
                <el-option label="禁用" :value="0" />
              </el-select>
            </el-form-item>
            <el-button type="primary" @click="saveCategory">保存分类</el-button>
          </el-form>
          <el-table :data="categories" border>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="name" label="名称" />
            <el-table-column prop="sort" label="排序" width="100" />
            <el-table-column prop="status" label="状态" width="100" />
            <el-table-column label="操作" width="180">
              <template #default="{ row }">
                <el-button size="small" @click="editCategory(row)">编辑</el-button>
                <el-button size="small" type="danger" @click="removeCategory(row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>
