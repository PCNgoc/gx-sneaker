<script setup>
import { ref, computed, onMounted } from "vue"
import { datHang, apDungMaGiamGia } from "@/services/HoaDonService"
import { useRouter } from "vue-router"
import { useCart } from "@/composables/useCart"
import { getByKhachHangId as getAddresses } from "@/services/diaChiService"

const router = useRouter()
const { fetchCartCount } = useCart()

const checkoutItems = ref([])
const isFromCart = ref(false)
const savedAddresses = ref([])

const fullName = ref("")
const phone = ref("")
const address = ref("")
const note = ref("")

const paymentMethod = ref("COD")
const shipFee = ref(30000)
const loading = ref(false)
const qrConfirmed = ref(false)

const couponCode = ref("")
const appliedCoupon = ref(null)
const discountAmount = ref(0)
const couponLoading = ref(false)

const bankInfo = {
  bankId: "MB",
  accountNo: "2601200488888",
  accountName: "NGUYEN VAN LUAN",
}

const transferContent = computed(() => {
  const phoneText = phone.value.trim() || "KHACHHANG"
  return `GX${phoneText}`
})

const qrUrl = computed(() => {
  const amount = Number(finalTotal.value || 0)
  const addInfo = encodeURIComponent(transferContent.value)
  const accountName = encodeURIComponent(bankInfo.accountName)

  return `https://img.vietqr.io/image/${bankInfo.bankId}-${bankInfo.accountNo}-compact2.png?amount=${amount}&addInfo=${addInfo}&accountName=${accountName}`
})

const formatMoney = (value) => {
  return Number(value || 0).toLocaleString("vi-VN") + " đ"
}

const getCurrentUser = () => {
  try {
    const userData = localStorage.getItem("user")
    return userData ? JSON.parse(userData) : null
  } catch (e) {
    console.error("Lỗi parse user:", e)
    return null
  }
}

onMounted(async () => {
  const user = getCurrentUser()

  if (user) {
    fullName.value = user.hoTen || user.fullName || ""
    phone.value = user.soDienThoai || user.phone || ""

    if (user.id) {
      try {
        const res = await getAddresses(user.id)
        savedAddresses.value = res.data || []
      } catch (e) {
        console.error("Lỗi tải địa chỉ:", e)
      }
    }
  }

  const cartCheckout = localStorage.getItem("checkoutData")
  const buyNowData = localStorage.getItem("buyNowProduct")

  if (cartCheckout) {
    const parsed = JSON.parse(cartCheckout)
    isFromCart.value = true
    checkoutItems.value = parsed.items || []
  } else if (buyNowData) {
    isFromCart.value = false
    checkoutItems.value = [JSON.parse(buyNowData)]
  } else {
    alert("Không có sản phẩm để thanh toán")
    router.push("/products")
  }
})

const getItemPrice = (item) => {
  return Number(
    item?.price ||
    item?.giaBan ||
    item?.donGia ||
    item?.gia ||
    0
  )
}

const getItemQuantity = (item) => {
  return Number(
    item?.quantity ||
    item?.soLuong ||
    1
  )
}

const totalMoney = computed(() => {
  return checkoutItems.value.reduce((sum, item) => {
    return sum + getItemPrice(item) * getItemQuantity(item)
  }, 0)
})

const finalTotal = computed(() => {
  return Math.max(totalMoney.value - discountAmount.value + shipFee.value, 0)
})

const selectAddress = (addr) => {
  fullName.value =
    addr.hoTenNguoiNhan ||
    addr.tenNguoiNhan ||
    addr.hoTen ||
    ""

  phone.value =
    addr.soDienThoai ||
    addr.phone ||
    ""

  const parts = [
    addr.diaChiChiTiet,
    addr.phuongXa,
    addr.quanHuyen,
    addr.tinhThanh,
  ].filter(Boolean)

  address.value = parts.join(", ")
}

