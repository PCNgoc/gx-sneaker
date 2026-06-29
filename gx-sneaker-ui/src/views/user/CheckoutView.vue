<script setup>
import { ref, computed, onMounted } from "vue"
import { useRouter } from "vue-router"
import { useCart } from "@/composables/useCart"
import { getByKhachHangId as getAddresses } from "@/services/diaChiService"
import { getByMa, getAll } from "@/services/phieuGiamGiaService"

const router = useRouter()
const { fetchCartCount } = useCart()

const checkoutItems = ref([])
const appliedVoucher = ref(null)
const isFromCart = ref(false)
const savedAddresses = ref([])

const fullName = ref("")
const phone = ref("")
const address = ref("")
const note = ref("")

const shipFee = ref(30000)

// Voucher states
const voucherCode = ref("")
const voucherError = ref("")
const voucherSuccess = ref("")
const availableVouchers = ref([])
const showVouchersList = ref(false)
const currentPage = ref(1)
const pageSize = 2

const formatMoneyCompact = (value) => {
  if (!value) return "0"
  if (value >= 1000000) {
    return (value / 1000000).toFixed(0) + "M"
  }
  if (value >= 1000) {
    return (value / 1000).toFixed(0) + "K"
  }
  return value
}

const selectVoucher = (code) => {
  voucherCode.value = code
  applyVoucher()
}

const removeVoucher = () => {
  appliedVoucher.value = null
  voucherCode.value = ""
  voucherSuccess.value = ""
  voucherError.value = ""
}

const applyVoucher = async () => {
  voucherError.value = ""
  voucherSuccess.value = ""
  if (!voucherCode.value.trim()) {
    voucherError.value = "Vui lòng nhập mã giảm giá"
    return
  }
  
  try {
    const res = await getByMa(voucherCode.value.trim())
    const voucher = res.data
    
    if (!voucher || !voucher.trangThai) {
      voucherError.value = "Mã giảm giá không hợp lệ hoặc đã hết hạn"
      return
    }
    
    // Check minimum order value
    if (totalMoney.value < voucher.giaTriDonHangToiThieu) {
      voucherError.value = `Đơn hàng tối thiểu phải từ ${formatMoney(voucher.giaTriDonHangToiThieu)} để sử dụng mã này`
      return
    }
    
    appliedVoucher.value = voucher
    voucherSuccess.value = `Áp dụng mã ${voucher.maPhieu} thành công!`
  } catch (err) {
    voucherError.value = "Mã giảm giá không tồn tại"
    appliedVoucher.value = null
  }
}

const selectAddress = (addr) => {
  fullName.value = addr.hoTenNguoiNhan || ""
  phone.value = addr.soDienThoai || ""
  address.value = `${addr.diaChiChiTiet}, ${addr.phuongXa}, ${addr.tinhThanh}`
}

const formatMoney = (value) => {
  if (!value) return "0 đ"
  return Number(value).toLocaleString("vi-VN") + " đ"
}

onMounted(async () => {
  // Load user info from localStorage if available to prefill
  const userData = localStorage.getItem("user")
  if (userData) {
    const parsedUser = JSON.parse(userData)
    fullName.value = parsedUser.hoTen || ""
    phone.value = parsedUser.soDienThoai || ""
    
    // Fetch saved addresses
    if (parsedUser.id) {
      try {
        const res = await getAddresses(parsedUser.id)
        savedAddresses.value = res.data || []
      } catch (err) {
        console.error("Lỗi tải danh sách địa chỉ đã lưu:", err)
      }
    }
  }

  // Load checkout details
  const cartCheckout = JSON.parse(localStorage.getItem("checkoutData"))
  if (cartCheckout) {
    isFromCart.value = true
    checkoutItems.value = cartCheckout.items || []
    appliedVoucher.value = cartCheckout.appliedVoucher
  } else {
    const buyNowItem = JSON.parse(localStorage.getItem("buyNowProduct"))
    if (buyNowItem) {
      isFromCart.value = false
      checkoutItems.value = [buyNowItem]
      appliedVoucher.value = null
    }
  }

  // Load vouchers list
  try {
    const voucherRes = await getAll()
    const now = new Date()
    availableVouchers.value = (voucherRes.data || []).filter(v => {
      const isStatusActive = v.trangThai === true
      const hasQty = v.soLuong === null || v.soLuong > 0
      const isStarted = !v.ngayBatDau || new Date(v.ngayBatDau) <= now
      const isNotExpired = !v.ngayKetThuc || new Date(v.ngayKetThuc) >= now
      return isStatusActive && hasQty && isStarted && isNotExpired
    })
  } catch (vErr) {
    console.error("Lỗi khi tải danh sách voucher:", vErr)
  }
})

