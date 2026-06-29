<script setup>
import { ref, computed, onMounted, watch } from "vue"
import { useRoute, useRouter } from "vue-router"

import {
  getById as getProductById
} from "@/services/sanPhamService"

import {
  getBySanPham
} from "@/services/chiTietSanPhamService"

import {
  addItemToCart
} from "@/services/gioHangService"
import { useCart } from "@/composables/useCart"




const route = useRoute()
const router = useRouter()
const { fetchCartCount } = useCart()

const loading = ref(true)

const product = ref({})

const details = ref([])

const selectedColor = ref(null)

const selectedSize = ref(null)

const quantity = ref(1)

const activeImage = ref("")

const activeTab = ref("overview")

const relatedProducts = ref([])

/* ===========================
   FORMAT
=========================== */

const formatMoney = (value) => {

  if (!value) return "0"

  return Number(value).toLocaleString("vi-VN")
}

/* ===========================
   LOAD DATA
=========================== */

const loadProduct = async () => {

  try {

    loading.value = true

    const id = route.params.id

    const productRes = await getProductById(id)

    product.value = productRes.data

    if (product.value?.anhDaiDien) {

      activeImage.value =
        `/images/${product.value.anhDaiDien}`
    }

    const detailRes = await getBySanPham(id)

    details.value = detailRes.data || []

    if (details.value.length) {

      selectedColor.value =
        details.value[0].idMauSac
    }

  } catch (error) {

    console.error(error)

  } finally {

    loading.value = false
  }
}

/* ===========================
   COLORS
=========================== */

const colors = computed(() => {

  const map = new Map()

  details.value.forEach(item => {

    if (!map.has(item.idMauSac)) {

      map.set(item.idMauSac, {
        id: item.idMauSac,
        name: item.tenMauSac
      })
    }
  })

  return [...map.values()]
})

/* ===========================
   SIZES
=========================== */

/* ===========================
   ALL SIZE 35 -> 44
=========================== */

const allSizes = computed(() => {

  const result = []

  for (let i = 35; i <= 44; i++) {

    result.push(i)

  }

  return result
})

const getSizeInfo = (size) => {

  return details.value.find(

    item =>

      item.idMauSac === selectedColor.value &&

      Number(item.size) === Number(size)

  )
}

/* ===========================
   AUTO SELECT SIZE
=========================== */

watch(
  selectedColor,
  () => {

    quantity.value = 1

    selectedSize.value = null

    const firstAvailable = details.value.find(
      item =>
        item.idMauSac === selectedColor.value &&
        item.soLuongTon > 0
    )

    if (firstAvailable) {
      selectedSize.value =
        firstAvailable.idKichThuoc
    }
  },
  {
    immediate: true
  }
)

/* ===========================
   SELECTED VARIANT
=========================== */

const selectedVariant = computed(() => {

  return details.value.find(
    item =>
      item.idMauSac === selectedColor.value &&
      item.idKichThuoc === selectedSize.value
  )
})

/* ===========================
   PRICE
=========================== */

const displayPrice = computed(() => {

  if (selectedVariant.value) {

    return formatMoney(
      selectedVariant.value.giaBan
    )
  }

  if (!details.value.length) {

    return "0"
  }

  return formatMoney(
    details.value[0].giaBan
  )
})

const minPrice = computed(() => {

  if (!details.value.length) {

    return 0
  }

  return Math.min(
    ...details.value.map(
      item => Number(item.giaBan)
    )
  )
})

const maxPrice = computed(() => {

  if (!details.value.length) {

    return 0
  }

  return Math.max(
    ...details.value.map(
      item => Number(item.giaBan)
    )
  )
})

/* ===========================
   STOCK
=========================== */

const stock = computed(() => {

  return selectedVariant.value
    ?.soLuongTon || 0
})

const totalStock = computed(() => {

  return details.value.reduce(
    (sum, item) =>
      sum + (item.soLuongTon || 0),
    0
  )
})

/* ===========================
   QUANTITY
=========================== */

const increaseQuantity = () => {

  if (quantity.value < stock.value) {

    quantity.value++
  }
}

const decreaseQuantity = () => {

  if (quantity.value > 1) {

    quantity.value--
  }
}

watch(stock, () => {

  if (quantity.value > stock.value) {

    quantity.value = 1
  }
})

/* ===========================
   ADD TO CART
=========================== */

