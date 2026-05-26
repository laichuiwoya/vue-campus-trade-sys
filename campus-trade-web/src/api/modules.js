import request from './request'

export const authApi = {
  login: (data) => request.post('/auth/login', data),
  register: (data) => request.post('/auth/register', data),
  me: () => request.get('/auth/me'),
}

export const goodsApi = {
  list: (params) => request.get('/goods', { params }),
  detail: (id) => request.get(`/goods/${id}`),
  create: (data) => request.post('/goods', data),
  update: (data) => request.put('/goods', data),
  remove: (id) => request.delete(`/goods/${id}`),
}

export const categoryApi = {
  list: () => request.get('/categories'),
}

export const favoriteApi = {
  list: () => request.get('/favorites'),
  create: (data) => request.post('/favorites', data),
  remove: (id) => request.delete(`/favorites/${id}`),
}

export const orderApi = {
  list: (params) => request.get('/orders', { params }),
  create: (data) => request.post('/orders', data),
  update: (data) => request.put('/orders', data),
}

export const commentApi = {
  list: (params) => request.get('/comments', { params }),
  create: (data) => request.post('/comments', data),
}

export const fileApi = {
  upload: (data) => request.post('/files/upload', data),
}

export const adminApi = {
  users: (params) => request.get('/admin/users', { params }),
  updateUserStatus: (id, status) => request.put(`/admin/users/${id}/status`, null, { params: { status } }),
  goods: (params) => request.get('/admin/goods', { params }),
  updateGoodsStatus: (id, status) => request.put(`/admin/goods/${id}/status`, null, { params: { status } }),
  orders: (params) => request.get('/admin/orders', { params }),
  categories: () => request.get('/admin/categories'),
  createCategory: (data) => request.post('/admin/categories', data),
  updateCategory: (data) => request.put('/admin/categories', data),
  removeCategory: (id) => request.delete(`/admin/categories/${id}`),
}