const applyCoupon = async () => {
  if (!couponCode.value.trim()) {
    alert("Vui lòng nhập mã giảm giá")
    return
  }

  if (!checkoutItems.value.length) {
    alert("Không có sản phẩm để áp dụng mã")
    return
  }

  try {
    couponLoading.value = true

    const res = await apDungMaGiamGia({
      maPhieuGiamGia: couponCode.value.trim(),
      tongTienHang: totalMoney.value,
    })

    appliedCoupon.value = res.data
    discountAmount.value = Number(res.data.soTienGiam || 0)
    couponCode.value = res.data.maPhieuGiamGia || couponCode.value.trim()

    alert(res.data.message || "Áp dụng mã giảm giá thành công")
  } catch (e) {
    appliedCoupon.value = null
    discountAmount.value = 0

    alert(
      e.response?.data?.message ||
      e.response?.data ||
      "Mã giảm giá không hợp lệ"
    )
  } finally {
    couponLoading.value = false
  }
}

const removeCoupon = () => {
  couponCode.value = ""
  appliedCoupon.value = null
  discountAmount.value = 0
}

const getChiTietSanPhamId = (item) => {
  return (
    item?.detailId ||
    item?.chiTietSanPhamId ||
    item?.idChiTietSanPham ||
    item?.id_chi_tiet_san_pham ||
    item?.idChiTietSP ||
    item?.chiTietSanPham?.id ||
    item?.id
  )
}

const getImageUrl = (image) => {
  if (!image) return "/images/no-image.png"

  if (image.startsWith("http")) return image

  if (image.startsWith("/")) return image

  return `/images/${image}`
}

const getItemImage = (item) => {
  return (
    item?.image ||
    item?.hinhAnh ||
    item?.anh ||
    item?.imageUrl ||
    item?.sanPham?.hinhAnh ||
    ""
  )
}

const getItemName = (item) => {
  return (
    item?.productName ||
    item?.tenSanPham ||
    item?.name ||
    item?.sanPham?.tenSanPham ||
    "Sản phẩm"
  )
}

const getItemColor = (item) => {
  return (
    item?.color ||
    item?.mauSac ||
    item?.tenMau ||
    item?.mau ||
    "Không có"
  )
}

const getItemSize = (item) => {
  return (
    item?.size ||
    item?.kichCo ||
    item?.tenSize ||
    "Không có"
  )
}

const validateForm = () => {
  if (!fullName.value.trim()) {
    alert("Vui lòng nhập họ tên người nhận")
    return false
  }

  if (!phone.value.trim()) {
    alert("Vui lòng nhập số điện thoại")
    return false
  }

  const phoneRegex = /^(0|\+84)[0-9]{9,10}$/
  if (!phoneRegex.test(phone.value.trim())) {
    alert("Số điện thoại không hợp lệ")
    return false
  }

  if (!address.value.trim()) {
    alert("Vui lòng nhập địa chỉ nhận hàng")
    return false
  }

  return true
}

const getCustomerId = () => {
  const user = getCurrentUser()

  return (
    localStorage.getItem("userId") ||
    localStorage.getItem("idKhachHang") ||
    user?.id ||
    1
  )
}

