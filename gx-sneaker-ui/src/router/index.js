import { createRouter, createWebHistory } from 'vue-router'

import HomeView from '../views/HomeView.vue'

// Auth
import LoginView from '@/views/LoginView.vue'
import RegisterView from '@/views/RegisterView.vue'
import ProfileView from '@/views/ProfileView.vue'
import ForgotPasswordView from '@/views/ForgotPasswordView.vue'
import ResetPasswordView from '@/views/ResetPasswordView.vue'
import ChangePasswordView from '@/views/ChangePasswordView.vue'

import AdminLoginView from '@/views/admin/AdminLoginView.vue'
import AdminDashboardView from '@/views/admin/AdminDashboardView.vue'
import StaffDashboardView from '@/views/admin/StaffDashboardView.vue'
import AdminLayout from '@/layouts/AdminLayout.vue'
import NhanVienView from '@/views/admin/NhanVienView.vue'
import OrderSuccessView from "@/views/user/OrderSuccessView.vue"
// Danh mục sản phẩm
import ThuongHieuView from '../views/ThuongHieuView.vue'
import DanhMucView from '@/views/DanhMucView.vue'
import XuatXuView from '@/views/XuatXuView.vue'
import ChatLieuView from '@/views/ChatLieuView.vue'
import CoGiayView from '@/views/CoGiayView.vue'
import DeGiayView from '@/views/DeGiayView.vue'
import KichThuocView from '@/views/KichThuocView.vue'
import MauSacView from '@/views/MauSacView.vue'
import SanPhamView from '@/views/SanPhamView.vue'
import ChiTietSanPhamView from '@/views/ChiTietSanPhamView.vue'

import ProductListView from '@/views/user/ProductListView.vue'
import ProductDetailView from '@/views/user/ProductDetailView.vue'

//Hóa đơn
import HoaDonView from '@/views/admin/HoaDonView.vue'
import Dashboard from "@/views/admin/Dashboard.vue";
// Giao diện nút mua ngay
import CheckoutView from '@/views/user/CheckoutView.vue'
// Order
import OrderView from "@/views/user/OrderView.vue"
import OrderDetailView from "@/views/user/OrderDetailView.vue"
//


// Hải's Khách Hàng, Phiếu Giảm Giá & Giỏ Hàng
import KhachHangView from '@/views/admin/KhachHangView.vue'
import PhieuGiamGiaView from '@/views/admin/PhieuGiamGiaView.vue'
import GioHangView from '@/views/user/GioHangView.vue'


const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },

    // Auth
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
    {
      path: "/order-success/:id",
      name: "OrderSuccess",
      component: OrderSuccessView,
    },
    //Đặt hàng


    // Danh mục
    {
      path: '/admin',
      component: AdminLayout,
      children: [
        {
          path: 'dashboard',
          component: AdminDashboardView,
        },
        {
          path: 'nhan-vien',
          name: 'AdminNhanVien',
          component: NhanVienView,
        },
        {
          path: 'san-pham',
          component: SanPhamView,
        },
        {
          path: 'san-pham/:id/chi-tiet',
          name: 'chi-tiet-san-pham',
          component: ChiTietSanPhamView,
        },
        {
          path: 'danh-muc',
          component: DanhMucView,
        },
        {
          path: 'thuong-hieu',
          component: ThuongHieuView,
        },
        {
          path: 'chat-lieu',
          component: ChatLieuView,
        },
        {
          path: 'co-giay',
          component: CoGiayView,
        },
        {
          path: 'de-giay',
          component: DeGiayView,
        },
        {
          path: 'mau-sac',
          component: MauSacView,
        },
        {
          path: 'kich-thuoc',
          component: KichThuocView,
        },
        {
          path: 'xuat-xu',
          component: XuatXuView,
        },
        {
          path: 'hoa-don',
          component: HoaDonView,
        },

        {
          path: 'thong-ke',
          component: Dashboard,
        },
        {
          path: 'khach-hang',
          component: KhachHangView,
        },
        {
          path: 'phieu-giam-gia',
          component: PhieuGiamGiaView,
        }

      ],

    },

    {
      path: '/products',
      name: 'products',
      component: ProductListView,
    },

    {
      path: '/products/:id',
      name: 'product-detail',
      component: ProductDetailView,
    },
    {
      path: '/cart',
      name: 'cart',
      component: GioHangView,
    },
    {
      path: '/checkout',
      name: 'checkout',
      component: CheckoutView
    },
    {
      path:"/orders",
      component:OrderView
    },


{
  path: "/orders/:id",
    name: "OrderDetail",
  component: OrderDetailView
},


    // Admin
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
      path: '/staff/dashboard',
      component: StaffDashboardView,
    },
    {
      path: '/dashboard',
      name: 'dashboard',
      component: Dashboard,
    },

  ],
})

router.beforeEach((to) => {
  const token = localStorage.getItem('token')

  const adminToken = localStorage.getItem('adminToken')

  const adminRole = localStorage.getItem('adminRole')

  const protectedRoutes = [
    '/profile',
    '/change-password',
    '/cart',
    '/checkout',
  ]

  if (protectedRoutes.includes(to.path) && !token) {
    return '/login'
  }

  if (to.path.startsWith('/admin') && to.path !== '/admin/login') {
    if (!adminToken || adminRole !== 'ADMIN') {
      return '/admin/login'
    }
  }


  if (to.path.startsWith('/staff')) {
    if (!adminToken || (adminRole !== 'ADMIN' && adminRole !== 'STAFF')) {
      return '/admin/login'
    }
  }
})

export default router
