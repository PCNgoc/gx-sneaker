<script setup>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import { getHoaDonById } from "@/services/HoaDonService";

const route = useRoute();

const order = ref(null);

const loading = ref(true);

const formatMoney = (money) => {
  return Number(money).toLocaleString("vi-VN");
};

const formatDate = (date) => {
  if (!date) return "";

  return new Date(date).toLocaleString("vi-VN");
};

onMounted(async () => {
  try {
    const id = route.params.id;

    const res = await getHoaDonById(id);

    order.value = res.data;
  } catch (e) {
    console.error(e);
    order.value = null;
  } finally {
    loading.value = false;
  }
});
</script>

<template>
  <div class="order-detail">

    <h2>Chi tiết đơn hàng</h2>

    <div v-if="loading">
      Đang tải...
    </div>

    <div v-else-if="order">

      <div class="info">

        <p>
          <b>Mã đơn:</b>
          {{ order.maHoaDon }}
        </p>

        <p>
          <b>Ngày đặt:</b>
          {{ formatDate(order.ngayDatHang) }}
        </p>

        <p>
          <b>Trạng thái:</b>
          {{ order.trangThai }}
        </p>

        <p>
          <b>Người nhận:</b>
          {{ order.tenNguoiNhan }}
        </p>

        <p>
          <b>Số điện thoại:</b>
          {{ order.soDienThoai }}
        </p>

        <p>
          <b>Địa chỉ:</b>
          {{ order.diaChi }}
        </p>

      </div>

      <table class="table">

        <thead>

        <tr>

          <th>Ảnh</th>

          <th>Sản phẩm</th>

          <th>Màu</th>

          <th>Size</th>

          <th>Số lượng</th>

          <th>Đơn giá</th>

          <th>Thành tiền</th>

        </tr>

        </thead>

        <tbody>

        <tr
          v-for="item in order.items"
          :key="item.chiTietSanPhamId"
        >

          <td>

            <img
              :src="`/images/${item.image}`"
              width="80"
            />

          </td>

          <td>{{ item.productName }}</td>

          <td>{{ item.color }}</td>

          <td>{{ item.size }}</td>

          <td>{{ item.quantity }}</td>

          <td>{{ formatMoney(item.price) }} đ</td>

          <td>{{ formatMoney(item.total) }} đ</td>

        </tr>

        </tbody>

      </table>

      <div class="total">

        <h3>

          Tổng thanh toán:
          {{ formatMoney(order.tongTien) }} đ

        </h3>

      </div>

    </div>

    <div v-else>

      Không tìm thấy đơn hàng.

    </div>

  </div>
</template>

<style scoped>

.order-detail{
  max-width:1100px;
  margin:auto;
  padding:30px;
}

.info{
  background:#f8f8f8;
  padding:20px;
  border-radius:10px;
  margin-bottom:25px;
}

.info p{
  margin:8px 0;
}

.table{
  width:100%;
  border-collapse:collapse;
}

.table th,
.table td{
  border:1px solid #ddd;
  padding:10px;
  text-align:center;
}

.table th{
  background:#f5f5f5;
}

.total{
  margin-top:20px;
  text-align:right;
  color:red;
}

img{
  border-radius:8px;
}

</style>
