<script setup>
import { onMounted, watch } from "vue"
import { useAuth } from "@/composables/useAuth"
import { useCart } from "@/composables/useCart"

const { user, logout } = useAuth()
const { cartCount, fetchCartCount } = useCart()

onMounted(() => {
  if (user.value && user.value.id) {
    fetchCartCount(user.value.id)
  }
})

watch(
  () => user.value,
  (newUser) => {
    if (newUser && newUser.id) {
      fetchCartCount(newUser.id)
    } else {
      fetchCartCount(null)
    }
  }
)
</script>


<template>

  <!-- ================= TOP BAR ================= -->

  <div class="top-bar">

    <div class="marquee">

      <span>🚚 Miễn phí giao hàng toàn quốc</span>

      <span>🔥 Giảm đến 30% cho thành viên GX Club</span>

      <span>⭐ Hơn 10.000 khách hàng tin dùng</span>

      <span>💎 Premium Sneaker Store</span>

      <span>👟 Bộ sưu tập Sneaker 2026 đã cập bến</span>

      <span>🎁 Đổi trả trong 30 ngày</span>

    </div>

  </div>

  <!-- ================= HEADER ================= -->

  <header class="navbar">

    <!-- Logo -->

    <router-link
      to="/"
      class="logo"
    >

      <div class="logo-icon">

        GX

      </div>

      <div class="logo-text">

        <h2>GX SNEAKER</h2>

        <span>PREMIUM SNEAKER STORE</span>

      </div>

    </router-link>

    <!-- Menu -->

    <nav class="menu">

      <router-link to="/">
        Trang chủ
      </router-link>

      <router-link to="/products">
        Cửa hàng
      </router-link>

      <router-link to="/wishlist">
        Yêu thích
      </router-link>

      <router-link to="/contact">
        Liên hệ
      </router-link>

    </nav>

    <!-- Right -->

    <div class="actions">

      <!-- USER -->

      <template v-if="user">

        <router-link
          to="/wishlist"
          class="circle-btn"
        >

          <i class="far fa-heart"></i>

          <span class="badge">0</span>

        </router-link>

        <router-link
          to="/cart"
          class="circle-btn"
        >

          <i class="fas fa-cart-shopping"></i>

          <span class="badge" v-if="cartCount > 0">{{ cartCount }}</span>

        </router-link>

        <div class="user-dropdown">

          <div class="user-trigger">

            <div class="avatar">

              {{ user.hoTen?.charAt(0).toUpperCase() }}

            </div>

            <div class="user-info">

              <strong>

                {{ user.hoTen }}

              </strong>

              <small>

                Thành viên GX Club

              </small>

            </div>

            <i class="fas fa-chevron-down arrow"></i>

          </div>

          <div class="user-menu">

            <router-link to="/profile">

              <i class="far fa-user"></i>

              Hồ sơ cá nhân

            </router-link>

            <router-link to="/orders">

              <i class="fas fa-box"></i>

              Đơn hàng

            </router-link>

            <router-link to="/wishlist">

              <i class="far fa-heart"></i>

              Sản phẩm yêu thích

            </router-link>

            <button @click="logout">

              <i class="fas fa-right-from-bracket"></i>

              Đăng xuất

            </button>

          </div>

        </div>

      </template>

      <!-- LOGIN -->

      <router-link
        v-else
        to="/login"
        class="login-btn"
      >

        <i class="far fa-user"></i>

        Đăng nhập

      </router-link>

    </div>

  </header>

</template>

<style scoped>
/* ==========================================
   RESET
========================================== */

*{
  margin:0;
  padding:0;
  box-sizing:border-box;
}

a{
  text-decoration:none;
}

button{
  font-family:inherit;
  cursor:pointer;
}

:root{

  --primary:#111;
  --text:#222;
  --gray:#777;
  --border:#ececec;
  --bg:#ffffff;
  --hover:#f6f6f6;
  --danger:#ff3b30;

}

/* ==========================================
   TOP BAR
========================================== */

.top-bar{

  height:38px;

  background:linear-gradient(
    90deg,
    #111,
    #1f1f1f,
    #111
  );

  color:#fff;

  display:flex;

  align-items:center;

  overflow:hidden;

}

.marquee{

  display:flex;

  align-items:center;

  gap:80px;

  white-space:nowrap;

  width:max-content;

  animation:marquee 25s linear infinite;

}

.marquee span{

  font-size:13px;

  font-weight:500;

  letter-spacing:.4px;

}

