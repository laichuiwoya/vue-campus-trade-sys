<script setup>
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { categoryApi, fileApi, goodsApi } from '../api/modules'

const router = useRouter()
const categories = ref([])
const loading = ref(false)
const uploadLoading = ref(false)
const form = reactive({
  categoryId: '',
  title: '',
  content: '',
  price: 0,
  originalPrice: 0,
  quantity: 1,
  cover: '',
})

async function loadCategories() {
  const res = await categoryApi.list()
  categories.value = res.data || []
}

function beforeUpload(file) {
  if (!file.type.startsWith('image/')) {
    ElMessage.error('请选择图片文件')
    return false
  }
  if (file.size / 1024 / 1024 > 10) {
    ElMessage.error('图片不能超过 10MB')
    return false
  }
  return true
}

async function uploadCover(option) {
  uploadLoading.value = true
  try {
    const data = new FormData()
    data.append('file', option.file)
    const res = await fileApi.upload(data)
    form.cover = res.data.url
    ElMessage.success('封面上传成功')
    option.onSuccess(res)
  } catch (error) {
    option.onError(error)
  } finally {
    uploadLoading.value = false
  }
}

async function submit() {
  if (!form.title || !form.categoryId || !form.price || !form.quantity) {
    ElMessage.warning('请填写标题、分类、价格和数量')
    return
  }
  loading.value = true
  try {
    await goodsApi.create(form)
    ElMessage.success('发布成功')
    router.push('/')
  } finally {
    loading.value = false
  }
}

onMounted(loadCategories)
</script>

<template>
  <div class="page">
    <div class="toolbar">
      <div>
        <h2>发布商品</h2>
        <p class="muted">当前登录用户会自动成为商品发布人。</p>
      </div>
    </div>
    <el-card shadow="never">
      <el-form label-width="100px" :model="form" style="max-width: 760px">
        <el-form-item label="商品标题">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="商品分类">
          <el-select v-model="form.categoryId" placeholder="选择分类" style="width: 100%">
            <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="现价">
          <el-input-number v-model="form.price" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="原价">
          <el-input-number v-model="form.originalPrice" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="商品数量">
          <el-input-number v-model="form.quantity" :min="1" :step="1" />
        </el-form-item>
        <el-form-item label="商品封面">
          <div class="cover-uploader">
            <el-upload
              class="cover-upload"
              :show-file-list="false"
              :http-request="uploadCover"
              :before-upload="beforeUpload"
              accept="image/*"
            >
              <img v-if="form.cover" :src="form.cover" class="cover-preview" alt="商品封面" />
              <div v-else class="cover-placeholder" v-loading="uploadLoading">
                <span>点击选择本地图片</span>
              </div>
            </el-upload>
            <el-input v-model="form.cover" readonly placeholder="上传后自动生成封面地址" />
          </div>
        </el-form-item>
        <el-form-item label="商品描述">
          <el-input v-model="form.content" type="textarea" :rows="5" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="submit">提交发布</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.cover-uploader {
  display: grid;
  gap: 12px;
  width: 100%;
}

.cover-upload {
  width: 220px;
}

.cover-placeholder,
.cover-preview {
  width: 220px;
  height: 150px;
  border: 1px dashed #9ca3af;
  border-radius: 8px;
}

.cover-placeholder {
  display: grid;
  place-items: center;
  color: #6b7280;
  background: #f8fafc;
  cursor: pointer;
}

.cover-preview {
  object-fit: cover;
  cursor: pointer;
}
</style>
