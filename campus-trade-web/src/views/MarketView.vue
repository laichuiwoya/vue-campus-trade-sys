<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { goodsApi, favoriteApi, orderApi, commentApi } from '../api/modules'
import { useAuthStore } from '../stores/auth'

const auth = useAuthStore()
const loading = ref(false)
const goods = ref([])
const keyword = ref('')
const detailVisible = ref(false)
const current = ref(null)
const comments = ref([])
const commentForm = reactive({ content: '' })

async function loadGoods() {
  loading.value = true
  try {
    const res = await goodsApi.list({ keyword: keyword.value || undefined })
    goods.value = res.data || []
  } finally {
    loading.value = false
  }
}

async function openDetail(item) {
  current.value = item
  detailVisible.value = true
  const res = await commentApi.list({ goodsId: item.id })
  comments.value = res.data || []
}

async function favorite(item) {
  await favoriteApi.create({ goodsId: item.id })
  ElMessage.success('已收藏')
}

async function buy(item) {
  await orderApi.create({ goodsId: item.id, remark: '前端页面下单' })
  ElMessage.success('下单成功')
  await loadGoods()
}

async function addComment() {
  if (!commentForm.content) return
  await commentApi.create({ goodsId: current.value.id, content: commentForm.content })
  commentForm.content = ''
  await openDetail(current.value)
}

onMounted(loadGoods)
</script>

<template>
  <div class="page">
    <div class="toolbar">
      <div>
        <h2>商品市场</h2>
        <p class="muted">浏览在售商品，支持搜索、收藏、留言和下单。</p>
      </div>
      <el-input v-model="keyword" placeholder="搜索商品标题" clearable style="max-width: 320px" @keyup.enter="loadGoods">
        <template #append>
          <el-button @click="loadGoods">搜索</el-button>
        </template>
      </el-input>
    </div>

    <div v-loading="loading" class="goods-grid">
      <article v-for="item in goods" :key="item.id" class="goods-card">
        <img v-if="item.cover" class="goods-cover-img" :src="item.cover" :alt="item.title" />
        <div v-else class="goods-cover">{{ item.title?.slice(0, 1) || 'G' }}</div>
        <div class="goods-body">
          <h3 class="goods-title">{{ item.title }}</h3>
          <p class="muted">{{ item.content || '暂无描述' }}</p>
          <div class="goods-meta">
            <span class="price">￥{{ item.price }}</span>
            <el-tag type="success">库存 {{ item.quantity ?? 0 }}</el-tag>
          </div>
          <el-space wrap>
            <el-button size="small" @click="openDetail(item)">详情</el-button>
            <el-button size="small" :disabled="!auth.isLogin" @click="favorite(item)">收藏</el-button>
            <el-button
              size="small"
              type="primary"
              :disabled="!auth.isLogin || (item.quantity ?? 0) <= 0"
              @click="buy(item)"
            >
              下单
            </el-button>
          </el-space>
        </div>
      </article>
    </div>

    <el-dialog v-model="detailVisible" title="商品详情" width="620px">
      <template v-if="current">
        <img v-if="current.cover" class="detail-cover" :src="current.cover" :alt="current.title" />
        <h3>{{ current.title }}</h3>
        <p class="muted">{{ current.content }}</p>
        <p class="price">￥{{ current.price }}</p>
        <el-tag type="success">库存 {{ current.quantity ?? 0 }}</el-tag>
        <el-divider />
        <h4>留言评论</h4>
        <el-empty v-if="comments.length === 0" description="暂无评论" />
        <el-timeline v-else>
          <el-timeline-item v-for="item in comments" :key="item.id" :timestamp="item.createTime">
            {{ item.content }}
          </el-timeline-item>
        </el-timeline>
        <el-input v-if="auth.isLogin" v-model="commentForm.content" type="textarea" placeholder="写一条留言" />
        <el-button v-if="auth.isLogin" type="primary" style="margin-top: 10px" @click="addComment">提交留言</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.goods-cover-img {
  width: 100%;
  height: 150px;
  object-fit: cover;
  display: block;
}

.goods-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 12px;
}

.detail-cover {
  width: 100%;
  max-height: 260px;
  object-fit: cover;
  border-radius: 8px;
  margin-bottom: 12px;
}
</style>
