<template>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

  <div class="container-fluid custom-layout bg-light min-vh-100">
    <div class="card hoa-don-card shadow border-0 w-100" style="max-width: 1500px;">

      <div class="card-header bg-white border-0 pt-5 pb-3 px-4">
        <div class="text-center">
          <h1 class="main-invoice-title">
            DANH SÁCH HÓA ĐƠN ({{ hoaDons.length }})
          </h1>

          <div class="search-box-container mx-auto my-4">
            <div class="search-toolbar">

              <div class="search-item search-input-box">
                <div class="search-input-wrapper">
                  <i class="bi bi-search search-icon"></i>

                  <input
                    v-model="searchMaHD"
                    type="text"
                    class="form-control search-input-modern"
                    placeholder="Nhập mã hóa đơn..."
                    @keyup.enter="handleSearch"
                  />
                </div>
              </div>

              <div class="search-item status-box">
                <select
                  v-model="searchStatus"
                  class="form-select search-select"
                >
                  <option value="">-- Tất cả trạng thái --</option>
                  <option value="CHO_XAC_NHAN">CHỜ XÁC NHẬN</option>
                  <option value="DA_XAC_NHAN">ĐÃ XÁC NHẬN</option>
                  <option value="DANG_GIAO">ĐANG GIAO</option>
                  <option value="HOAN_THANH">HOÀN THÀNH</option>
                  <option value="DA_HUY">ĐÃ HỦY</option>
                </select>
              </div>

              <div class="search-item button-box">
                <button
                  @click="handleSearch"
                  class="btn btn-filter"
                >
                  <i class="bi bi-funnel-fill me-2"></i>
                  Lọc
                </button>

                <button
                  @click="handleReset"
                  class="btn btn-outline-secondary btn-reset"
                >
                  <i class="bi bi-arrow-counterclockwise"></i>
                </button>
              </div>

            </div>
          </div>

          <div class="invoice-action">
            <button class="btn btn-export-lg d-inline-flex align-items-center gap-2">
              <i class="bi bi-download"></i>
              In hóa đơn
            </button>
          </div>
        </div>
      </div>

      <div class="card-body p-0">
        <div class="table-responsive">
          <table class="table mb-0 align-middle">
            <thead>
            <tr>
              <th style="width: 80px;" class="text-center">#</th>
              <th>Mã hóa đơn</th>
              <th>Người nhận</th>
              <th>SĐT</th>
              <th>Tổng thanh toán</th>
              <th>Trạng thái</th>
              <th>Ngày đặt</th>
              <th class="text-center" style="width: 320px;">Thao tác</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(hd, index) in hoaDons" :key="hd.id">
              <td class="text-center text-muted fw-semibold">{{ index + 1 }}</td>
              <td><span class="ma-hd">{{ hd.maHoaDon }}</span></td>
              <td class="text-secondary fw-semibold">{{ hd.tenNguoiNhan }}</td>
              <td class="text-secondary">{{ hd.soDienThoaiNguoiNhan }}</td>
              <td><span class="tien">{{ formatMoney(hd.tongTienThanhToan) }}</span></td>
              <td>
                  <span class="status-pill" :class="getStatusClass(hd.trangThai)">
                    <i :class="getStatusIcon(hd.trangThai)" class="me-1"></i>
                    {{ getStatusText(hd.trangThai) }}
                  </span>
              </td>
              <td>
                <div class="date-time">
                  <div class="fw-semibold text-secondary">{{ formatDate(hd.ngayDatHang) }}</div>
                  <div class="text-muted small mt-1 text-black-50">{{ formatTime(hd.ngayDatHang) }}</div>
                </div>
              </td>
              <td>
                <div class="action-buttons">

                  <button
                    @click="openDetailModal(hd)"
                    class="btn-action btn-view"
                    title="Chi tiết"
                  >
                    <i class="bi bi-eye"></i>
                  </button>

                  <button
                    v-if="hd.trangThai === 'CHO_XAC_NHAN'"
                    class="btn btn-success btn-sm"
                    @click="updateStatus(hd.id, 'DA_XAC_NHAN')"
                  >
                    Xác nhận
                  </button>

                  <button
                    v-if="hd.trangThai === 'CHO_XAC_NHAN'"
                    class="btn btn-danger btn-sm"
                    @click="updateStatus(hd.id, 'DA_HUY')"
                  >
                    Hủy
                  </button>

                  <button
                    v-if="hd.trangThai === 'DA_XAC_NHAN'"
                    class="btn btn-primary btn-sm"
                    @click="updateStatus(hd.id, 'DANG_GIAO')"
                  >
                    Giao hàng
                  </button>

                  <button
                    v-if="hd.trangThai === 'DANG_GIAO'"
                    class="btn btn-warning btn-sm text-white"
                    @click="updateStatus(hd.id, 'HOAN_THANH')"
                  >
                    Hoàn thành
                  </button>

                </div>
              </td>
            </tr>
            <tr v-if="hoaDons.length === 0">
              <td colspan="8" class="text-center py-5 text-muted fw-medium">
                Không tìm thấy dữ liệu hóa đơn nào phù hợp.
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <div v-if="showModal" class="custom-modal-overlay" @click.self="closeModal">
      <div class="modal-content-card animate-slide-up">

        <div class="modal-header-pro">
          <div>
            <h4 class="m-0 fw-bold text-dark">CHI TIẾT ĐƠN HÀNG</h4>
            <small class="text-muted">Mã số: <span class="text-danger fw-bold">{{ selectedHoaDon?.maHoaDon }}</span></small>
          </div>
          <button @click="closeModal" class="btn-close-pro"><i class="bi bi-x-lg"></i></button>
        </div>

        <div class="modal-body-pro">
          <div class="info-grid mb-4">
            <div class="info-item">
              <label>Người nhận</label>
              <span>{{ selectedHoaDon?.tenNguoiNhan }}</span>
            </div>
            <div class="info-item">
              <label>Số điện thoại</label>
              <span>{{ selectedHoaDon?.soDienThoaiNguoiNhan }}</span>
            </div>
            <div class="info-item">
              <label>Ngày đặt</label>
              <span>{{ formatDate(selectedHoaDon?.ngayDatHang) }} {{ formatTime(selectedHoaDon?.ngayDatHang) }}</span>
            </div>
            <div class="info-item">
              <label>Trạng thái</label>
              <div>
                <span class="status-pill m-0" :class="getStatusClass(selectedHoaDon?.trangThai)">
                  {{ getStatusText(selectedHoaDon?.trangThai) }}
                </span>
              </div>
            </div>
          </div>

          <div class="product-table-wrapper">

            <table class="table align-middle mb-0">
              <thead class="table-light">
              <tr>
                <th style="width:45%">Sản phẩm</th>
                <th class="text-center">Size/màu</th>
                <th class="text-center">SL</th>
                <th class="text-end">Đơn giá</th>
                <th class="text-end">Thành tiền</th>
              </tr>
              </thead>

              <tbody>

              <tr
                v-for="item in chiTietList"
                :key="item.chiTietSanPhamId"
              >
                <!-- Sản phẩm -->
                <td>

                  <div class="d-flex align-items-center">

                    <img

                      :src="`/images/${item.image}`"
                      width="80"
                      class="product-image"
                      @error="$event.target.src='/default-shoe.png'"
                    >

                    <div class="ms-3">

                      <div class="fw-bold fs-6">
                        {{ item.productName }}
                      </div>

                      <div class="text-muted small">
                        SKU #{{ item.chiTietSanPhamId }}
                      </div>

                    </div>

                  </div>

                </td>

                <!-- Size màu -->
                <td class="text-center">

                <span class="badge-size">
                  {{ item.size }}
                </span>

                  <span class="badge-color ms-2">
                  {{ item.color }}
                  </span>

                </td>

                <!-- Số lượng -->

                <td>{{ item.quantity }}</td>
                <!-- Đơn giá -->
                <td class="text-end text-secondary fw-semibold">
                  {{ formatMoney(item.price) }}
                </td>

                <!-- Thành tiền -->
                <td class="text-end">

        <span class="fw-bold text-danger fs-5">
          {{ formatMoney(item.total) }}
        </span>

                </td>

              </tr>

              <tr v-if="chiTietList.length===0">
                <td colspan="5" class="text-center py-5 text-muted">
                  Không có sản phẩm
                </td>
              </tr>

              </tbody>

              <!-- Footer -->
              <tfoot
                v-if="chiTietList.length"
                class="table-light"
              >
              <tr>

                <td colspan="4" class="text-end fw-bold fs-5">
                  Tổng tiền hàng
                </td>

                <td class="text-end">

        <span class="text-danger fw-bold fs-4">
          {{ formatMoney(selectedHoaDon?.tongTien) }}
        </span>

                </td>

              </tr>
              </tfoot>

            </table>

          </div>

          <div class="history-section mt-4">
            <h5 class="fw-bold mb-3">
              Lịch sử trạng thái
            </h5>

            <table class="table table-bordered table-striped">
              <thead>
              <tr>
                <th>Thời gian</th>
                <th>Từ</th>
                <th>Sang</th>
                <th>Người thực hiện</th>
                <th>Ghi chú</th>
              </tr>
              </thead>

              <tbody>
              <tr
                v-for="item in lichSuList"
                :key="item.id"
              >
                <td>{{ formatDateTime(item.thoiGian) }}</td>
                <td>{{ item.trangThaiCu || '-' }}</td>
                <td>{{ item.trangThaiMoi }}</td>
                <td>{{ item.nguoiThucHien }}</td>
                <td>{{ item.ghiChu || '-' }}</td>
              </tr>

              <tr v-if="lichSuList.length === 0">
                <td colspan="5" class="text-center">
                  Chưa có lịch sử
                </td>
              </tr>
              </tbody>
            </table>
          </div>

        </div>

        <div class="modal-footer-pro">
          <div class="total-section">
            <span class="text-muted me-2">TỔNG CỘNG:</span>
            <span class="total-price">
      {{ formatMoney(selectedHoaDon?.tongTien) }}
    </span>
          </div>

          <button
            @click="closeModal"
            class="btn btn-dark px-5 fw-bold rounded-3">
            ĐÓNG
          </button>
        </div>

      </div>
    </div>
  </div>