@keyframes marquee{

  from{

    transform:translateX(100%);

  }

  to{

    transform:translateX(-100%);

  }

}

/* ==========================================
   NAVBAR
========================================== */

.navbar{

  position:sticky;

  top:0;

  z-index:999;

  height:76px;

  background:rgba(255,255,255,.95);

  backdrop-filter:blur(18px);

  border-bottom:1px solid var(--border);

  display:flex;

  justify-content:space-between;

  align-items:center;

  padding:0 60px;

  transition:.35s;

  box-shadow:

    0 10px 28px rgba(0,0,0,.05);

}

.navbar:hover{

  box-shadow:

    0 15px 35px rgba(0,0,0,.08);

}

/* ==========================================
   LOGO
========================================== */

.logo{

  display:flex;

  align-items:center;

  gap:14px;

}

.logo-icon{

  width:58px;

  height:58px;

  border-radius:18px;

  background:

    linear-gradient(
      135deg,
      #111,
      #444
    );

  color:#fff;

  display:flex;

  justify-content:center;

  align-items:center;

  font-size:22px;

  font-weight:800;

  letter-spacing:1px;

  transition:.35s;

  box-shadow:

    0 12px 25px rgba(0,0,0,.18);

}

.logo:hover .logo-icon{

  transform:

    rotate(-8deg)

    scale(1.08);

}

.logo-text{

  display:flex;

  flex-direction:column;

}

.logo-text h2{

  color:#111;

  font-size:22px;

  font-weight:800;

  letter-spacing:2px;

}

.logo-text span{

  color:#888;

  font-size:11px;

  letter-spacing:2px;

  margin-top:3px;

}

/* ==========================================
   MENU
========================================== */

.menu{

  display:flex;

  align-items:center;

  gap:42px;

}

.menu a{

  position:relative;

  color:#222;

  font-size:15px;

  font-weight:600;

  transition:.35s;

}

.menu a::after{

  content:"";

  position:absolute;

  left:0;

  bottom:-10px;

  width:0;

  height:3px;

  background:#111;

  border-radius:30px;

  transition:.35s;

}

.menu a:hover{

  color:#000;

}

.menu a:hover::after{

  width:100%;

}

.menu .router-link-active{

  color:#000;

}

.menu .router-link-active::after{

  width:100%;

}
/* ==========================================
   ACTIONS
========================================== */

.actions{
  display:flex;
  align-items:center;
  gap:14px;
}

/* ==========================================
   LOGIN BUTTON
========================================== */

.login-btn{

  height:46px;

  padding:0 22px;

  border-radius:50px;

  background:#111;

  color:#fff;

  display:flex;

  align-items:center;

  gap:10px;

  font-size:14px;

  font-weight:600;

  transition:.35s;

}

.login-btn:hover{

  transform:translateY(-2px);

  background:#000;

  box-shadow:0 10px 25px rgba(0,0,0,.18);

}

/* ==========================================
   CIRCLE BUTTON
========================================== */

.circle-btn{

  position:relative;

  width:46px;

  height:46px;

  border-radius:50%;

  background:#f5f5f5;

  color:#222;

  display:flex;

  align-items:center;

  justify-content:center;

  transition:.35s;

}

.circle-btn i{

  font-size:18px;

}

.circle-btn:hover{

  background:#111;

  color:#fff;

  transform:translateY(-3px);

  box-shadow:0 10px 22px rgba(0,0,0,.15);

}

/* ==========================================
   BADGE
========================================== */

.badge{

  position:absolute;

  top:-4px;

  right:-4px;

  width:18px;

  height:18px;

  border-radius:50%;

  background:#ff3b30;

  color:#fff;

  font-size:10px;

  font-weight:700;

  display:flex;

  align-items:center;

  justify-content:center;

  border:2px solid #fff;

}

/* ==========================================
   USER
========================================== */

.user-dropdown{

  position:relative;

}

.user-trigger{

  display:flex;

  align-items:center;

  gap:12px;

  padding:6px 10px;

  border-radius:50px;

  cursor:pointer;

  transition:.35s;

}

.user-trigger:hover{

  background:#f7f7f7;

}

