import { createRouter, createWebHistory } from 'vue-router'

import HomeView from '../views/HomeView.vue'
import LoginView from '@/views/LoginView.vue'
import RegisterView from '@/views/RegisterView.vue'
import ProfileView from '@/views/ProfileView.vue'
import ForgotPasswordView from '@/views/ForgotPasswordView.vue'
import ResetPasswordView from '@/views/ResetPasswordView.vue'
import ChangePasswordView from '@/views/ChangePasswordView.vue'
import AdminLoginView from '@/views/admin/AdminLoginView.vue'
import AdminDashboardView from '@/views/admin/AdminDashboardView.vue'
import StaffDashboardView from '@/views/admin/StaffDashboardView.vue'
const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView,
    },
    {
      path: '/profile',
      name: 'profile',
      component: ProfileView,
    },
    {
      path: '/forgot-password',
      name: 'forgot-password',
      component: ForgotPasswordView,
    },
    {
      path: '/reset-password',
      name: 'reset-password',
      component: ResetPasswordView,
    },
    {
      path: '/change-password',
      name: 'change-password',
      component: ChangePasswordView,
    },

    // Tạm thời tạo trước
    {
      path: '/admin',
      name: 'admin',
      component: HomeView,
    },
    {
      path: '/staff',
      name: 'staff',
      component: HomeView,
    },
    {
      path: '/admin/login',
      component: AdminLoginView,
    },

    {
      path: '/admin/dashboard',
      component: AdminDashboardView,
    },
    {
      path: '/staff',
      component: StaffDashboardView,
    },
  ],
})

router.beforeEach((to) => {
  const token = localStorage.getItem('token')

  const adminToken = localStorage.getItem('adminToken')

  const adminRole = localStorage.getItem('adminRole')

  // CUSTOMER

  const protectedRoutes = ['/profile', '/change-password', '/cart', '/checkout']

  if (protectedRoutes.includes(to.path) && !token) {
    return '/login'
  }

  // ADMIN

  if (to.path.startsWith('/admin') && to.path !== '/admin/login') {
    if (!adminToken || adminRole !== 'ADMIN') {
      return '/admin/login'
    }
  }

  // STAFF

  if (to.path.startsWith('/staff')) {
    if (!adminToken || (adminRole !== 'ADMIN' && adminRole !== 'STAFF')) {
      return '/admin/login'
    }
  }
})

export default router
