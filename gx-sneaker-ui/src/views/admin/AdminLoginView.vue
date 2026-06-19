<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()

const username = ref('')
const password = ref('')
const errorMessage = ref('')

const login = async () => {
  try {
    const response = await axios.post('http://localhost:8080/api/auth/admin/login', {
      username: username.value,
      password: password.value,
    })

    localStorage.setItem('adminToken', response.data.token)

    localStorage.setItem('adminRole', response.data.role)

    localStorage.setItem('adminName', response.data.hoTen)

    if (response.data.role === 'ADMIN') {
      router.push('/admin/dashboard')
    } else {
      router.push('/staff')
    }
  } catch (error) {
    errorMessage.value = error.response?.data?.message || 'Đăng nhập thất bại'
  }
}
</script>

<template>
  <div class="login-container">
    <div class="login-card">
      <h2>GX Sneaker Admin</h2>

      <input v-model="username" placeholder="Email hoặc SĐT" />

      <input v-model="password" type="password" placeholder="Mật khẩu" />

      <button @click="login">Đăng nhập</button>

      <p class="error">
        {{ errorMessage }}
      </p>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #f5f5f5;
}

.login-card {
  width: 400px;
  background: white;
  padding: 30px;
  border-radius: 10px;
}

input {
  width: 100%;
  margin-bottom: 15px;
  padding: 10px;
}

button {
  width: 100%;
  padding: 10px;
}

.error {
  color: red;
}
</style>
