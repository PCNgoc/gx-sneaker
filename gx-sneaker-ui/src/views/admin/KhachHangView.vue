<script setup>
import { ref, onMounted, computed } from "vue"
import { getAll, create, update, remove } from "@/services/khachHangService"
import { getByKhachHangId as getAddresses, create as createAddress, update as updateAddress, remove as removeAddress } from "@/services/diaChiService"

const list = ref([])
const detail = ref(null)
const loading = ref(false)
const page = ref(1)
const pageSize = 10

const form = ref({
  id: null,
  maKhachHang: "",
  hoTen: "",
  email: "",
  matKhau: "",
  soDienThoai: "",
  gioiTinh: true,
  ngaySinh: "",
  trangThai: true,
  daXacThuc: false
})

// Address states
const showAddressModal = ref(false)
const selectedCustomer = ref(null)
const addressList = ref([])
const addressForm = ref({
  id: null,
  hoTenNguoiNhan: "",
  soDienThoai: "",
  tinhThanh: "",
  quanHuyen: "",
  phuongXa: "",
  diaChiChiTiet: ""
})

// FORMAT DATE
const formatDate = (date) => {
  if (!date) return ""
  return new Date(date).toLocaleDateString("vi-VN")
}

// LOAD DATA
const load = async () => {
  loading.value = true
  try {
    const res = await getAll()
    list.value = res.data
  } catch (err) {
    console.error("Lỗi load dữ liệu:", err)
  } finally {
    loading.value = false
  }
}

onMounted(load)

// RESET FORM
const reset = () => {
  form.value = {
    id: null,
    maKhachHang: "",
    hoTen: "",
    email: "",
    matKhau: "",
    soDienThoai: "",
    gioiTinh: true,
    ngaySinh: "",
    trangThai: true,
    daXacThuc: false
  }
}

// SAVE CUSTOMER
const save = async () => {
  if (!form.value.hoTen || !form.value.hoTen.trim()) {
    alert("Họ tên không được để trống!");
    return;
  }
  if (!form.value.email || !form.value.email.trim()) {
    alert("Email không được để trống!");
    return;
  }
  if (!form.value.soDienThoai || !form.value.soDienThoai.trim()) {
    alert("Số điện thoại không được để trống!");
    return;
  }
  try {
    if (form.value.id) {
      await update(form.value.id, form.value)
    } else {
      await create(form.value)
    }
    reset()
    load()
  } catch (err) {
    console.error(err)
    alert(err.response?.data?.message || "Lỗi khi lưu thông tin khách hàng");
  }
}

// EDIT
const edit = (item) => {
  form.value = { ...item }
}

// DELETE
const del = async (id) => {
  if (!confirm("Bạn có chắc muốn xóa khách hàng này?")) return
  try {
    await remove(id)
    load()
  } catch (err) {
    console.error(err)
  }
}

// ADDRESS MANAGEMENT
const openAddresses = async (customer) => {
  selectedCustomer.value = customer
  showAddressModal.value = true
  await loadAddresses()
}

const loadAddresses = async () => {
  try {
    const res = await getAddresses(selectedCustomer.value.id)
    addressList.value = res.data
  } catch (err) {
    console.error("Lỗi load địa chỉ:", err)
  }
}

const saveAddress = async () => {
  if (!addressForm.value.hoTenNguoiNhan || !addressForm.value.hoTenNguoiNhan.trim()) {
    alert("Tên người nhận không được để trống!");
    return;
  }
  if (!addressForm.value.soDienThoai || !addressForm.value.soDienThoai.trim()) {
    alert("Số điện thoại nhận không được để trống!");
    return;
  }
  if (!addressForm.value.tinhThanh || !addressForm.value.tinhThanh.trim()) {
    alert("Tỉnh / Thành phố không được để trống!");
    return;
  }
  if (!addressForm.value.phuongXa || !addressForm.value.phuongXa.trim()) {
    alert("Phường / Xã không được để trống!");
    return;
  }
  if (!addressForm.value.diaChiChiTiet || !addressForm.value.diaChiChiTiet.trim()) {
    alert("Địa chỉ chi tiết không được để trống!");
    return;
  }

  try {
    const data = {
      ...addressForm.value,
      idKhachHang: selectedCustomer.value.id
    }
    if (addressForm.value.id) {
      await updateAddress(addressForm.value.id, data)
    } else {
      await createAddress(data)
    }
    resetAddressForm()
    await loadAddresses()
    alert("Lưu địa chỉ thành công!");
  } catch (err) {
    console.error(err)
    alert("Lỗi khi lưu địa chỉ!");
  }
}

const editAddress = (addr) => {
  addressForm.value = { ...addr }
}

const deleteAddress = async (addrId) => {
  if (!confirm("Bạn có chắc muốn xóa địa chỉ này?")) return
  try {
    await removeAddress(addrId)
    await loadAddresses()
  } catch (err) {
    console.error(err)
  }
}

