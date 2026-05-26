<script setup>
import { useRouter } from 'vue-router'
import { useAuthStore } from './stores/auth'

const router = useRouter()
const auth = useAuthStore()

function logout() {
  auth.logout()
  router.push('/login')
}
</script>

<template>
  <el-container class="app-shell">
    <el-header class="topbar">
      <div class="brand" @click="router.push('/')">
        <span class="brand-mark">CT</span>
        <span>校园二手交易平台</span>
      </div>
      <el-menu class="nav" mode="horizontal" router :ellipsis="false">
        <el-menu-item index="/">商品市场</el-menu-item>
        <el-menu-item index="/publish" v-if="auth.isLogin">发布商品</el-menu-item>
        <el-menu-item index="/favorites" v-if="auth.isLogin">我的收藏</el-menu-item>
        <el-menu-item index="/orders" v-if="auth.isLogin">我的订单</el-menu-item>
        <el-menu-item index="/admin" v-if="auth.user?.role === 'ADMIN'">后台管理</el-menu-item>
      </el-menu>
      <div class="user-zone">
        <template v-if="auth.isLogin">
          <el-tag effect="plain">{{ auth.user.nickname || auth.user.username }}</el-tag>
          <el-button @click="logout">退出</el-button>
        </template>
        <el-button v-else type="primary" @click="router.push('/login')">登录</el-button>
      </div>
    </el-header>
    <el-main>
      <router-view />
    </el-main>
  </el-container>
</template>