</template>

<script setup>
import axios from 'axios'
import { ref, onMounted } from 'vue'

const hoaDons = ref([])
const searchMaHD = ref('')
const searchStatus = ref('')

const showModal = ref(false)
const selectedHoaDon = ref(null)
const chiTietList = ref([])

const loadHoaDon = async (maHD = '', status = '') => {
  try {
    const response = await axios.get('http://localhost:8080/api/hoa-don/search', {
      params: { maHoaDon: maHD, trangThai: status }
    })
    hoaDons.value = response.data
  } catch (error) {
    console.error('Lỗi API:', error)
  }
}

const updateStatus = async (id, trangThaiMoi) => {
  try {
    await axios.put(
      `http://localhost:8080/api/hoa-don/${id}/status`,
      {
        trangThaiMoi,
        nguoiThucHien: "Admin",
        ghiChu: ""
      }
    )

    await loadHoaDon(
      searchMaHD.value,
      searchStatus.value
    )

    alert("Cập nhật trạng thái thành công!")
  } catch (error) {
    console.error(error)
    alert("Cập nhật trạng thái thất bại!")
  }
}


const openDetailModal = async (hd) => {
  try {
    // Lấy chi tiết hóa đơn
    const detailResponse = await axios.get(
      `http://localhost:8080/api/hoa-don/${hd.id}`
    )

    // API trả về:
    // {
    //   id,
    //   maHoaDon,
    //   ...
    //   items:[...]
    // }

    selectedHoaDon.value = detailResponse.data
    chiTietList.value = detailResponse.data.items || []

    // Lấy lịch sử
    const historyResponse = await axios.get(
      `http://localhost:8080/api/hoa-don/${hd.id}/history`
    )

    lichSuList.value = historyResponse.data

    // Mở modal
    showModal.value = true

  } catch (error) {
    console.error("Lỗi khi tải chi tiết hóa đơn:", error)
  }
}