const resetAddressForm = () => {
  addressForm.value = {
    id: null,
    hoTenNguoiNhan: "",
    soDienThoai: "",
    tinhThanh: "",
    quanHuyen: "",
    phuongXa: "",
    diaChiChiTiet: ""
  }
}

// PAGINATION
const totalPages = computed(() => Math.ceil(list.value.length / pageSize))
const paginatedList = computed(() => {
  const start = (page.value - 1) * pageSize
  return list.value.slice(start, start + pageSize)
})
const changePage = (p) => {
  page.value = p
}
</script>

<template>
  <div class="container">
    <h2 class="title">👥 QUẢN LÝ KHÁCH HÀNG</h2>

    <!-- FORM BOX -->
    <div class="card form-box">
      <h3>{{ form.id ? "Cập nhật thông tin khách hàng" : "Thêm khách hàng mới" }}</h3>
      
      <div class="grid">
        <div class="form-group" v-if="form.id">
          <label>Mã khách hàng</label>
          <input v-model="form.maKhachHang" disabled placeholder="Tự động sinh" />
        </div>

        <div class="form-group">
          <label>Họ tên <span class="required">*</span></label>
          <input v-model="form.hoTen" placeholder="Nhập họ tên" />
        </div>

        <div class="form-group">
          <label>Email <span class="required">*</span></label>
          <input v-model="form.email" placeholder="Nhập email" />
        </div>

        <div class="form-group">
          <label>Số điện thoại <span class="required">*</span></label>
          <input v-model="form.soDienThoai" placeholder="Nhập số điện thoại" />
        </div>

        <div class="form-group">
          <label>Ngày sinh</label>
          <input v-model="form.ngaySinh" type="date" />
        </div>
      </div>

      <div class="grid mt-2">
        <div class="form-group">
          <label>Giới tính</label>
          <select v-model="form.gioiTinh">
            <option :value="true">Nam</option>
            <option :value="false">Nữ</option>
          </select>
        </div>
        
        <div class="form-group">
          <label>Trạng thái</label>
          <select v-model="form.trangThai">
            <option :value="true">Hoạt động</option>
            <option :value="false">Khóa</option>
          </select>
        </div>
      </div>

      <div class="mt-2 text-start" v-if="!form.id">
        <small class="text-muted">💡 Mật khẩu mặc định khi tạo mới là <strong style="color: #0d6efd;">123456</strong> (Khách hàng có thể đổi lại sau)</small>
      </div>

      <div class="actions">
        <button class="btn-save" @click="save">💾 Lưu khách hàng</button>
        <button class="btn-reset" @click="reset">Reset</button>
      </div>
    </div>

    <!-- TABLE -->
    <div class="card">
      <table v-if="list.length > 0">
        <thead>
          <tr>
            <th>Mã KH</th>
            <th>Họ Tên</th>
            <th>Email</th>
            <th>Số điện thoại</th>
            <th>Giới tính</th>
            <th>Ngày sinh</th>
            <th>Trạng thái</th>
            <th>Hành động</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="i in paginatedList" :key="i.id">
            <td>{{ i.maKhachHang }}</td>
            <td>{{ i.hoTen }}</td>
            <td>{{ i.email }}</td>
            <td>{{ i.soDienThoai }}</td>
            <td>{{ i.gioiTinh ? "Nam" : "Nữ" }}</td>
            <td>{{ formatDate(i.ngaySinh) }}</td>
            <td>
              <span :class="i.trangThai ? 'active' : 'hidden'">
                {{ i.trangThai ? "Hoạt động" : "Khóa" }}
              </span>
            </td>
            <td class="btn-group">
              <button @click="openAddresses(i)" title="Quản lý địa chỉ">🏠</button>
              <button @click="edit(i)" title="Sửa">✏️</button>
              <button @click="del(i.id)" title="Xóa">🗑</button>
            </td>
          </tr>
        </tbody>
      </table>
      
      <div class="pagination" v-if="totalPages > 1">
        <button @click="page--" :disabled="page === 1">Trước</button>
        <button v-for="p in totalPages" :key="p" @click="changePage(p)" :class="{ activePage: p === page }">
          {{ p }}
        </button>
        <button @click="page++" :disabled="page === totalPages">Sau</button>
      </div>
    </div>

    <!-- ADDRESSES MODAL -->
    <div v-if="showAddressModal" class="modal-overlay">
      <div class="modal large-modal">
        <h3>🏠 Địa chỉ của: {{ selectedCustomer?.hoTen }}</h3>
        
        <div class="address-layout">
          <!-- Form address -->
          <div class="address-form">
            <h4>{{ addressForm.id ? "Sửa địa chỉ" : "Thêm địa chỉ mới" }}</h4>
            
            <div class="form-group">
              <label>Tên người nhận <span class="required">*</span></label>
              <input v-model="addressForm.hoTenNguoiNhan" placeholder="Tên người nhận" />
            </div>
            
            <div class="form-group">
              <label>Số điện thoại nhận <span class="required">*</span></label>
              <input v-model="addressForm.soDienThoai" placeholder="Số điện thoại nhận" />
            </div>
            
            <div class="form-group">
              <label>Tỉnh / Thành phố <span class="required">*</span></label>
              <input v-model="addressForm.tinhThanh" placeholder="Tỉnh / Thành phố" />
            </div>
            
            <div class="form-group">
              <label>Phường / Xã <span class="required">*</span></label>
              <input v-model="addressForm.phuongXa" placeholder="Phường / Xã" />
            </div>
            
            <div class="form-group">
              <label>Địa chỉ chi tiết <span class="required">*</span></label>
              <textarea v-model="addressForm.diaChiChiTiet" placeholder="Địa chỉ chi tiết"></textarea>
            </div>
            
            <div class="btn-group mt-2">
              <button class="btn-save" @click="saveAddress">💾 Lưu địa chỉ</button>
              <button class="btn-reset" @click="resetAddressForm">Reset</button>
            </div>
          </div>

          <!-- List address -->
          <div class="address-list">
            <h4>Danh sách địa chỉ</h4>
            <div v-if="addressList.length === 0">Chưa có địa chỉ nào.</div>
            <div v-for="addr in addressList" :key="addr.id" class="address-item">
              <p><strong>{{ addr.hoTenNguoiNhan }}</strong> ({{ addr.soDienThoai }})</p>
              <p>{{ addr.diaChiChiTiet }}, {{ addr.phuongXa }}, {{ addr.tinhThanh }}</p>
              <div class="addr-actions">
                <button @click="editAddress(addr)">✏️</button>
                <button @click="deleteAddress(addr.id)">🗑</button>
              </div>
            </div>
          </div>
        </div>

        <button class="btn-close" @click="showAddressModal = false">Đóng</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  padding: 20px;
  font-family: Inter, Arial, sans-serif;
}
.title {
  margin-bottom: 15px;
}
.card {
  background: #fff;
  padding: 15px;
  border-radius: 10px;
  margin-bottom: 15px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}
