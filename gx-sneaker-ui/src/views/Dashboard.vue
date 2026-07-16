<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { computed } from 'vue'
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  LineElement,
  PointElement,
  CategoryScale,
  LinearScale,
  ArcElement
} from 'chart.js'

import {
  Line,
  Pie
} from 'vue-chartjs'

ChartJS.register(
  Title,
  Tooltip,
  Legend,
  LineElement,
  PointElement,
  CategoryScale,
  LinearScale,
  ArcElement
)
const router = useRouter()

const dashboard = ref({
  tongSoDon: 0,
  tongDoanhThu: 0,
  soDonChoXacNhan: 0,
  soDonDaXacNhan: 0,
  soDonDangGiao: 0,
  soDonHoanThanh: 0,
  soDonDaHuy: 0
})

const revenueChartData = ref({
  labels: [],
  datasets: []
})

const chartOptions = {
  responsive: true,

  plugins: {
    legend: {
      position: 'top'
    }
  },

  scales: {
    y: {
      ticks: {
        callback: function (value) {
          return new Intl.NumberFormat('vi-VN').format(value) + ' đ'
        }
      }
    }
  }
}

const xemTatCaDonHang = () => {
  router.push('/hoa-don')
}

const loadDashboard = async () => {
  try {
    const response = await axios.get(
      'http://localhost:8080/api/hoa-don/thong-ke/dashboard'
    )

    dashboard.value = response.data
  } catch (error) {
    console.error('Lỗi tải dashboard:', error)
  }
}
const selectedYear = ref(new Date().getFullYear())

const loadRevenueChart = async () => {
  try {

    const response = await axios.get(
      `http://localhost:8080/api/hoa-don/thong-ke/bieu-do-doanh-thu-thang?year=${selectedYear.value}`
    )

    revenueChartData.value = {
      labels: response.data.map(item => `Tháng ${item.thang}`),

      datasets: [
        {
          label: `Doanh thu năm ${selectedYear.value}`,
          data: response.data.map(item => item.doanhThu),

          borderColor: '#0d6efd',
          backgroundColor: '#0d6efd',

          tension: 0.4,
          fill: false
        }
      ]
    }

  } catch (error) {
    console.error('Lỗi tải biểu đồ doanh thu:', error)
  }
}

const formatCurrency = (value) => {
  return new Intl.NumberFormat('vi-VN').format(value || 0) + ' VNĐ'
}

const statusChartData = ref({
  labels: [],
  datasets: []
})

const pieOptions = {
  responsive: true,
  maintainAspectRatio: false
}

const handleYearChange = () => {
  loadRevenueChart()
  loadStatusChart()
}

const loadStatusChart = async () => {
  try {

    const response = await axios.get(
      `http://localhost:8080/api/hoa-don/thong-ke/trang-thai-don-hang?year=${selectedYear.value}`
    )

    statusChartData.value = {
      labels: response.data.map(item => item.trangThai),

      datasets: [
        {
          data: response.data.map(item => item.soLuong),

          backgroundColor: [
            '#198754',
            '#0d6efd',
            '#ffc107',
            '#dc3545',
            '#6c757d'
          ]
        }
      ]
    }

  } catch (error) {
    console.error(error)
  }
}

const tongTrangThai = computed(() => {
  return statusChartData.value?.datasets?.[0]?.data?.reduce(
    (tong, item) => tong + item,
    0
  ) || 0
})

onMounted(() => {
  loadDashboard()
  loadRevenueChart()
  loadStatusChart()
})
</script>

<template>
  <div class="dashboard-container">
    <h1 class="dashboard-title">
      Dashboard Thống Kê GX Sneaker
    </h1>


    <div class="dashboard-grid">

      <div class="card" @click="xemTatCaDonHang">
        <h3>Tổng đơn hàng</h3>
        <p>{{ dashboard.tongSoDon }}</p>
      </div>

      <div class="card">
        <h3>Doanh thu lũy kế</h3>
        <p>{{ formatCurrency(dashboard.tongDoanhThu) }}</p>
      </div>

      <div class="card">
        <h3>Chờ xác nhận</h3>
        <p>{{ dashboard.soDonChoXacNhan }}</p>
      </div>

      <div class="card">
        <h3>Đã xác nhận</h3>
        <p>{{ dashboard.soDonDaXacNhan }}</p>
      </div>

      <div class="card">
        <h3>Đang giao</h3>
        <p>{{ dashboard.soDonDangGiao }}</p>
      </div>

      <div class="card">
        <h3>Hoàn thành</h3>
        <p>{{ dashboard.soDonHoanThanh }}</p>
      </div>

      <div class="card">
        <h3>Đã hủy</h3>
        <p>{{ dashboard.soDonDaHuy }}</p>
      </div>

    </div>

    <div class="year-filter mt-4 mb-4">
      <label class="form-label">Chọn năm</label>

      <select
        class="form-select year-select"
        v-model="selectedYear"
        @change="handleYearChange"
      >
        <option :value="2024">2024</option>
        <option :value="2025">2025</option>
        <option :value="2026">2026</option>
      </select>
    </div>

    <div class="chart-grid">

      <!-- Biểu đồ doanh thu -->
      <div class="chart-card">
        <h3 class="chart-title">
          📈 Biểu đồ doanh thu theo tháng năm {{ selectedYear }}
        </h3>

        <Line :data="revenueChartData" />
      </div>

      <!-- Biểu đồ trạng thái -->
      <div class="chart-card">
        <h3 class="chart-title">
          Tỷ lệ trạng thái đơn hàng năm {{ selectedYear }}
        </h3>

        <div class="pie-chart-container">

          <div v-if="tongTrangThai === 0" class="no-data">
            Không có dữ liệu đơn hàng trong năm {{ selectedYear }}
          </div>

          <Pie
            v-else
            :data="statusChartData"
            :options="pieOptions"
          />

        </div>
      </div>

    </div>

  </div>
</template>

<style scoped>
.dashboard-container {
  padding: 24px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.dashboard-title {
  margin-bottom: 24px;
  color: #333;
  font-size: 32px;
  font-weight: bold;
}

.dashboard-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
}

.card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  border: 1px solid #e5e7eb;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  cursor: pointer;
}

.card h3 {
  margin-bottom: 12px;
  color: #666;
  font-size: 16px;
}

.card p {
  font-size: 28px;
  font-weight: bold;
  color: #111;
}

.chart-card {
  margin-top: 24px;
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}

.year-filter {
  margin-top: 24px;
  margin-bottom: 24px;
}

.year-filter .form-label {
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
}

.year-select {
  width: 180px;
  border-radius: 10px;
  padding: 8px 12px;
  border: 1px solid #dcdfe6;
  transition: all 0.3s ease;
}

.chart-subtitle {
  text-align: center;
  color: #6b7280;
  font-size: 14px;
  margin-bottom: 20px;
}

.year-select:hover {
  border-color: #0d6efd;
}

.year-select:focus {
  border-color: #0d6efd;
  box-shadow: 0 0 0 0.2rem rgba(13, 110, 253, 0.2);
}

.chart-card {
  background: white;
  margin-top: 24px;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0,0,0,.08);
}

.chart-card h3 {
  margin-bottom: 20px;
}

.chart-grid {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 20px;
}

.chart-card {
  background: white;
  padding: 20px;
  border-radius: 12px;
}

.pie-chart-container {
  width: 280px;
  height: 280px;
  margin: 0 auto;
}


.no-data {
  height: 300px;
  display: flex;
  justify-content: center;
  align-items: center;

  color: #999;
  font-size: 16px;
  font-weight: 500;
}

</style>
