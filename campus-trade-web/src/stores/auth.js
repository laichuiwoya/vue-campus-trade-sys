import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import { authApi } from '../api/modules'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || '')
  const user = ref(JSON.parse(localStorage.getItem('user') || 'null'))
  const isLogin = computed(() => Boolean(token.value))

  async function login(form) {
    const res = await authApi.login(form)
    token.value = res.data.token
    user.value = {
      userId: res.data.userId,
      username: res.data.username,
      nickname: res.data.nickname,
      role: res.data.role,
    }
    localStorage.setItem('token', token.value)
    localStorage.setItem('user', JSON.stringify(user.value))
  }

  async function register(form) {
    await authApi.register(form)
  }

  function logout() {
    token.value = ''
    user.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  return { token, user, isLogin, login, register, logout }
})