.form-box input, .form-box textarea, .form-box select,
.address-form input, .address-form textarea, .address-form select {
  width: 100%;
  margin: 5px 0 12px 0;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
  transition: border-color 0.2s;
}
.form-box input:focus, .form-box select:focus,
.address-form input:focus, .address-form select:focus, .address-form textarea:focus {
  border-color: #0d6efd;
  outline: none;
}
.form-box input:disabled {
  background-color: #f1f3f5;
  color: #495057;
  cursor: not-allowed;
  border-color: #dee2e6;
}
.form-group {
  display: flex;
  flex-direction: column;
}
.form-group label, .address-form label {
  font-size: 13px;
  font-weight: 600;
  color: #495057;
  margin-bottom: 2px;
  text-align: left;
}
.required {
  color: #dc3545;
  margin-left: 2px;
}
.grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 15px;
}
.actions {
  margin-top: 10px;
}
button {
  margin-right: 5px;
  cursor: pointer;
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
}
.btn-save {
  background: green;
  color: white;
}
.btn-reset {
  background: gray;
  color: white;
}
.active {
  color: green;
  font-weight: bold;
}
.hidden {
  color: red;
  font-weight: bold;
}
table {
  width: 100%;
  border-collapse: collapse;
}
th, td {
  padding: 10px;
  border-bottom: 1px solid #ddd;
  text-align: left;
}
.btn-group button {
  background: #f0f0f0;
}
.btn-group button:hover {
  background: #e0e0e0;
}
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0,0,0,0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1100;
}
.modal {
  background: white;
  padding: 20px;
  border-radius: 10px;
  width: 500px;
}
.large-modal {
  width: 800px;
}
.btn-close {
  margin-top: 15px;
  background: red;
  color: white;
}
.address-layout {
  display: flex;
  gap: 20px;
  margin-top: 15px;
}
.address-form {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.address-form input, .address-form textarea {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.address-list {
  flex: 1.2;
  max-height: 400px;
  overflow-y: auto;
}
.address-item {
  border: 1px solid #eee;
  padding: 10px;
  border-radius: 6px;
  margin-bottom: 10px;
  position: relative;
}
.addr-actions {
  position: absolute;
  top: 5px;
  right: 5px;
}
.addr-actions button {
  padding: 2px 6px;
  font-size: 12px;
}
.pagination {
  margin-top: 15px;
  display: flex;
  justify-content: center;
  gap: 5px;
}
.pagination button {
  background: #e9ecef;
}
.activePage {
  background: #0d6efd !important;
  color: white;
}
</style>
