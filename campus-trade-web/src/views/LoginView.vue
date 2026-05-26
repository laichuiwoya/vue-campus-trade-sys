<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const auth = useAuthStore()
const active = ref('login')
const loading = ref(false)
const loginForm = reactive({ username: 'admin', password: '123456' })
const registerForm = reactive({ username: '', password: '', nickname: '' })

async function submitLogin() {
  loading.value = true
  try {
    await auth.login(loginForm)
    ElMessage.success('登录成功')
    router.push('/')
  } finally {
    loading.value = false
  }
}

async function submitRegister() {
  loading.value = true
  try {
    await auth.register(registerForm)
    ElMessage.success('注册成功，请登录')
    active.value = 'login'
    loginForm.username = registerForm.username
    loginForm.password = registerForm.password
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="page auth-page">
    <section class="auth-hero">
      <h1>校园闲置流转更简单</h1>
      <p>发布、搜索、收藏、下单和后台管理已经接入本地 SpringBoot 接口。</p>
    </section>
    <el-card class="auth-card" shadow="never">
      <el-tabs v-model="active">
        <el-tab-pane label="登录" name="login">
          <el-form label-position="top" @submit.prevent>
            <el-form-item label="用户名">
              <el-input v-model="loginForm.username" />
            </el-form-item>
            <el-form-item label="密码">
              <el-input v-model="loginForm.password" type="password" show-password />
            </el-form-item>
            <el-button type="primary" :loading="loading" class="full-btn" @click="submitLogin">登录</el-button>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="注册" name="register">
          <el-form label-position="top" @submit.prevent>
            <el-form-item label="用户名">
              <el-input v-model="registerForm.username" />
            </el-form-item>
            <el-form-item label="昵称">
              <el-input v-model="registerForm.nickname" />
            </el-form-item>
            <el-form-item label="密码">
              <el-input v-model="registerForm.password" type="password" show-password />
            </el-form-item>
            <el-button type="primary" :loading="loading" class="full-btn" @click="submitRegister">注册</el-button>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<style scoped>
.auth-page {
  display: grid;
  grid-template-columns: 1.2fr 420px;
  gap: 26px;
  align-items: center;
  min-height: calc(100vh - 120px);
}

.auth-hero {
  padding: 46px;
  border-radius: 8px;
  color: #fff;
  background: linear-gradient(135deg, #176b87, #64ccc5);
}

.auth-hero h1 {
  margin: 0 0 16px;
  font-size: 44px;
}

.auth-card {
  border-radius: 8px;
}

.full-btn {
  width: 100%;
}

@media (max-width: 860px) {
  .auth-page {
    grid-template-columns: 1fr;
  }

  .auth-hero h1 {
    font-size: 32px;
  }
}
</style>