const totalMoney = computed(() => {
  return checkoutItems.value.reduce((sum, item) => {
    return sum + (Number(item.price) * Number(item.quantity))
  }, 0)
})

const discountAmount = computed(() => {
  if (!appliedVoucher.value) return 0
  const v = appliedVoucher.value
  if (v.loaiGiamGia) {
    let calc = (totalMoney.value * v.giaTriGiam) / 100
    if (v.giaTriGiamToiDa > 0 && calc > v.giaTriGiamToiDa) {
      calc = v.giaTriGiamToiDa
    }
    return calc
  } else {
    return v.giaTriGiam
  }
})

const finalTotal = computed(() => {
  const total = totalMoney.value - discountAmount.value + shipFee.value
  return total < 0 ? 0 : total
})

const getPotentialDiscount = (v, total) => {
  if (v.loaiGiamGia) {
    let calc = (total * v.giaTriGiam) / 100
    if (v.giaTriGiamToiDa > 0 && calc > v.giaTriGiamToiDa) {
      calc = v.giaTriGiamToiDa
    }
    return calc
  }
  return v.giaTriGiam
}

const sortedVouchers = computed(() => {
  const total = totalMoney.value
  return [...availableVouchers.value].sort((a, b) => {
    const discA = getPotentialDiscount(a, total)
    const discB = getPotentialDiscount(b, total)
    return discB - discA // highest discount first
  })
})

const totalPages = computed(() => {
  const count = sortedVouchers.value.length
  return count > 0 ? Math.ceil(count / pageSize) : 1
})

const paginatedVouchers = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  const end = start + pageSize
  return sortedVouchers.value.slice(start, end)
})

const toggleVouchersList = () => {
  showVouchersList.value = !showVouchersList.value
  currentPage.value = 1
}

const placeOrder = async () => {
  if (!fullName.value.trim()) {
    alert("Vui lòng nhập họ tên")
    return
  }

  if (!phone.value.trim()) {
    alert("Vui lòng nhập số điện thoại")
    return
  }

  if (!address.value.trim()) {
    alert("Vui lòng nhập địa chỉ nhận hàng")
    return
  }

  console.log({
    customerName: fullName.value,
    phone: phone.value,
    address: address.value,
    note: note.value,
    items: checkoutItems.value,
    voucher: appliedVoucher.value,
    discountAmount: discountAmount.value,
    shipFee: shipFee.value,
    totalMoney: finalTotal.value
  })

  alert("Đặt hàng thành công! Xin cảm ơn quý khách.")
  
  // Clear checkout storage
  localStorage.removeItem("checkoutData")
  localStorage.removeItem("buyNowProduct")
  
  // If it was checked out from the database cart, we should clear the cart in DB
  const userData = localStorage.getItem("user")
  if (userData && isFromCart.value) {
    try {
      const parsedUser = JSON.parse(userData)
      // Clear DB cart count using our composable/API
      await fetchCartCount(parsedUser.id)
    } catch (e) {
      console.error(e)
    }
  }

  router.push("/")
}
</script>