const closeModal = () => {
  showModal.value = false
  selectedHoaDon.value = null
  chiTietList.value = []
  lichSuList.value = []
}


const handleSearch = () => { loadHoaDon(searchMaHD.value, searchStatus.value) }
const handleReset = () => { searchMaHD.value = ''; searchStatus.value = ''; loadHoaDon() }

const formatMoney = (v) => v ? Number(v).toLocaleString('vi-VN') + ' đ' : '0 đ'
const formatDate = (s) => s ? `${String(new Date(s).getDate()).padStart(2,'0')}/${String(new Date(s).getMonth()+1).padStart(2,'0')}/${new Date(s).getFullYear()}` : ''
const formatTime = (s) => s ? `${String(new Date(s).getHours()).padStart(2,'0')}:${String(new Date(s).getMinutes()).padStart(2,'0')}` : ''

const getStatusText = (s) => {
  const m = { 'CHO_XAC_NHAN': 'CHỜ XÁC NHẬN', 'DA_XAC_NHAN': 'ĐÃ XÁC NHẬN', 'DANG_GIAO': 'ĐANG GIAO', 'HOAN_THANH': 'HOÀN THÀNH', 'DA_HUY': 'ĐÃ HỦY' }
  return m[s] || s
}
const getStatusClass = (s) => {
  switch (s) {
    case 'CHO_XAC_NHAN': return 'bg-warning-custom'
    case 'DA_XAC_NHAN': return 'bg-primary-custom'
    case 'DANG_GIAO': return 'bg-info-custom'
    case 'HOAN_THANH': return 'bg-success-custom'
    case 'DA_HUY': return 'bg-danger-custom'
    default: return 'bg-secondary-custom'
  }
}
const getStatusIcon = (s) => {
  switch (s) {
    case 'CHO_XAC_NHAN': return 'bi bi-clock-history'
    case 'DA_XAC_NHAN': return 'bi bi-check2-all'
    case 'DANG_GIAO': return 'bi bi-truck'
    case 'HOAN_THANH': return 'bi bi-check-circle'
    case 'DA_HUY': return 'bi bi-x-circle'
    default: return 'bi bi-question-circle'
  }
}