.avatar{

  width:44px;

  height:44px;

  border-radius:50%;

  background:linear-gradient(135deg,#111,#555);

  color:#fff;

  display:flex;

  align-items:center;

  justify-content:center;

  font-size:17px;

  font-weight:700;

  flex-shrink:0;

}

.user-info{

  display:flex;

  flex-direction:column;

  line-height:1.2;

}

.user-info strong{

  font-size:14px;

  color:#111;

}

.user-info small{

  color:#888;

  font-size:11px;

}

.arrow{

  color:#666;

  font-size:11px;

  margin-left:4px;

}

/* ==========================================
   DROPDOWN
========================================== */

.user-menu{

  position:absolute;

  right:0;

  top:62px;

  width:260px;

  background:#fff;

  border-radius:18px;

  overflow:hidden;

  border:1px solid #eee;

  box-shadow:0 20px 45px rgba(0,0,0,.12);

  opacity:0;

  visibility:hidden;

  transform:translateY(15px);

  transition:.3s;

}

.user-dropdown:hover .user-menu{

  opacity:1;

  visibility:visible;

  transform:translateY(0);

}

.user-menu a,

.user-menu button{

  width:100%;

  padding:15px 20px;

  display:flex;

  align-items:center;

  gap:12px;

  border:none;

  background:none;

  color:#222;

  font-size:14px;

  transition:.3s;

  text-align:left;

}

.user-menu a:hover,

.user-menu button:hover{

  background:#f7f7f7;

  padding-left:26px;

}

.user-menu button{

  border-top:1px solid #eee;

  color:#d93025;

}

.user-menu i{

  width:18px;

  text-align:center;

}

/* ==========================================
   ANIMATION
========================================== */

.logo,

.menu a,

.circle-btn,

.login-btn,

.user-trigger{

  animation:fadeUp .6s ease both;

}

@keyframes fadeUp{

  from{

    opacity:0;

    transform:translateY(15px);

  }

  to{

    opacity:1;

    transform:translateY(0);

  }

}/* ==========================================
   RESPONSIVE
========================================== */

@media (max-width:1200px){

  .navbar{
    padding:0 35px;
  }

  .menu{
    gap:28px;
  }

}

@media (max-width:992px){

  .navbar{

    height:auto;

    padding:18px 25px;

    flex-wrap:wrap;

    gap:20px;

  }

  .logo{

    width:100%;

    justify-content:center;

  }

  .menu{

    width:100%;

    justify-content:center;

    gap:25px;

  }

  .actions{

    width:100%;

    justify-content:center;

  }

}

@media (max-width:768px){

  .top-bar{

    display:none;

  }

  .navbar{

    padding:15px;

  }

  .menu{

    display:none;

  }

  .actions{

    gap:10px;

  }

  .login-btn{

    height:42px;

    padding:0 18px;

    font-size:13px;

  }

  .circle-btn{

    width:42px;

    height:42px;

  }

  .avatar{

    width:40px;

    height:40px;

    font-size:15px;

  }

  .user-info{

    display:none;

  }

  .user-menu{

    width:230px;

    right:-10px;

  }

}

@media (max-width:480px){

  .navbar{

    padding:12px;

  }

  .logo{

    gap:10px;

  }

  .logo-icon{

    width:48px;

    height:48px;

    font-size:18px;

    border-radius:14px;

  }

  .logo-text h2{

    font-size:17px;

    letter-spacing:1px;

  }

  .logo-text span{

    font-size:9px;

    letter-spacing:1px;

  }

}

/* ==========================================
   SMOOTH EFFECT
========================================== */

html{

  scroll-behavior:smooth;

}

body{

  background:#fff;

  color:#222;

  font-family:
    "Inter",
    "Segoe UI",
    sans-serif;

}

/* ==========================================
   EXTRA EFFECT
========================================== */

.logo,
.menu a,
.circle-btn,
.login-btn,
.user-trigger{

  transition:all .3s ease;

}

.menu a:hover{

  transform:translateY(-2px);

}

.logo:hover{

  transform:translateY(-1px);

}

.circle-btn:hover{

  transform:translateY(-3px) scale(1.05);

}

.user-trigger:hover{

  background:#f5f5f5;

}

.user-menu{

  animation:dropdown .25s ease;

}

@keyframes dropdown{

  from{

    opacity:0;

    transform:
      translateY(10px)
      scale(.96);

  }

  to{

    opacity:1;

    transform:
      translateY(0)
      scale(1);

  }

}

::-webkit-scrollbar{

  width:8px;

}

::-webkit-scrollbar-thumb{

  background:#bbb;

  border-radius:20px;

}

::-webkit-scrollbar-thumb:hover{

  background:#888;

}

</style>