const addToCart = async () => {

  if (!selectedVariant.value) {
    alert("Vui lòng chọn màu sắc và kích thước")
    return
  }

  const userData = localStorage.getItem("user")
  if (!userData) {
    alert("Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng")
    router.push("/login")
    return
  }

  try {
    const parsedUser = JSON.parse(userData)
    if (!parsedUser || !parsedUser.id) {
      alert("Vui lòng đăng nhập lại")
      router.push("/login")
      return
    }

    // Call backend API to add to cart database
    await addItemToCart(parsedUser.id, selectedVariant.value.id, quantity.value)
    await fetchCartCount(parsedUser.id)
    alert("Đã thêm sản phẩm vào giỏ hàng thành công!")
  } catch (err) {
    console.error("Lỗi thêm vào giỏ hàng:", err)
    alert("Không thể thêm vào giỏ hàng: " + (err.response?.data?.message || err.message))
  }
}


/* ===========================
   BUY NOW
=========================== */

// const buyNow = () => {
//
//   addToCart()
//
//   router.push("/cart")
// }


const buyNow = () => {

// Chưa chọn biến thể
  if (!selectedVariant.value) {


alert(
  "Vui lòng chọn màu sắc và kích thước"
)

return


  }

// Hết hàng
  if (stock.value <= 0) {


alert(
  "Sản phẩm hiện đã hết hàng"
)

return


  }

// Số lượng vượt tồn kho
  if (quantity.value > stock.value) {


alert(
  "Số lượng vượt quá tồn kho"
)

return


  }

  const buyNowProduct = {


productId:
  product.value.id,

detailId:
  selectedVariant.value.id,

productName:
  product.value.tenSanPham,

image:
  product.value.anhDaiDien,

color:
  selectedVariant.value.tenMauSac,

size:
  selectedVariant.value.size,

quantity:
  quantity.value,

price:
  selectedVariant.value.giaBan


}

// Lưu sản phẩm mua ngay
  localStorage.setItem(
    "buyNowProduct",
    JSON.stringify(buyNowProduct)
  )

// Chuyển sang trang thanh toán
  router.push("/checkout")

}

/* ===========================
   RELATED PRODUCTS
=========================== */

const loadRelatedProducts =
  async () => {

    try {

      relatedProducts.value = []

    } catch (error) {

      console.error(error)
    }
  }

/* ===========================
   IMAGE
=========================== */

const changeImage = image => {

  activeImage.value = image
}

/* ===========================
   TABS
=========================== */

const changeTab = tab => {

  activeTab.value = tab
}

/* ===========================
   MOUNTED
=========================== */