const lichSuList = ref([])

const formatDateTime = (date) => {
  if (!date) return '-'

  return new Date(date).toLocaleString('vi-VN')
}

onMounted(() => { loadHoaDon() })
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');

* { font-family: 'Inter', sans-serif; }

.custom-layout {
  padding: 60px 70px;
  display: flex;
  justify-content: center;
  align-items: flex-start;

}

.hoa-don-card {
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 10px 35px rgba(0, 0, 0, 0.05) !important;
  background-color: #ffffff;
}

.main-invoice-title {
  text-align: center;
  font-size: 42px;
  font-weight: 800;

  color: #111827;
  letter-spacing: 1px;

  margin-top: 10px;
  margin-bottom: 45px;
}

.search-box-container {
  background: #f8f9fa;
  padding: 24px;
  border-radius: 14px;
  border: 1px solid #eef2f5;
  max-width: 1100px;
  width: 100%;
}

/* ==================== ĐỊNH DẠNG MODAL CAO CẤP MỚI ==================== */
.custom-modal-overlay {
  position: fixed;
  top: 0; left: 0;
  width: 100vw; height: 100vh;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999 !important;
}

.modal-content-card {
  background: #ffffff;
  width: 900px;
  max-width: 95%;
  max-height: 88vh;
  border-radius: 20px;
  display: flex;
  flex-direction: column;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
  overflow: hidden;
}