<template>
  <div class="checkout-page">
    <h1>🛒 Thanh toán đơn hàng</h1>

    <div class="checkout-wrapper">
      <!-- Thông tin nhận hàng -->
      <div class="customer-info">
        <h2>Thông tin nhận hàng</h2>

        <!-- Saved Address Picker -->
        <div v-if="savedAddresses.length > 0" class="saved-addresses-picker">
          <label class="picker-label">🔑 Chọn nhanh địa chỉ nhận hàng đã lưu:</label>
          <div class="address-chips">
            <button
              v-for="addr in savedAddresses"
              :key="addr.id"
              type="button"
              class="address-chip"
              @click="selectAddress(addr)"
            >
              <strong>{{ addr.hoTenNguoiNhan }}</strong> - {{ addr.soDienThoai }}
              <span class="chip-text">{{ addr.diaChiChiTiet }}, {{ addr.phuongXa }}, {{ addr.tinhThanh }}</span>
            </button>
          </div>
        </div>

        <div class="form-group">
          <label>Họ và tên</label>
          <input
            v-model="fullName"
            type="text"
            placeholder="Nhập họ tên người nhận"
          >
        </div>

        <div class="form-group">
          <label>Số điện thoại</label>
          <input
            v-model="phone"
            type="text"
            placeholder="Nhập số điện thoại"
          >
        </div>

        <div class="form-group">
          <label>Địa chỉ nhận hàng</label>
          <textarea
            v-model="address"
            rows="4"
            placeholder="Nhập địa chỉ chi tiết (Số nhà, tên đường, phường/xã, quận/huyện, tỉnh/thành phố)"
          />
        </div>

        <div class="form-group">
          <label>Ghi chú</label>
          <textarea
            v-model="note"
            rows="3"
            placeholder="Ghi chú thêm cho shipper hoặc cửa hàng..."
          />
        </div>
      </div>

      <!-- Đơn hàng -->
      <div class="order-summary">
        <h2>Đơn hàng của bạn</h2>

        <div class="product-list">
          <div
            v-for="(item, idx) in checkoutItems"
            :key="idx"
            class="product-item"
          >
            <img
              :src="item.image ? `/images/${item.image}` : '/images/banner1.png'"
              alt="Product Image"
            >

            <div class="product-info">
              <h3>{{ item.productName }}</h3>
              <p>Màu: <strong>{{ item.color }}</strong></p>
              <p>Size: <strong>{{ item.size }}</strong></p>
              <p>Số lượng: <strong>{{ item.quantity }}</strong></p>
              <p>Giá: <strong>{{ formatMoney(item.price) }}</strong></p>
            </div>
          </div>
        </div>

        <!-- Voucher Section in Checkout -->
        <div class="voucher-section-checkout">
          <h3>🎟️ Áp dụng Mã Giảm Giá</h3>
          <div class="voucher-input-group">
            <input v-model="voucherCode" placeholder="Nhập mã giảm giá..." @keyup.enter="applyVoucher" />
            <button @click="applyVoucher">Áp dụng</button>
          </div>
          <p v-if="voucherError" class="voucher-error">{{ voucherError }}</p>
          <p v-if="voucherSuccess" class="voucher-success">{{ voucherSuccess }}</p>

          <!-- Suggested Vouchers trigger line -->
          <div v-if="availableVouchers.length > 0" class="voucher-suggest-trigger" @click="toggleVouchersList">
            <span class="trigger-icon">💡</span>
            <span class="trigger-text">
              Bạn có <strong>{{ availableVouchers.length }}</strong> mã giảm giá khả dụng.
              <span class="trigger-action">{{ showVouchersList ? 'Thu gọn' : 'Xem gợi ý' }}</span>
            </span>
            <span class="trigger-arrow" :class="{ 'rotated': showVouchersList }">▼</span>
          </div>

          <!-- Suggested Vouchers list below input (collapsible and paginated) -->
          <div v-if="showVouchersList && paginatedVouchers.length > 0" class="suggested-vouchers-container">
            <div class="vouchers-list-scroll">
              <div 
                v-for="v in paginatedVouchers" 
                :key="v.id" 
                class="voucher-suggest-card"
                :class="{ 
                  'eligible': totalMoney >= v.giaTriDonHangToiThieu, 
                  'applied': appliedVoucher?.id === v.id 
                }"
              >
                <!-- Left ticket-like edge -->
                <div class="voucher-card-left">
                  <span class="voucher-percent">
                    {{ v.loaiGiamGia ? `${v.giaTriGiam}%` : formatMoneyCompact(v.giaTriGiam) }}
                  </span>
                  <span class="voucher-off-lbl">GIẢM</span>
                </div>
                
                <!-- Dotted divider -->
                <div class="voucher-card-divider"></div>
                
                <!-- Right main content -->
                <div class="voucher-card-right">
                  <div class="voucher-header-info">
                    <span class="voucher-code-badge">{{ v.maPhieu }}</span>
                    <span v-if="appliedVoucher?.id === v.id" class="applied-badge">Đang áp dụng</span>
                  </div>
                  <p class="voucher-desc" :title="v.tenPhieu">{{ v.tenPhieu }}</p>
                  <p class="voucher-min-spend">Đơn tối thiểu: {{ formatMoney(v.giaTriDonHangToiThieu) }}</p>
                  
                  <!-- Progress bar if not eligible yet -->
                  <div v-if="totalMoney < v.giaTriDonHangToiThieu" class="spend-progress-bar">
                    <div class="spend-progress-fill" :style="{ width: `${Math.min(100, (totalMoney / v.giaTriDonHangToiThieu) * 100)}%` }"></div>
                    <span class="spend-progress-text">Mua thêm {{ formatMoney(v.giaTriDonHangToiThieu - totalMoney) }} để dùng</span>
                  </div>

                  <div class="voucher-actions">
                    <button 
                      v-if="totalMoney >= v.giaTriDonHangToiThieu && appliedVoucher?.id !== v.id" 
                      class="btn-apply-voucher" 
                      @click="selectVoucher(v.maPhieu)"
                    >
                      Áp dụng
                    </button>
                    <button 
                      v-else-if="appliedVoucher?.id === v.id" 
                      class="btn-remove-applied-voucher" 
                      @click="removeVoucher"
                    >
                      Hủy
                    </button>
                    <span v-else class="locked-voucher-lbl">Chưa đủ đ/k</span>
                  </div>
                </div>
              </div>
            </div>

            <!-- Pagination Controls -->
            <div v-if="totalPages > 1" class="voucher-pagination">
              <button 
                :disabled="currentPage === 1" 
                class="btn-page-nav" 
                @click="currentPage--"
              >
                ◀
              </button>
              <span class="page-indicator">Trang {{ currentPage }} / {{ totalPages }}</span>
              <button 
                :disabled="currentPage === totalPages" 
                class="btn-page-nav" 
                @click="currentPage++"
              >
                ▶
              </button>
            </div>
          </div>
        </div>

        <hr>

        <div class="price-row">
          <span>Tạm tính</span>
          <strong>{{ formatMoney(totalMoney) }}</strong>
        </div>

        <div v-if="discountAmount > 0" class="price-row discount-row">
          <span>Mã giảm giá áp dụng ({{ appliedVoucher?.maPhieu }})</span>
          <strong class="discount-val">- {{ formatMoney(discountAmount) }}</strong>
        </div>

        <div class="price-row">
          <span>Phí vận chuyển</span>
          <strong>{{ formatMoney(shipFee) }}</strong>
        </div>

        <hr>

        <div class="price-row total-row">
          <span>Tổng cộng</span>
          <strong class="grand-total">{{ formatMoney(finalTotal) }}</strong>
        </div>

        <button
          class="btn-order"
          @click="placeOrder"
        >
          XÁC NHẬN ĐẶT HÀNG
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.checkout-page {
  max-width: 1200px;
  margin: 40px auto;
  padding: 0 20px;
  font-family: Inter, sans-serif;
}