onMounted(async () => {

  await loadProduct()

  await loadRelatedProducts()
})
</script>
<template>
  <div class="product-detail-page">

    <!-- LOADING -->
    <div v-if="loading" class="loading-container">
      <div class="loader"></div>
      <p>Đang tải sản phẩm...</p>
    </div>

    <template v-else>

      <!-- BREADCRUMB -->
      <div class="breadcrumb">

        <span @click="$router.push('/')">
          Trang chủ
        </span>

        <span>/</span>

        <span>
          {{ product.tenDanhMuc }}
        </span>

        <span>/</span>

        <b>
          {{ product.tenSanPham }}
        </b>

      </div>

      <!-- MAIN -->
      <div class="detail-container">

        <!-- LEFT -->
        <div class="gallery-section">

          <div class="main-image-wrapper">

            <img
              :src="activeImage"
              class="main-image"
              alt=""
            />

            <div class="image-badge">
              HOT
            </div>

          </div>

          <div class="thumbnail-list">

            <div
              class="thumbnail active"
            >
              <img
                :src="activeImage"
                alt=""
              />
            </div>

          </div>

        </div>

        <!-- RIGHT -->
        <div class="info-panel">

          <!-- BRAND -->
          <div class="brand">
            {{ product.tenThuongHieu }}
          </div>

          <!-- TITLE -->
          <h1 class="product-title">
            {{ product.tenSanPham }}
          </h1>

          <!-- BADGES -->
          <div class="badges">

            <span class="badge brand-badge">
              {{ product.tenThuongHieu }}
            </span>

            <span class="badge stock-badge">
              {{ totalStock }} sản phẩm
            </span>

            <span class="badge hot-badge">
              BÁN CHẠY
            </span>

          </div>

          <!-- RATING -->
          <div class="rating">

            <span class="stars">
              ★★★★★
            </span>

            <span class="rating-text">
              4.9 (120 đánh giá)
            </span>

          </div>

          <!-- PRICE -->
          <div class="price-box">

            <div class="current-price">
              {{ displayPrice }} đ
            </div>

            <div
              v-if="minPrice !== maxPrice"
              class="price-range"
            >
              Giá từ
              {{ formatMoney(minPrice) }}
              -
              {{ formatMoney(maxPrice) }} đ
            </div>

          </div>

          <!-- STOCK -->
          <div class="stock-section">

            <span
              v-if="stock > 0"
              class="in-stock"
            >
              ✔ Còn hàng ({{ stock }})
            </span>

            <span
              v-else
              class="out-stock"
            >
              Hết hàng
            </span>

          </div>

          <!-- COLORS -->
          <div class="option-group">

            <h4>
              Màu sắc
            </h4>

            <div class="color-list">

              <button
                v-for="color in colors"
                :key="color.id"
                class="color-btn"
                :class="{
                  active:
                    selectedColor === color.id
                }"
                @click="
                  selectedColor = color.id
                "
              >
                {{ color.name }}
              </button>

            </div>

          </div>

          <!-- SIZE -->
          <div class="option-group">

            <h4>
              Kích thước
            </h4>

            <div class="size-list">

              <button

                v-for="size in allSizes"

                :key="size"

                class="size-btn"

                :class="{
  active:
    selectedSize === getSizeInfo(size)?.idKichThuoc,

  disabled:
    !getSizeInfo(size) ||
    getSizeInfo(size)?.soLuongTon <= 0
}"

                :disabled="
      !getSizeInfo(size) ||
      getSizeInfo(size)?.soLuongTon <= 0
    "

                @click="
      selectedSize =
      getSizeInfo(size)?.idKichThuoc
    "

              >

                <div class="size-number">
                  {{ size }}
                </div>

                <small
                  v-if="getSizeInfo(size)"
                >
                  {{
                    getSizeInfo(size).soLuongTon > 0
                      ? `Còn ${getSizeInfo(size).soLuongTon}`
                      : 'Hết hàng'
                  }}
                </small>

              </button>

            </div>

          </div>

          <!-- QUANTITY -->
          <div class="option-group">

            <h4>
              Số lượng
            </h4>

            <div class="quantity-box">

              <button
                @click="decreaseQuantity"
              >
                -
              </button>

              <input
                v-model="quantity"
                type="number"
              />

              <button
                @click="increaseQuantity"
              >
                +
              </button>

            </div>

          </div>

          <!-- ACTIONS -->
          <div class="action-buttons">

            <button
              class="btn-cart"
              @click="addToCart"
            >
              🛒 Thêm vào giỏ
            </button>

            <button
              class="btn-buy"
              @click="buyNow"
            >
              MUA NGAY
            </button>

          </div>

        </div>

      </div>

      <!-- TABS -->
      <div class="tabs">

        <button
          :class="{
            active:
              activeTab === 'overview'
          }"
          @click="
            changeTab('overview')
          "
        >
          Mô tả
        </button>

        <button
          :class="{
            active:
              activeTab === 'info'
          }"
          @click="
            changeTab('info')
          "
        >
          Thông tin
        </button>

        <button
          :class="{
            active:
              activeTab === 'variants'
          }"
          @click="
            changeTab('variants')
          "
        >
          Phiên bản
        </button>

      </div>

      <!-- OVERVIEW -->
      <div
        v-if="activeTab === 'overview'"
        class="content-card"
      >

        <h2>
          Mô tả sản phẩm
        </h2>

        <p>
          {{ product.moTaChiTiet }}
        </p>

      </div>

      <!-- INFO -->
      <div
        v-if="activeTab === 'info'"
        class="content-card"
      >

        <div class="info-grid">

          <div class="info-item">

            <span>
              Thương hiệu
            </span>

            <strong>
              {{ product.tenThuongHieu }}
            </strong>

          </div>

          <div class="info-item">

            <span>
              Danh mục
            </span>

            <strong>
              {{ product.tenDanhMuc }}
            </strong>

          </div>

          <div class="info-item">

            <span>
              Chất liệu
            </span>

            <strong>
              {{ product.tenChatLieu }}
            </strong>

          </div>

          <div class="info-item">

            <span>
              Xuất xứ
            </span>

            <strong>
              {{ product.tenXuatXu }}
            </strong>

          </div>

          <div class="info-item">

            <span>
              Giới tính
            </span>

            <strong>
              {{ product.gioiTinh }}
            </strong>

          </div>

        </div>

      </div>

      <!-- VARIANTS -->
      <div
        v-if="activeTab === 'variants'"
        class="content-card"
      >

        <table class="variant-table">

          <thead>

          <tr>
            <th>Màu</th>
            <th>Size</th>
            <th>Giá</th>
            <th>Tồn</th>
          </tr>

          </thead>

          <tbody>

          <tr
            v-for="item in details"
            :key="item.id"
          >

            <td>
              {{ item.tenMauSac }}
            </td>

            <td>
              {{ item.size }}
            </td>

            <td>
              {{ formatMoney(item.giaBan) }} đ
            </td>

            <td>
              {{ item.soLuongTon }}
            </td>

          </tr>

          </tbody>

        </table>

      </div>

      <!-- RELATED -->
      <section class="related-section">

        <h2>
          Có thể bạn sẽ thích
        </h2>

        <div class="related-grid">

          <div
            v-for="item in relatedProducts"
            :key="item.id"
            class="related-card"
          >

            <img
              :src="`/images/${item.anhDaiDien}`"
              alt=""
            />

            <h4>
              {{ item.tenSanPham }}
            </h4>

          </div>

        </div>

      </section>

    </template>

  </div>