.modal-header-pro {
  padding: 24px 32px;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.btn-close-pro {
  border: none;
  background: none;
  font-size: 22px;
  color: #bfbfbf;
  cursor: pointer;
}
.btn-close-pro:hover { color: #000000; }

.modal-body-pro {
  padding: 32px;
  overflow-y: auto;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  background: #f8f9fa;
  padding: 20px;
  border-radius: 12px;
}

.info-item label {
  display: block;
  font-size: 12px;
  color: #8c8c8c;
  margin-bottom: 4px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.info-item span {
  font-weight: 600;
  color: #262626;
  font-size: 14.5px;
}

.product-image{
  width:72px;
  height:72px;
  object-fit:cover;
  border-radius:14px;
  border:1px solid #ececec;
  background:#fff;
  padding:4px;
  transition:.25s;
}

.product-image:hover{
  transform:scale(1.05);
}

.product-table-wrapper{
  margin-top: 24px;
  border:1px solid #ececec;
  border-radius:14px;
  overflow:hidden;
  background:#fff;

}

.product-table-wrapper table{
  margin:0;
}

.product-table-wrapper th{
  font-size:14px;
  font-weight:700;
  color:#6b7280;
  text-transform:uppercase;
  letter-spacing:.5px;
  padding:16px;
}

.product-table-wrapper td{
  vertical-align:middle;
  padding:20px 16px;
}

.product-table-wrapper tbody tr{
  transition:.25s;
}

.product-table-wrapper tbody tr:hover{
  background:#f8fafc;
}

tfoot td{
  padding:18px !important;
}

.modal-footer-pro {
  padding: 24px 32px;
  border-top: 1px solid #f0f0f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fafafa;
}

.total-price{
  font-size:34px;
  font-weight:900;
  color:#ef4444;
  letter-spacing:.5px;
}

.animate-slide-up {
  animation: slideUp 0.25s cubic-bezier(0.16, 1, 0.3, 1);
}
@keyframes slideUp {
  from { transform: translateY(30px); opacity: 0; }
  to { transform: translateY(0); opacity: 1; }
}


.search-toolbar {
  display: flex;
  gap: 15px;
  align-items: center;
  width: 100%;
}

.search-input-box {
  flex: 2;
}

.status-box {
  flex: 1;
}

.button-box {
  display: flex;
  gap: 10px;
}

.btn-filter {
  min-width: 120px;
}

.btn-reset {
  width: 50px;
}

.search-input,
.search-select {
  width: 100%;
}
.action-buttons {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;

  gap: 10px; /* chỉnh 14px, 18px, 20px tùy ý */
}


.history-section{
  margin-top:32px;
  padding:20px;
  border:1px solid #ececec;
  border-radius:14px;
  background:#fff;
}

.history-item {
  display: flex;
  gap: 20px;
  padding: 14px 0;
  border-bottom: 1px dashed #e5e7eb;
}

.history-time {
  width: 170px;
  font-size: 13px;
  color: #6b7280;
}

.history-content {
  flex: 1;
}

/* ==================== ĐỊNH DẠNG TABLE & BUTTON DANH SÁCH ==================== */
.search-input .form-control,
.search-input .input-group-text,
.search-select,
.btn-filter,
.btn-reset {
  height: 50px !important;
}

.search-input .form-control,
.search-select {
  height: 50px;
  border: none;
  border-radius: 14px;
  background: #f8fafc;
  box-shadow: inset 0 0 0 1px #dbe2ea;
}

.search-input .input-group-text {
  background: #f8fafc;
  border: none;
  box-shadow: inset 1px 0 0 #dbe2ea,
  inset 0 1px 0 #dbe2ea,
  inset 0 -1px 0 #dbe2ea;
}

.search-input .form-control:focus,
.search-select:focus {
  background: white;
  box-shadow:
    inset 0 0 0 2px #3b82f6,
    0 0 0 4px rgba(59,130,246,.15);
}


.search-input-wrapper {
  position: relative;
  width: 100%;
}

.search-icon {
  position: absolute;
  left: 14px;
  top: 50%;
  transform: translateY(-50%);
  color: #9ca3af;
  font-size: 15px;
  z-index: 2;
}

.search-input-modern {
  height: 48px;
  padding-left: 42px !important;
  border-radius: 12px;
  border: 1px solid #dbe2ea;
}

.search-input-modern:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 4px rgba(59,130,246,.15);
}

.invoice-action {
  margin-top: 20px;
  margin-bottom: 20px;
  padding-left: 15px;
}

.badge-size{
  background:#eff6ff;
  color:#2563eb;
  border:1px solid #bfdbfe;
  padding:6px 10px;
  border-radius:8px;
  font-weight:600;
}

.badge-color{
  background:#f3f4f6;
  color:#374151;
  border:1px solid #e5e7eb;
  padding:6px 10px;
  border-radius:8px;
  font-weight:600;
}

.btn-export-lg {
  padding: 10px 22px;
  border-radius: 12px;
}

.btn-sm {
  border-radius: 8px;
  font-weight: 600;
  padding: 6px 12px;
}

.btn-success,
.btn-danger,
.btn-primary,
.btn-warning {
  transition: all 0.25s ease;
}

.btn-success:hover,
.btn-danger:hover,
.btn-primary:hover,
.btn-warning:hover {
  transform: translateY(-2px);
}

.search-input .input-group-text { border-radius: 8px 0 0 8px !important; border: 1px solid #ced4da; background-color: #ffffff; }
.search-input .form-control { border-radius: 0 8px 8px 0 !important; border: 1px solid #ced4da; }
.search-select { border-radius: 8px !important; border: 1px solid #ced4da; }

.btn-filter {
  padding: 0 24px; font-size: 14px; font-weight: 600; border-radius: 8px;
  background-color: #1a1a1a; color: #ffffff; border: none; transition: all 0.2s ease;
}
.btn-filter:hover { background-color: #000000; }

.btn-reset { width: 46px; border-radius: 8px; background-color: #ffffff; border: 1px solid #ced4da; color: #6c757d; }
.btn-reset:hover { background-color: #f8f9fa; }

.text-nowrap { white-space: nowrap !important; }

.btn-export-lg {
  padding: 10px 22px; font-size: 14px; font-weight: 600; border-radius: 6px;
  color: #434343; background-color: #ffffff; border: 1px solid #d9d9d9;
}
.btn-export-lg:hover { background-color: #f5f5f5; }

.table thead th { background: #f8f9fa; color: #262626; font-weight: 600; font-size: 14px; padding: 18px 24px; border-bottom: 2px solid #edf2f7; }
.table tbody td { padding: 20px 24px; border-bottom: 1px solid #f0f0f0; font-size: 14.5px; }

.ma-hd, .tien { color: #e04646; font-weight: 600; }

.status-pill { padding: 6px 14px; border-radius: 6px; font-size: 12px; font-weight: 600; display: inline-flex; align-items: center; }

.bg-warning-custom { background: #fff7e6 !important; color: #fa8c16 !important; border: 1px solid #ffe7ba; }
.bg-primary-custom { background: #e6f4ff !important; color: #1677ff !important; border: 1px solid #bae0ff; }
.bg-info-custom { background: #f0f5ff !important; color: #2f54eb !important; border: 1px solid #adc6ff; }
.bg-success-custom { background: #f6ffed !important; color: #52c41a !important; border: 1px solid #b7eb8f; }
.bg-danger-custom { background: #fff1f0 !important; color: #ff4d4f !important; border: 1px solid #ffa39e; }

.btn-action {
  width: 38px; height: 38px; border-radius: 8px; display: inline-flex; align-items: center;
  justify-content: center; border: 1px solid; background: #ffffff; font-size: 16px; transition: all 0.2s ease;
}
.btn-view { border-color: #1677ff; color: #1677ff; }
.btn-view:hover { background: #e6f4ff; transform: translateY(-2px); }
.btn-edit { border-color: #52c41a; color: #52c41a; }
.btn-edit:hover { background: #f6ffed; transform: translateY(-2px); }
.btn-delete { border-color: #ff4d4f; color: #ff4d4f; }
.btn-delete:hover { background: #fff1f0; transform: translateY(-2px); }

.date-time { line-height: 1.4; }
</style>
