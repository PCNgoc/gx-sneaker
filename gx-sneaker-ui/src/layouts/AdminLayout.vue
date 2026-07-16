<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const adminName = localStorage.getItem('adminName')
const role = localStorage.getItem('adminRole')

const logout = () => {
  localStorage.removeItem('adminToken')
  localStorage.removeItem('adminRole')
  localStorage.removeItem('adminName')

  router.push('/admin/login')
}
</script>

<template>
  <div class="admin-layout">

    <!-- Sidebar -->
    <aside class="sidebar">

      <div class="brand">
        <div class="logo">GX</div>
        <div>
          <h2>GX Sneaker</h2>
          <span>Admin Panel</span>
        </div>
      </div>

      <div class="menu-group">

        <p class="menu-title">QUẢN TRỊ</p>

        <router-link to="/admin/dashboard">
          🏠 Dashboard
        </router-link>

        <router-link to="/admin/nhan-vien">
          👨‍💼 Nhân viên
        </router-link>

      </div>

      <div class="menu-group">

        <p class="menu-title">SẢN PHẨM</p>

        <router-link to="/admin/san-pham">
          👟 Sản phẩm
        </router-link>

        <router-link to="/admin/danh-muc">
          📂 Danh mục
        </router-link>

        <router-link to="/admin/thuong-hieu">
          🏷 Thương hiệu
        </router-link>

        <router-link to="/admin/chat-lieu">
          🧵 Chất liệu
        </router-link>

        <router-link to="/admin/co-giay">
          👞 Cổ giày
        </router-link>

        <router-link to="/admin/de-giay">
          👟 Đế giày
        </router-link>

        <router-link to="/admin/mau-sac">
          🎨 Màu sắc
        </router-link>

        <router-link to="/admin/kich-thuoc">
          📏 Kích thước
        </router-link>

        <router-link to="/admin/xuat-xu">
          🌎 Xuất xứ
        </router-link>

      </div>

      <div class="menu-group">

        <p class="menu-title">BÁN HÀNG</p>

        <router-link to="/admin/hoa-don">
          🧾 Hóa đơn
        </router-link>
        <router-link to="/admin/khach-hang">
          👥 Khách hàng
        </router-link>
        <router-link to="/admin/phieu-giam-gia">
          🎟 Phiếu giảm giá
        </router-link>
        <router-link to="/admin/thong-ke">
          📊 Thống kê
        </router-link>

      </div>


    </aside>

    <!-- Main -->
    <div class="main-content">

      <!-- Header -->
      <header class="topbar">

        <div>
          <h3>Trang quản trị GX Sneaker</h3>
        </div>

        <div class="admin-info">

          <div class="user-box">
            <span class="name">{{ adminName }}</span>
            <span class="role">{{ role }}</span>
          </div>

          <button @click="logout">
            Đăng xuất
          </button>

        </div>

      </header>

      <!-- Content -->
      <div class="page-content">
        <router-view />
      </div>

    </div>

  </div>
</template>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: Inter, sans-serif;
}

.admin-layout {
  display: flex;
  min-height: 100vh;
  background:
    radial-gradient(circle at top left,#f8fafc,#eef2ff);
}

/* ===== SIDEBAR ===== */

.sidebar {
  width: 280px;
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;

  background:
    linear-gradient(
      180deg,
      #0f172a,
      #111827,
      #1e293b
    );

  backdrop-filter: blur(20px);

  color: white;
  overflow-y: auto;

  border-right: 1px solid rgba(255,255,255,.05);

  box-shadow:
    10px 0 40px rgba(0,0,0,.12);

  z-index: 1000;
}

/* ===== BRAND ===== */

.brand {
  display: flex;
  align-items: center;
  gap: 15px;

  padding: 25px;

  border-bottom:
    1px solid rgba(255,255,255,.08);
}

.logo {
  width: 58px;
  height: 58px;

  border-radius: 16px;

  background:
    linear-gradient(
      135deg,
      #ef4444,
      #dc2626
    );

  display: flex;
  justify-content: center;
  align-items: center;

  font-size: 22px;
  font-weight: 800;

  color: white;

  box-shadow:
    0 10px 25px rgba(239,68,68,.4);
}

.brand h2 {
  font-size: 20px;
  font-weight: 700;
}

.brand span {
  color: #94a3b8;
  font-size: 13px;
}

/* ===== MENU ===== */

.menu-group {
  margin-top: 25px;
}

.menu-title {
  color: #64748b;
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 1px;

  padding: 0 24px;
  margin-bottom: 12px;
}

.sidebar a {
  display: flex;
  align-items: center;
  gap: 12px;

  margin: 6px 14px;
  padding: 14px 18px;

  color: #cbd5e1;
  text-decoration: none;

  border-radius: 14px;

  transition: all .25s ease;
}

.sidebar a:hover {
  background: rgba(255,255,255,.08);

  color: white;

  transform: translateX(4px);
}

.router-link-active {
  background:
    linear-gradient(
      135deg,
      #ef4444,
      #dc2626
    ) !important;

  color: white !important;

  box-shadow:
    0 10px 25px rgba(239,68,68,.35);
}

/* ===== MAIN ===== */

.main-content {
  margin-left: 280px;
  width: calc(100% - 280px);
}

/* ===== TOPBAR ===== */

.topbar {
  position: sticky;
  top: 0;

  height: 80px;

  display: flex;
  justify-content: space-between;
  align-items: center;

  padding: 0 30px;

  background:
    rgba(255,255,255,.85);

  backdrop-filter: blur(20px);

  border-bottom:
    1px solid rgba(0,0,0,.05);

  z-index: 999;
}

.topbar h3 {
  font-size: 22px;
  font-weight: 700;
  color: #111827;
}

.admin-info {
  display: flex;
  align-items: center;
  gap: 18px;
}

/* USER */

.user-box {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.name {
  font-size: 14px;
  font-weight: 700;
  color: #111827;
}

.role {
  font-size: 12px;
  color: #ef4444;
}

/* BUTTON */

.topbar button {
  border: none;

  padding: 12px 20px;

  border-radius: 12px;

  background:
    linear-gradient(
      135deg,
      #ef4444,
      #dc2626
    );

  color: white;
  font-weight: 600;

  cursor: pointer;

  transition: all .3s ease;

  box-shadow:
    0 10px 20px rgba(239,68,68,.25);
}

.topbar button:hover {
  transform: translateY(-2px);

  box-shadow:
    0 15px 25px rgba(239,68,68,.4);
}

/* ===== PAGE CONTENT ===== */

.page-content {
  padding: 30px;
}

/* CARD STYLE */

.page-content :deep(.card),
.page-content :deep(.table-box),
.page-content :deep(.dashboard-card) {
  background: white;
  border-radius: 20px;

  box-shadow:
    0 10px 30px rgba(15,23,42,.05);

  border:
    1px solid rgba(0,0,0,.04);
}

/* SCROLLBAR */

.sidebar::-webkit-scrollbar {
  width: 6px;
}

.sidebar::-webkit-scrollbar-thumb {
  background: #475569;
  border-radius: 10px;
}

/* ===== MOBILE ===== */

@media (max-width: 1024px) {

  .sidebar {
    width: 240px;
  }

  .main-content {
    margin-left: 240px;
    width: calc(100% - 240px);
  }
}

@media (max-width: 768px) {

  .sidebar {
    transform: translateX(-100%);
  }

  .main-content {
    margin-left: 0;
    width: 100%;
  }

  .topbar {
    padding: 0 15px;
  }

  .topbar h3 {
    font-size: 18px;
  }
}
</style>

