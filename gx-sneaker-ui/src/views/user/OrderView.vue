<script setup>
import { ref, onMounted } from "vue";
import { getHoaDonByKhachHang } from "@/services/HoaDonService";

const orders = ref([]);

const formatMoney = (value) =>
  Number(value).toLocaleString("vi-VN") + " đ";

const formatDate = (value) => {
  return new Date(value).toLocaleString("vi-VN");
};

onMounted(async () => {

  try {

    // Sau này thay bằng user đang đăng nhập
    const res = await getHoaDonByKhachHang(1);

    orders.value = res.data;

  } catch (e) {

    console.log(e);

  }

});
</script>
<template>
  <div
    v-for="order in orders"
    :key="order.id"
    class="order-card"
  >

    <div class="top">

      <div>

        <b>Mã đơn:</b>

        {{ order.maHoaDon }}

      </div>

      <div>

        {{ formatDate(order.ngayDatHang) }}

      </div>

    </div>

    <hr>

    <div
      v-for="item in order.items"
      :key="item.chiTietSanPhamId"
      class="product"
    >

      <img
        :src="'/images/' + item.image"
      >

      <div class="info">

        <h3>{{ item.productName }}</h3>

        <p>Màu: {{ item.color }}</p>

        <p>Size: {{ item.size }}</p>

        <p>Số lượng: {{ item.quantity }}</p>

      </div>

    </div>

    <div class="bottom">

      <div>

        <b>Tổng tiền:</b>

        <span class="price">

                {{ formatMoney(order.tongTien) }}

            </span>

      </div>

      <div>

            <span class="status">

                {{ order.trangThai }}

            </span>

        <router-link
          :to="`/orders/${order.id}`"
          class="btn-detail"
        >
          Xem chi tiết
        </router-link>

      </div>

    </div>

  </div>
</template>

<style scoped>

.order-page{
  max-width:1100px;
  margin:auto;
  padding:40px 20px;
}

.order-card{
  background:#fff;
  padding:20px;
  margin-bottom:20px;
  border-radius:12px;
  box-shadow:0 3px 10px rgba(0,0,0,.08);
}

.top{
  display:flex;
  justify-content:space-between;
}

.product{
  display:flex;
  margin-top:15px;
}

.product img{
  width:100px;
  height:100px;
  object-fit:cover;
  margin-right:20px;
}

.bottom{
  display:flex;
  justify-content:space-between;
  align-items:center;
  margin-top:20px;
}

.price{
  color:red;
  font-size:22px;
  font-weight:bold;
}

.status{
  background:#ffe58a;
  padding:6px 12px;
  border-radius:20px;
  margin-right:10px;
}

button{
  padding:8px 16px;
  background:#111827;
  color:white;
  border:none;
  border-radius:8px;
  cursor:pointer;
}

.empty{
  text-align:center;
  padding:80px;
  font-size:20px;
}

</style>
