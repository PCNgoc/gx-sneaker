<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const logout = () => {
  localStorage.removeItem('adminToken')
  localStorage.removeItem('adminRole')
  localStorage.removeItem('adminName')

  router.push('/admin/login')
}
</script>

<template>
  <div>
    <h1>Dashboard Admin</h1>

    <button @click="logout">Đăng xuất</button>
  </div>
</template>
