import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import MarketView from '../views/MarketView.vue'
import LoginView from '../views/LoginView.vue'
import PublishView from '../views/PublishView.vue'
import FavoritesView from '../views/FavoritesView.vue'
import OrdersView from '../views/OrdersView.vue'
import AdminView from '../views/AdminView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', component: MarketView },
    { path: '/login', component: LoginView },
    { path: '/publish', component: PublishView, meta: { auth: true } },
    { path: '/favorites', component: FavoritesView, meta: { auth: true } },
    { path: '/orders', component: OrdersView, meta: { auth: true } },
    { path: '/admin', component: AdminView, meta: { auth: true, admin: true } },
  ],
})

router.beforeEach((to) => {
  const auth = useAuthStore()
  if (to.meta.auth && !auth.isLogin) {
    return '/login'
  }
  if (to.meta.admin && auth.user?.role !== 'ADMIN') {
    return '/'
  }
  return true
})

export default router