.checkout-page h1 {
  margin-bottom: 30px;
  font-size: 32px;
  font-weight: 800;
  color: #1e293b;
}

.checkout-wrapper {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 30px;
}

.customer-info,
.order-summary {
  background: #fff;
  padding: 28px;
  border-radius: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05);
  border: 1px solid rgba(0, 0, 0, 0.04);
}

.customer-info h2,
.order-summary h2 {
  font-size: 22px;
  font-weight: 800;
  color: #1e293b;
  margin-bottom: 24px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 600;
  color: #475569;
  font-size: 14px;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid #cbd5e1;
  border-radius: 10px;
  font-size: 14px;
  font-family: inherit;
  transition: all 0.3s;
}

.form-group input:focus,
.form-group textarea:focus {
  border-color: #64748b;
  outline: none;
  box-shadow: 0 0 0 3px rgba(100, 116, 139, 0.1);
}

.product-list {
  max-height: 400px;
  overflow-y: auto;
  padding-right: 8px;
}

.product-item {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f1f5f9;
}

.product-item:last-child {
  border-bottom: none;
  margin-bottom: 0;
  padding-bottom: 0;
}

.product-item img {
  width: 90px;
  height: 90px;
  object-fit: cover;
  border-radius: 12px;
  border: 1px solid #e2e8f0;
}