</template>
<style scoped>
.product-detail-page{
  background:#fff;
  min-height:100vh;
  padding-bottom:50px;
}

/* ===== LOADING ===== */

.loading-container{
  min-height:70vh;
  display:flex;
  flex-direction:column;
  justify-content:center;
  align-items:center;
  gap:12px;
}

.loader{
  width:38px;
  height:38px;
  border:3px solid #e5e7eb;
  border-top:3px solid #111827;
  border-radius:50%;
  animation:spin 1s linear infinite;
}

@keyframes spin{
  to{
    transform:rotate(360deg);
  }
}

/* ===== BREADCRUMB ===== */

.breadcrumb{
  width:92%;
  max-width:1100px;
  margin:auto;
  padding:18px 0;
  display:flex;
  gap:8px;
  font-size:13px;
  color:#6b7280;
}

.breadcrumb span{
  cursor:pointer;
}

.breadcrumb span:hover{
  color:#111827;
}

/* ===== MAIN ===== */

.detail-container{
  width:92%;
  max-width:1100px;
  margin:auto;

  display:grid;
  grid-template-columns:46% 54%;
  gap:24px;
}

/* ===== IMAGE ===== */

.gallery-section{
  position:sticky;
  top:15px;
  align-self:start;
}

.main-image-wrapper{
  background:#fff;
  border:1px solid #f1f5f9;
  border-radius:12px;
  padding:12px;
}

.main-image{
  width:100%;
  height:450px;
  object-fit:contain;
}

.image-badge{
  display:none;
}

.thumbnail-list{
  display:flex;
  gap:8px;
  margin-top:10px;
}

.thumbnail{
  width:60px;
  height:60px;
  border-radius:8px;
  overflow:hidden;
  border:1px solid #e5e7eb;
}

.thumbnail img{
  width:100%;
  height:100%;
  object-fit:cover;
}

/* ===== INFO ===== */

.info-panel{
  border:1px solid #f1f5f9;
  border-radius:12px;
  padding:22px;
  background:#fff;
}

.brand{
  color:#2563eb;
  font-size:12px;
  font-weight:700;
  text-transform:uppercase;
}

.product-title{
  font-size:24px;
  line-height:1.3;
  font-weight:700;
  margin:8px 0;
  color:#111827;
}

/* ===== BADGES ===== */

.badges{
  display:flex;
  gap:6px;
  flex-wrap:wrap;
  margin-top:10px;
}

.badge{
  padding:4px 10px;
  border-radius:999px;
  font-size:10px;
  font-weight:600;
}

.brand-badge{
  background:#dbeafe;
  color:#1d4ed8;
}

.stock-badge{
  background:#dcfce7;
  color:#15803d;
}

.hot-badge{
  background:#fee2e2;
  color:#dc2626;
}

/* ===== RATING ===== */

.rating{
  display:flex;
  gap:8px;
  align-items:center;
  margin-top:12px;
}

.stars{
  color:#f59e0b;
}

.rating-text{
  font-size:13px;
  color:#6b7280;
}

/* ===== PRICE ===== */

.price-box{
  margin-top:18px;
}

.current-price{
  font-size:28px;
  font-weight:800;
  color:#111827;
}

.price-range{
  margin-top:4px;
  color:#6b7280;
  font-size:13px;
}

/* ===== STOCK ===== */

.stock-section{
  margin-top:18px;
}

.in-stock{
  color:#16a34a;
  font-weight:600;
}

.out-stock{
  color:#dc2626;
  font-weight:600;
}

/* ===== OPTION ===== */

.option-group{
  margin-top:22px;
}

.option-group h4{
  margin-bottom:10px;
  font-size:14px;
}

/* ===== COLOR ===== */

.color-list{
  display:flex;
  gap:8px;
  flex-wrap:wrap;
}