const placeOrder = async () => {
  if (!checkoutItems.value.length) {
    alert("Không có sản phẩm")
    return
  }

  if (!validateForm()) return

  if (paymentMethod.value === "QR" && !qrConfirmed.value) {
    alert("Vui lòng xác nhận rằng bạn đã thực hiện chuyển khoản và chờ admin kiểm tra")
    return
  }

  const invalidItem = checkoutItems.value.find(item => !getChiTietSanPhamId(item))

  if (invalidItem) {
    alert("Có sản phẩm bị thiếu ID chi tiết sản phẩm")
    console.log("Sản phẩm thiếu ID chi tiết:", invalidItem)
    return
  }

  try {
    loading.value = true

    const request = {
      idKhachHang: Number(getCustomerId()),
      tenNguoiNhan: fullName.value.trim(),
      soDienThoai: phone.value.trim(),
      diaChi: address.value.trim(),
      ghiChu:
        paymentMethod.value === "QR"
          ? `[QR_MB_CHO_XAC_NHAN] ${note.value.trim()}`
          : `[COD_CHO_XAC_NHAN] ${note.value.trim()}`,
      maPhieuGiamGia: appliedCoupon.value
        ? appliedCoupon.value.maPhieuGiamGia || couponCode.value.trim()
        : null,
      items: checkoutItems.value.map(item => ({
        chiTietSanPhamId: Number(getChiTietSanPhamId(item)),
        soLuong: getItemQuantity(item),
      })),
    }

    console.log("===== REQUEST DAT HANG =====")
    console.log(request)

    const res = await datHang(request)

    console.log("===== DAT HANG RESPONSE =====")
    console.log(res.data)

    localStorage.removeItem("buyNowProduct")
    localStorage.removeItem("checkoutData")

    if (isFromCart.value) {
      const user = getCurrentUser()

      if (user?.id) {
        try {
          await fetchCartCount(user.id)
        } catch (e) {
          console.error("Lỗi cập nhật số lượng giỏ hàng:", e)
        }
      }
    }

    router.push({
      path: `/order-success/${res.data.id}`,
      query: {
        maHoaDon: res.data.maHoaDon,
        payment: paymentMethod.value,
      },
    })
  } catch (e) {
    console.error(e)
    console.log(e.response)
    console.log(e.response?.data)

    alert(
      e.response?.data?.message ||
      e.response?.data ||
      "Đặt hàng thất bại"
    )
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="checkout-page">
    <div class="checkout-header">
      <button class="back-btn" @click="router.back()">
        ← Quay lại
      </button>

      <div>
        <h1>Thanh toán</h1>
        <p>Hoàn tất thông tin để đặt hàng</p>
      </div>
    </div>

    <div class="checkout-wrapper">
      <div class="left-content">
        <section class="card">
          <div class="card-title">
            <span class="step">1</span>
            <div>
              <h2>Thông tin nhận hàng</h2>
              <p>Vui lòng nhập chính xác thông tin giao hàng</p>
            </div>
          </div>

          <div v-if="savedAddresses.length > 0" class="saved-addresses-picker">
            <label class="picker-label">Chọn nhanh địa chỉ đã lưu</label>

            <div class="address-chips">
              <button
                v-for="addr in savedAddresses"
                :key="addr.id"
                type="button"
                class="address-chip"
                @click="selectAddress(addr)"
              >
                <strong>
                  {{ addr.hoTenNguoiNhan || addr.tenNguoiNhan || addr.hoTen }}
                </strong>
                -
                {{ addr.soDienThoai }}

                <span class="chip-text">
                  {{ addr.diaChiChiTiet }},
                  {{ addr.phuongXa }},
                  {{ addr.quanHuyen }},
                  {{ addr.tinhThanh }}
                </span>
              </button>
            </div>
          </div>

          <div class="form-grid">
            <div class="form-group">
              <label>Họ và tên người nhận <span>*</span></label>
              <input
                v-model="fullName"
                type="text"
                placeholder="Ví dụ: Nguyễn Văn A"
              >
            </div>

            <div class="form-group">
              <label>Số điện thoại <span>*</span></label>
              <input
                v-model="phone"
                type="text"
                placeholder="Ví dụ: 0987654321"
              >
            </div>
          </div>

          <div class="form-group">
            <label>Địa chỉ nhận hàng <span>*</span></label>
            <textarea
              v-model="address"
              rows="4"
              placeholder="Số nhà, đường, phường/xã, quận/huyện, tỉnh/thành phố"
            />
          </div>

          <div class="form-group">
            <label>Ghi chú</label>
            <textarea
              v-model="note"
              rows="3"
              placeholder="Ghi chú cho shop hoặc đơn vị vận chuyển"
            />
          </div>
        </section>

        <section class="card">
          <div class="card-title">
            <span class="step">2</span>
            <div>
              <h2>Phương thức thanh toán</h2>
              <p>Chọn hình thức thanh toán phù hợp</p>
            </div>
          </div>

          <div class="payment-list">
            <label
              class="payment-item"
              :class="{ active: paymentMethod === 'COD' }"
            >
              <input
                v-model="paymentMethod"
                type="radio"
                value="COD"
              >
              <div class="payment-icon">💵</div>
              <div>
                <h3>Thanh toán khi nhận hàng</h3>
                <p>Khách hàng thanh toán trực tiếp cho shipper khi nhận hàng.</p>
              </div>
            </label>

            <label
              class="payment-item"
              :class="{ active: paymentMethod === 'QR' }"
            >
              <input
                v-model="paymentMethod"
                type="radio"
                value="QR"
              >
              <div class="payment-icon">🏦</div>
              <div>
                <h3>Chuyển khoản MB Bank bằng QR</h3>
                <p>Quét mã bằng app ngân hàng và chuyển khoản thật vào tài khoản shop.</p>
              </div>
            </label>
          </div>

          <div v-if="paymentMethod === 'QR'" class="qr-box">
            <h3>Quét mã VietQR để thanh toán</h3>

            <div class="qr-content">
              <img
                :src="qrUrl"
                alt="QR thanh toán"
                class="qr-image"
              >

              <div class="bank-detail">
                <p>Ngân hàng: <strong>MB Bank</strong></p>
                <p>Số tài khoản: <strong>{{ bankInfo.accountNo }}</strong></p>
                <p>Chủ tài khoản: <strong>{{ bankInfo.accountName }}</strong></p>
                <p>Số tiền: <strong class="money">{{ formatMoney(finalTotal) }}</strong></p>
                <p>Nội dung CK: <strong>{{ transferContent }}</strong></p>
              </div>
            </div>

            <label class="confirm-transfer">
              <input
                v-model="qrConfirmed"
                type="checkbox"
              >
              Tôi đã thực hiện chuyển khoản và hiểu rằng đơn hàng sẽ chờ admin xác nhận
            </label>
          </div>
        </section>
      </div>

      <aside class="order-summary">
        <h2>Đơn hàng của bạn</h2>

        <div
          v-for="(item, index) in checkoutItems"
          :key="index"
          class="product-box"
        >
          <img
            :src="getImageUrl(getItemImage(item))"
            alt="Ảnh sản phẩm"
          >

          <div class="product-info">
            <h3>{{ getItemName(item) }}</h3>

            <div class="variant">
              <span>Màu: <b>{{ getItemColor(item) }}</b></span>
              <span>Size: <b>{{ getItemSize(item) }}</b></span>
            </div>

            <div class="quantity-price">
              <span>x{{ getItemQuantity(item) }}</span>
              <strong>{{ formatMoney(getItemPrice(item)) }}</strong>
            </div>
          </div>
        </div>

        <div class="coupon-box">
          <label>Mã giảm giá</label>

          <div class="coupon-input">
            <input
              v-model="couponCode"
              type="text"
              placeholder="Nhập mã giảm giá"
              :disabled="!!appliedCoupon"
              @keyup.enter="applyCoupon"
            >

            <button
              v-if="!appliedCoupon"
              type="button"
              :disabled="couponLoading"
              @click="applyCoupon"
            >
              {{ couponLoading ? "Đang kiểm tra..." : "Áp dụng" }}
            </button>

            <button
              v-else
              type="button"
              class="btn-remove-coupon"
              @click="removeCoupon"
            >
              Hủy
            </button>
          </div>

          <p v-if="appliedCoupon" class="coupon-success">
            Đã áp dụng: {{ appliedCoupon.maPhieuGiamGia }}
            - giảm {{ formatMoney(discountAmount) }}
          </p>
        </div>

        <div class="divider"></div>

        <div class="price-row">
          <span>Tạm tính</span>
          <strong>{{ formatMoney(totalMoney) }}</strong>
        </div>

        <div class="price-row">
          <span>Phí vận chuyển</span>
          <strong>{{ formatMoney(shipFee) }}</strong>
        </div>

        <div class="price-row">
          <span>Giảm giá</span>
          <strong class="discount-text">-{{ formatMoney(discountAmount) }}</strong>
        </div>

        <div class="divider"></div>

        <div class="price-row total-row">
          <span>Tổng thanh toán</span>
          <strong>{{ formatMoney(finalTotal) }}</strong>
        </div>

        <button
          class="btn-order"
          :disabled="loading"
          @click="placeOrder"
        >
          {{
            loading
              ? "ĐANG XỬ LÝ..."
              : paymentMethod === "QR"
                ? "GỬI ĐƠN - CHỜ ADMIN XÁC NHẬN THANH TOÁN"
                : "ĐẶT HÀNG - CHỜ XÁC NHẬN"
          }}
        </button>

        <p class="policy-text">
          Bằng việc đặt hàng, bạn đồng ý với chính sách mua hàng của GX Sneaker.
        </p>
      </aside>
    </div>
  </div>
</template>

<style scoped>
.checkout-page {
  min-height: 100vh;
  padding: 40px 24px;
  background:
    radial-gradient(circle at top left, #fee2e2, transparent 30%),
    linear-gradient(135deg, #f8fafc, #eef2ff);
}

.checkout-header {
  max-width: 1200px;
  margin: 0 auto 28px;
  display: flex;
  align-items: center;
  gap: 18px;
}

.back-btn {
  border: none;
  background: white;
  color: #111827;
  padding: 12px 18px;
  border-radius: 14px;
  font-weight: 700;
  cursor: pointer;
  box-shadow: 0 10px 25px rgba(15, 23, 42, 0.08);
}

.checkout-header h1 {
  font-size: 38px;
  font-weight: 800;
  color: #111827;
  margin: 0;
}

.checkout-header p {
  margin-top: 4px;
  color: #6b7280;
}

.checkout-wrapper {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: minmax(0, 1.6fr) 430px;
  gap: 28px;
  align-items: flex-start;
}

.left-content {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.card,
.order-summary {
  background: rgba(255, 255, 255, 0.96);
  border-radius: 24px;
  padding: 28px;
  box-shadow: 0 18px 45px rgba(15, 23, 42, 0.08);
  border: 1px solid rgba(15, 23, 42, 0.06);
}

.card-title {
  display: flex;
  gap: 14px;
  align-items: flex-start;
  margin-bottom: 24px;
}

.step {
  width: 36px;
  height: 36px;
  background: linear-gradient(135deg, #ef4444, #dc2626);
  color: white;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-weight: 800;
}

.card-title h2,
.order-summary h2 {
  margin: 0;
  font-size: 22px;
  color: #111827;
}

.card-title p {
  margin-top: 4px;
  color: #6b7280;
  font-size: 14px;
}

.saved-addresses-picker {
  margin-bottom: 24px;
  padding: 16px;
  background: #f9fafb;
  border-radius: 16px;
  border: 1px solid #e5e7eb;
}

.picker-label {
  display: block;
  margin-bottom: 12px;
  font-weight: 800;
  color: #111827;
}

.address-chips {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.address-chip {
  width: 100%;
  text-align: left;
  border: 1px solid #e5e7eb;
  background: white;
  border-radius: 12px;
  padding: 12px 14px;
  cursor: pointer;
  color: #374151;
  transition: 0.2s;
}

.address-chip:hover {
  border-color: #dc2626;
  background: #fef2f2;
}

.chip-text {
  display: block;
  margin-top: 4px;
  font-size: 13px;
  color: #6b7280;
  line-height: 1.4;
}

.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 18px;
}

.form-group {
  margin-bottom: 18px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 700;
  color: #374151;
}

.form-group label span {
  color: #dc2626;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 14px 16px;
  border: 1px solid #e5e7eb;
  border-radius: 14px;
  font-size: 15px;
  outline: none;
  transition: 0.2s;
  background: #f9fafb;
}

.form-group input:focus,
.form-group textarea:focus {
  border-color: #ef4444;
  background: white;
  box-shadow: 0 0 0 4px rgba(239, 68, 68, 0.08);
}

.payment-list {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.payment-item {
  display: flex;
  gap: 16px;
  padding: 18px;
  border: 2px solid #e5e7eb;
  border-radius: 18px;
  cursor: pointer;
  transition: 0.2s;
  background: #fff;
}

.payment-item.active {
  border-color: #dc2626;
  background: #fef2f2;
}

.payment-item input {
  margin-top: 6px;
}

.payment-icon {
  font-size: 30px;
}

.payment-item h3 {
  margin: 0 0 6px;
  font-size: 16px;
  color: #111827;
}

.payment-item p {
  margin: 0;
  font-size: 14px;
  color: #6b7280;
}

.qr-box {
  margin-top: 22px;
  padding: 22px;
  border-radius: 20px;
  background: #f8fafc;
  border: 1px dashed #dc2626;
}

.qr-box h3 {
  margin: 0 0 16px;
  color: #111827;
}

.qr-content {
  display: flex;
  gap: 22px;
  align-items: center;
}

.qr-image {
  width: 220px;
  height: 220px;
  border-radius: 16px;
  background: white;
  border: 1px solid #e5e7eb;
  padding: 10px;
}

.bank-detail p {
  margin: 8px 0;
  color: #374151;
}

.bank-detail strong {
  color: #111827;
}

.bank-detail .money {
  color: #dc2626;
  font-size: 20px;
}

.confirm-transfer {
  margin-top: 18px;
  display: flex;
  gap: 10px;
  align-items: center;
  font-weight: 700;
  color: #111827;
}

.confirm-transfer input {
  width: 18px;
  height: 18px;
}

.order-summary {
  position: sticky;
  top: 24px;
}

.product-box {
  display: flex;
  gap: 16px;
  margin-top: 20px;
  padding-bottom: 18px;
  border-bottom: 1px solid #e5e7eb;
}

.product-box:last-of-type {
  border-bottom: none;
}

.product-box img {
  width: 118px;
  height: 118px;
  object-fit: cover;
  border-radius: 18px;
  border: 1px solid #e5e7eb;
  background: #f9fafb;
}

.product-info {
  flex: 1;
}

.product-info h3 {
  margin: 0 0 10px;
  font-size: 17px;
  line-height: 1.35;
  color: #111827;
}

.variant {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  margin-bottom: 12px;
  color: #6b7280;
  font-size: 14px;
}

.quantity-price {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.quantity-price strong {
  color: #dc2626;
}

.coupon-box {
  margin-top: 20px;
  padding: 16px;
  border-radius: 16px;
  background: #f9fafb;
  border: 1px solid #e5e7eb;
}

.coupon-box label {
  display: block;
  margin-bottom: 10px;
  font-weight: 800;
  color: #111827;
}

.coupon-input {
  display: flex;
  gap: 10px;
}

.coupon-input input {
  flex: 1;
  min-width: 0;
  padding: 12px 14px;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  outline: none;
  font-weight: 600;
  background: white;
}

.coupon-input input:focus {
  border-color: #dc2626;
  box-shadow: 0 0 0 4px rgba(220, 38, 38, 0.08);
}

.coupon-input button {
  border: none;
  border-radius: 12px;
  padding: 0 14px;
  background: #111827;
  color: white;
  font-weight: 800;
  cursor: pointer;
  white-space: nowrap;
}

.coupon-input button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-remove-coupon {
  background: #6b7280 !important;
}

.coupon-success {
  margin: 10px 0 0;
  color: #16a34a;
  font-size: 13px;
  font-weight: 700;
}

.divider {
  height: 1px;
  background: #e5e7eb;
  margin: 22px 0;
}

.price-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 14px 0;
  font-size: 15px;
  color: #374151;
}

.discount-text {
  color: #16a34a;
}

.total-row {
  font-size: 20px;
  font-weight: 800;
  color: #111827;
}

.total-row strong {
  color: #dc2626;
  font-size: 26px;
}

.btn-order {
  width: 100%;
  margin-top: 18px;
  padding: 16px;
  border: none;
  border-radius: 16px;
  background: linear-gradient(135deg, #ef4444, #dc2626);
  color: white;
  font-size: 17px;
  font-weight: 800;
  cursor: pointer;
  transition: 0.25s;
  box-shadow: 0 14px 25px rgba(220, 38, 38, 0.28);
}

.btn-order:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 18px 32px rgba(220, 38, 38, 0.38);
}

.btn-order:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.policy-text {
  margin-top: 14px;
  font-size: 13px;
  color: #6b7280;
  text-align: center;
  line-height: 1.5;
}

@media (max-width: 992px) {
  .checkout-wrapper {
    grid-template-columns: 1fr;
  }

  .order-summary {
    position: static;
  }
}

@media (max-width: 640px) {
  .checkout-page {
    padding: 24px 14px;
  }

  .checkout-header {
    align-items: flex-start;
    flex-direction: column;
  }

  .checkout-header h1 {
    font-size: 30px;
  }

  .form-grid {
    grid-template-columns: 1fr;
  }

  .card,
  .order-summary {
    padding: 20px;
    border-radius: 20px;
  }

  .qr-content {
    flex-direction: column;
    align-items: flex-start;
  }

  .qr-image {
    width: 100%;
    height: auto;
  }

  .product-box {
    flex-direction: column;
  }

  .product-box img {
    width: 100%;
    height: 220px;
  }
}
</style>