.product-info h3 {
  font-size: 16px;
  font-weight: 700;
  color: #1e293b;
  margin-bottom: 6px;
}

.product-info p {
  margin-bottom: 4px;
  font-size: 13px;
  color: #64748b;
}

.price-row {
  display: flex;
  justify-content: space-between;
  margin: 14px 0;
  font-size: 15px;
  color: #475569;
}

.discount-row {
  color: #059669;
  background: #ecfdf5;
  padding: 8px 12px;
  border-radius: 8px;
}

.discount-val {
  color: #10b981;
}

.total-row {
  font-size: 18px;
  color: #1e293b;
}

.grand-total {
  font-size: 24px;
  color: #ef4444;
  font-weight: 800;
}

.btn-order {
  width: 100%;
  margin-top: 20px;
  padding: 16px;
  border: none;
  border-radius: 12px;
  background: linear-gradient(135deg, #ef4444, #dc2626);
  color: white;
  font-size: 16px;
  font-weight: 700;
  cursor: pointer;
  box-shadow: 0 10px 25px rgba(239, 68, 68, 0.25);
  transition: all 0.3s;
}

.btn-order:hover {
  transform: translateY(-2px);
  box-shadow: 0 15px 30px rgba(239, 68, 68, 0.35);
}

@media (max-width: 992px) {
  .checkout-wrapper {
    grid-template-columns: 1fr;
  }
}

.saved-addresses-picker {
  margin-bottom: 24px;
  background: #f8fafc;
  padding: 16px;
  border-radius: 12px;
  border: 1px solid #e2e8f0;
}

.picker-label {
  display: block;
  font-weight: 700;
  font-size: 14px;
  color: #475569;
  margin-bottom: 12px !important;
}

.address-chips {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.address-chip {
  text-align: left;
  background: #ffffff;
  border: 1px solid #cbd5e1;
  padding: 12px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
  width: 100%;
}

.address-chip:hover {
  border-color: #0ea5e9;
  background: #f0f9ff;
  box-shadow: 0 4px 12px rgba(14, 165, 233, 0.05);
}

.chip-text {
  display: block;
  font-size: 13px;
  color: #64748b;
  margin-top: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* Voucher Apply Section in Checkout */
.voucher-section-checkout {
  background: #f8fafc;
  padding: 16px;
  border-radius: 12px;
  border: 1px solid #e2e8f0;
  margin-top: 15px;
  margin-bottom: 15px;
}

.voucher-section-checkout h3 {
  font-size: 14px;
  font-weight: 700;
  color: #1e293b;
  margin-bottom: 12px;
  display: flex;
  align-items: center;
  gap: 6px;
}

.voucher-input-group {
  display: flex;
  gap: 8px;
}

.voucher-input-group input {
  flex: 1;
  padding: 8px 12px;
  border: 1px solid #cbd5e1;
  border-radius: 8px;
  font-size: 13px;
  outline: none;
  transition: all 0.2s;
  background: #ffffff;
}

.voucher-input-group input:focus {
  border-color: #ef4444;
  box-shadow: 0 0 0 3px rgba(239, 68, 68, 0.1);
}

.voucher-input-group button {
  background: #1e293b;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 8px;
  font-weight: 600;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s;
}

.voucher-input-group button:hover {
  background: #ef4444;
}

.voucher-error {
  color: #ef4444;
  font-size: 12px;
  margin-top: 8px;
  font-weight: 600;
}

.voucher-success {
  color: #10b981;
  font-size: 12px;
  margin-top: 8px;
  font-weight: 600;
}

.suggested-vouchers-container {
  margin-top: 15px;
  border-top: 1px dashed #cbd5e1;
  padding-top: 15px;
}

.suggested-title {
  font-size: 13px;
  font-weight: 700;
  color: #475569;
  margin-bottom: 12px;
}

.vouchers-list-scroll {
  display: flex;
  flex-direction: column;
  gap: 12px;
  max-height: 220px;
  overflow-y: auto;
  padding-right: 4px;
}

/* Custom Scrollbar */
.vouchers-list-scroll::-webkit-scrollbar {
  width: 5px;
}
.vouchers-list-scroll::-webkit-scrollbar-track {
  background: #f1f5f9;
  border-radius: 4px;
}
.vouchers-list-scroll::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 4px;
}

.voucher-suggest-card {
  display: flex;
  background: #ffffff;
  border-radius: 10px;
  overflow: hidden;
  border: 1px solid #e2e8f0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.02);
  transition: all 0.3s ease;
  position: relative;
}

.voucher-suggest-card::before,
.voucher-suggest-card::after {
  content: "";
  position: absolute;
  left: 24%;
  width: 10px;
  height: 10px;
  background: #f8fafc; /* Match container bg */
  border-radius: 50%;
  z-index: 2;
}
.voucher-suggest-card::before {
  top: -5px;
  box-shadow: inset 0 -1px 1px rgba(0,0,0,0.05);
}
.voucher-suggest-card::after {
  bottom: -5px;
  box-shadow: inset 0 1px 1px rgba(0,0,0,0.05);
}

.voucher-suggest-card.applied {
  border-color: #10b981;
  background: #f0fdf4;
}

.voucher-suggest-card.applied::before,
.voucher-suggest-card.applied::after {
  background: #f8fafc;
}

.voucher-card-left {
  width: 25%;
  background: #cbd5e1;
  color: #64748b;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  font-weight: 800;
  padding: 10px;
  min-height: 80px;
  transition: all 0.3s ease;
}

.voucher-suggest-card.eligible .voucher-card-left {
  background: linear-gradient(135deg, #ef4444, #f43f5e);
  color: white;
}

.voucher-suggest-card.applied .voucher-card-left {
  background: linear-gradient(135deg, #10b981, #059669);
  color: white;
}

.voucher-percent {
  font-size: 16px;
  text-shadow: 0 1px 2px rgba(0,0,0,0.1);
}

.voucher-off-lbl {
  font-size: 9px;
  opacity: 0.8;
  letter-spacing: 1px;
  margin-top: 2px;
}

.voucher-card-divider {
  border-left: 2px dashed #e2e8f0;
  margin: 0;
  background: transparent;
  z-index: 1;
}

.voucher-suggest-card.applied .voucher-card-divider {
  border-left-color: #a7f3d0;
}

.voucher-card-right {
  flex: 1;
  padding: 10px 12px 10px 16px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.voucher-header-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}

.voucher-code-badge {
  background: #f1f5f9;
  color: #334155;
  padding: 2px 6px;
  border-radius: 4px;
  font-family: monospace;
  font-weight: 700;
  font-size: 12px;
  border: 1px solid #e2e8f0;
}

.voucher-suggest-card.applied .voucher-code-badge {
  background: #d1fae5;
  color: #065f46;
  border-color: #a7f3d0;
}

.applied-badge {
  background: #10b981;
  color: white;
  font-size: 9px;
  font-weight: 700;
  padding: 2px 6px;
  border-radius: 20px;
}

.voucher-desc {
  font-size: 12px;
  font-weight: 600;
  color: #1e293b;
  margin: 3px 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.voucher-min-spend {
  font-size: 11px;
  color: #64748b;
  margin: 2px 0 6px 0;
}

.spend-progress-bar {
  background: #e2e8f0;
  height: 4px;
  border-radius: 2px;
  position: relative;
  margin: 4px 0 8px 0;
}

.spend-progress-fill {
  background: #f43f5e;
  height: 100%;
  border-radius: 2px;
}

.spend-progress-text {
  position: absolute;
  left: 0;
  top: 6px;
  font-size: 9px;
  color: #ef4444;
  font-weight: 600;
  white-space: nowrap;
}

.voucher-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 4px;
}

.btn-apply-voucher {
  background: #1e293b;
  color: white;
  border: none;
  padding: 4px 10px;
  border-radius: 6px;
  font-size: 11px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-apply-voucher:hover {
  background: #ef4444;
  transform: scale(1.05);
}

.btn-remove-applied-voucher {
  background: #fee2e2;
  color: #ef4444;
  border: 1px solid #fca5a5;
  padding: 3px 10px;
  border-radius: 6px;
  font-size: 11px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-remove-applied-voucher:hover {
  background: #ef4444;
  color: white;
  border-color: #ef4444;
}

.locked-voucher-lbl {
  font-size: 10px;
  color: #94a3b8;
  font-weight: 600;
  background: #f1f5f9;
  padding: 2px 6px;
  border-radius: 4px;
}

/* Suggested Vouchers Trigger Line */
.voucher-suggest-trigger {
  display: flex;
  align-items: center;
  gap: 8px;
  background: #f0fdf4;
  border: 1px solid #bbf7d0;
  padding: 8px 12px;
  border-radius: 8px;
  margin-top: 12px;
  cursor: pointer;
  transition: all 0.2s ease;
  user-select: none;
}

.voucher-suggest-trigger:hover {
  background: #dcfce7;
  border-color: #86efac;
}

.trigger-icon {
  font-size: 15px;
}

.trigger-text {
  flex: 1;
  font-size: 12.5px;
  color: #166534;
}

.trigger-action {
  color: #059669;
  text-decoration: underline;
  margin-left: 6px;
  font-weight: 700;
}

.trigger-arrow {
  font-size: 10px;
  color: #166534;
  transition: transform 0.2s ease;
}

.trigger-arrow.rotated {
  transform: rotate(180deg);
}

/* Pagination Controls */
.voucher-pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
  margin-top: 12px;
  padding-top: 10px;
  border-top: 1px solid #f1f5f9;
}

.btn-page-nav {
  background: #ffffff;
  border: 1px solid #cbd5e1;
  color: #64748b;
  width: 26px;
  height: 26px;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 10px;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-page-nav:hover:not(:disabled) {
  background: #f1f5f9;
  color: #1e293b;
  border-color: #94a3b8;
}

.btn-page-nav:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

.page-indicator {
  font-size: 12px;
  font-weight: 600;
  color: #475569;
}
</style>