.color-btn{
  height:36px;
  padding:0 14px;
  border-radius:8px;
  border:1px solid #d1d5db;
  background:#fff;
  cursor:pointer;
  font-size:13px;
  transition:.2s;
}

.color-btn.active{
  background:#111827;
  border-color:#111827;
  color:#fff;
}

/* ===== SIZE ===== */

.size-list{
  display:grid;
  grid-template-columns:repeat(5,52px);
  gap:8px;
}

.size-btn{
  width:52px;
  height:42px;

  border-radius:8px;
  border:1px solid #d1d5db;

  background:#fff;
  cursor:pointer;

  display:flex;
  justify-content:center;
  align-items:center;

  transition:.2s;
}

.size-btn:hover:not(.disabled){
  border-color:#111827;
}

.size-btn.active{
  background:#111827;
  color:#fff;
  border-color:#111827;
}

.size-number{
  font-size:13px;
  font-weight:700;
}

.size-btn small{
  display:none;
}

.size-btn.disabled{
  opacity:.35;
  cursor:not-allowed;
}

/* ===== QUANTITY ===== */

.quantity-box{
  display:flex;
  align-items:center;
  gap:8px;
}

.quantity-box button{
  width:36px;
  height:36px;
  border:none;
  border-radius:8px;
  background:#f3f4f6;
  cursor:pointer;
}

.quantity-box input{
  width:60px;
  height:36px;
  border:1px solid #d1d5db;
  border-radius:8px;
  text-align:center;
}

/* ===== ACTION ===== */

.action-buttons{
  margin-top:24px;
  display:flex;
  gap:10px;
}

.btn-cart,
.btn-buy{
  flex:1;
  height:46px;
  border-radius:8px;
  font-size:14px;
  font-weight:700;
  cursor:pointer;
  transition:.2s;
}

.btn-cart{
  border:1px solid #111827;
  background:#fff;
}

.btn-cart:hover{
  background:#111827;
  color:#fff;
}

.btn-buy{
  border:none;
  background:#111827;
  color:#fff;
}

.btn-buy:hover{
  opacity:.9;
}

/* ===== TABS ===== */

.tabs{
  width:92%;
  max-width:1100px;
  margin:35px auto 15px;
  display:flex;
  gap:8px;
}

.tabs button{
  border:none;
  background:#f3f4f6;
  padding:8px 16px;
  border-radius:8px;
  cursor:pointer;
  font-size:13px;
}

.tabs button.active{
  background:#111827;
  color:#fff;
}

/* ===== CONTENT ===== */

.content-card{
  width:92%;
  max-width:1100px;
  margin:auto;

  border:1px solid #f1f5f9;
  border-radius:12px;

  padding:20px;
}

.content-card p{
  line-height:1.8;
  color:#475569;
}

/* ===== INFO ===== */

.info-grid{
  display:grid;
  grid-template-columns:repeat(2,1fr);
  gap:12px;
}

.info-item{
  border:1px solid #e5e7eb;
  border-radius:10px;
  padding:14px;
}

.info-item span{
  display:block;
  color:#64748b;
  margin-bottom:4px;
}

/* ===== TABLE ===== */

.variant-table{
  width:100%;
  border-collapse:collapse;
}

.variant-table th{
  background:#111827;
  color:#fff;
  padding:10px;
}

.variant-table td{
  padding:10px;
  border-bottom:1px solid #e5e7eb;
  text-align:center;
}

/* ===== RELATED ===== */

.related-section{
  width:92%;
  max-width:1100px;
  margin:40px auto 0;
}

.related-grid{
  display:grid;
  grid-template-columns:repeat(auto-fit,minmax(200px,1fr));
  gap:16px;
}

.related-card{
  border:1px solid #f1f5f9;
  border-radius:12px;
  overflow:hidden;
  transition:.2s;
}

.related-card:hover{
  transform:translateY(-3px);
}

.related-card img{
  width:100%;
  height:200px;
  object-fit:cover;
}

.related-card h4{
  padding:12px;
  font-size:14px;
}

/* ===== MOBILE ===== */

@media(max-width:992px){

  .detail-container{
    grid-template-columns:1fr;
  }

  .gallery-section{
    position:static;
  }

  .main-image{
    height:350px;
  }
}

@media(max-width:768px){

  .product-title{
    font-size:20px;
  }

  .current-price{
    font-size:24px;
  }

  .info-grid{
    grid-template-columns:1fr;
  }

  .action-buttons{
    flex-direction:column;
  }

  .size-list{
    grid-template-columns:repeat(4,52px);
  }
}
</style>
