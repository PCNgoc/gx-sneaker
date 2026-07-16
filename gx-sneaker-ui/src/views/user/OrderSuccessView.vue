<script setup>
import { computed } from "vue"
import { useRoute, useRouter } from "vue-router"

const route = useRoute()
const router = useRouter()

const maHoaDon = computed(() => {
  return route.query.maHoaDon || "Đơn hàng của bạn"
})

const paymentMethod = computed(() => {
  return route.query.payment || "COD"
})

const isQR = computed(() => {
  return paymentMethod.value === "QR"
})
</script>

<template>
  <div class="success-page">
    <div class="success-card">
      <div class="success-icon">✓</div>

      <h1>Đã gửi đơn hàng thành công</h1>

      <p class="subtitle">
        Cảm ơn bạn đã mua hàng tại GX Sneaker.
      </p>

      <div class="order-code">
        <span>Mã hóa đơn</span>
        <strong>{{ maHoaDon }}</strong>
      </div>

      <div class="status-box">
        <div class="status-row">
          <span>Trạng thái đơn hàng</span>
          <strong>Đang chờ admin xác nhận</strong>
        </div>

        <div class="status-row">
          <span>Trạng thái thanh toán</span>

          <strong v-if="isQR">
            Chờ xác nhận thanh toán
          </strong>

          <strong v-else>
            Thanh toán khi nhận hàng
          </strong>
        </div>
      </div>

      <div v-if="isQR" class="notice">
        <h3>Lưu ý thanh toán QR</h3>

        <p>
          Đơn hàng của bạn đã được ghi nhận. Shop sẽ kiểm tra giao dịch
          chuyển khoản MB Bank. Sau khi xác nhận đã nhận tiền, admin sẽ cập nhật
          trạng thái đơn hàng.
        </p>
      </div>

      <div v-else class="notice">
        <h3>Lưu ý đơn COD</h3>

        <p>
          Đơn hàng của bạn đang chờ shop xác nhận. Bạn sẽ thanh toán trực tiếp
          cho shipper khi nhận hàng.
        </p>
      </div>

      <div class="actions">
        <button class="btn-primary" @click="router.push('/orders')">
          Xem đơn hàng của tôi
        </button>

        <button class="btn-secondary" @click="router.push('/products')">
          Tiếp tục mua sắm
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.success-page {
  min-height: 100vh;
  background:
    radial-gradient(circle at top left, #fee2e2, transparent 32%),
    linear-gradient(135deg, #f8fafc, #eef2ff);
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 32px 16px;
}

.success-card {
  width: 100%;
  max-width: 580px;
  background: white;
  border-radius: 28px;
  padding: 42px;
  text-align: center;
  box-shadow: 0 24px 60px rgba(15, 23, 42, 0.12);
  border: 1px solid #e5e7eb;
}

.success-icon {
  width: 82px;
  height: 82px;
  border-radius: 50%;
  background: linear-gradient(135deg, #22c55e, #16a34a);
  color: white;
  font-size: 48px;
  font-weight: 900;
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 0 auto 22px;
}

h1 {
  margin: 0;
  font-size: 30px;
  color: #111827;
}

.subtitle {
  color: #6b7280;
  margin: 10px 0 26px;
}

.order-code {
  background: #f9fafb;
  border: 1px dashed #dc2626;
  border-radius: 18px;
  padding: 18px;
  margin-bottom: 22px;
}

.order-code span {
  display: block;
  color: #6b7280;
  font-size: 14px;
  margin-bottom: 6px;
}

.order-code strong {
  font-size: 24px;
  color: #dc2626;
}

.status-box {
  background: #f8fafc;
  border-radius: 18px;
  padding: 18px;
  margin-bottom: 20px;
  text-align: left;
}

.status-row {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  padding: 10px 0;
  color: #374151;
}

.status-row strong {
  color: #f59e0b;
  text-align: right;
}

.notice {
  background: #fff7ed;
  border: 1px solid #fed7aa;
  border-radius: 18px;
  padding: 18px;
  text-align: left;
  margin-bottom: 26px;
}

.notice h3 {
  margin: 0 0 8px;
  color: #9a3412;
}

.notice p {
  margin: 0;
  color: #7c2d12;
  line-height: 1.6;
}

.actions {
  display: flex;
  gap: 14px;
}

button {
  flex: 1;
  border: none;
  border-radius: 16px;
  padding: 14px 16px;
  font-weight: 800;
  cursor: pointer;
}

.btn-primary {
  background: linear-gradient(135deg, #ef4444, #dc2626);
  color: white;
}

.btn-secondary {
  background: #f3f4f6;
  color: #111827;
}

@media (max-width: 560px) {
  .success-card {
    padding: 28px 20px;
  }

  .actions {
    flex-direction: column;
  }

  .status-row {
    flex-direction: column;
  }
}
</style>
