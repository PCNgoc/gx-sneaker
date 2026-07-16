<template>
  <div class="page">
    <!-- ================= HEADER ================= -->

    <div class="page-header">
      <div>
        <h2>Quản lý nhân viên</h2>
        <p>Quản lý thông tin nhân viên GX Sneaker</p>
      </div>

      <button class="btn-refresh" @click="loadNhanVien">🔄 Làm mới</button>
    </div>

    <!-- ================= DASHBOARD ================= -->

    <div class="summary">
      <div class="summary-card">
        <h4>Tổng nhân viên</h4>

        <h2>{{ tongNhanVien }}</h2>
      </div>

      <div class="summary-card success">
        <h4>Đang hoạt động</h4>

        <h2>{{ dangHoatDong }}</h2>
      </div>

      <div class="summary-card danger">
        <h4>Ngừng hoạt động</h4>

        <h2>{{ ngungHoatDong }}</h2>
      </div>
    </div>

    <!-- ================= TOOLBAR ================= -->

    <div class="toolbar">
      <input v-model="keyword" placeholder="🔍 Tìm theo mã, tên, email..." />

      <select v-model="filterTrangThai">
        <option value="">Tất cả trạng thái</option>

        <option value="true">Hoạt động</option>

        <option value="false">Ngừng hoạt động</option>
      </select>

      <button class="btn-add" @click="clearForm">+ Thêm mới</button>
    </div>

    <!-- ================= TABLE ================= -->

    <div class="table-card">
      <table>
        <thead>
          <tr>
            <th width="60">STT</th>

            <th width="80">Ảnh</th>

            <th>Mã NV</th>

            <th>Họ tên</th>

            <th>Email</th>

            <th>SĐT</th>

            <th>Quyền</th>

            <th>Trạng thái</th>

            <th width="180">Thao tác</th>
          </tr>
        </thead>

        <tbody>
          <tr v-if="filteredNhanVien.length === 0">
            <td colspan="9" class="empty">Không có dữ liệu</td>
          </tr>

          <tr
            v-for="(item, index) in filteredNhanVien"
            :key="item.id"
            @click="selectNhanVien(item)"
          >
            <td>
              {{ index + 1 }}
            </td>

            <td>
              <img
                v-if="item.anhDaiDien"
                :src="`http://localhost:8080/uploads/avatars/${item.anhDaiDien}`"
                class="avatar"
                @error="onImageError"
              />

              <img
                v-else
                src="https://ui-avatars.com/api/?background=dc2626&color=fff&name=GX"
                class="avatar"
              />
            </td>

            <td>
              {{ item.maNhanVien }}
            </td>

            <td>
              {{ item.hoTen }}
            </td>

            <td>
              {{ item.email }}
            </td>

            <td>
              {{ item.soDienThoai }}
            </td>

            <td>
              <span class="badge role">
                {{ item.tenQuyen }}
              </span>
            </td>

            <td>
              <span :class="item.trangThai ? 'badge active' : 'badge inactive'">
                {{ item.trangThai ? 'Hoạt động' : 'Ngừng' }}
              </span>
            </td>

            <td>
              <button class="btn-edit" @click.stop="selectNhanVien(item)">✏️ Sửa</button>

              <button class="btn-delete" @click.stop="deleteNhanVien(item.id)">🗑 Xóa</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- ================= LOADING ================= -->

    <div v-if="loading" class="loading">Đang tải dữ liệu...</div>

    <!-- ================= FORM ================= -->

    <div class="form-card">
      <h3>
        {{ form.id ? 'Cập nhật nhân viên' : 'Thêm nhân viên' }}
      </h3>

      <div class="grid">
        <div class="form-group">
          <label>Mã nhân viên</label>

          <input v-model="form.maNhanVien" placeholder="NV001" />
        </div>

        <div class="form-group">
          <label>Họ tên</label>

          <input v-model="form.hoTen" placeholder="Nguyễn Văn A" />
        </div>

        <div class="form-group">
          <label>Email</label>

          <input v-model="form.email" type="email" />
        </div>

        <div class="form-group">
          <label>Mật khẩu</label>

          <input v-model="form.matKhau" type="password" />
        </div>

        <div class="form-group">
          <label>Số điện thoại</label>

          <input v-model="form.soDienThoai" />
        </div>

        <div class="form-group">
          <label>Giới tính</label>

          <select v-model="form.gioiTinh">
            <option :value="true">Nam</option>

            <option :value="false">Nữ</option>
          </select>
        </div>

        <div class="form-group">
          <label>Địa chỉ</label>

          <input v-model="form.diaChi" />
        </div>

        <div class="form-group">
          <label>Quyền</label>

          <select v-model="form.idPhanQuyen">
            <option :value="1">ADMIN</option>

            <option :value="2">STAFF</option>
          </select>
        </div>

        <div class="form-group">
          <label>Trạng thái</label>

          <select v-model="form.trangThai">
            <option :value="true">Hoạt động</option>

            <option :value="false">Ngừng hoạt động</option>
          </select>
        </div>

        <div class="form-group">
          <label>Ảnh đại diện</label>

          <input type="file" accept="image/*" @change="chooseAvatar" />

          <div class="avatar-box">
            <!-- Ảnh vừa chọn -->
            <img v-if="preview" :src="preview" class="avatar" />

            <!-- Ảnh đã lưu trong database -->
            <img
              v-else-if="form.anhDaiDien"
              :src="'http://localhost:8080/avatars/' + form.anhDaiDien"
              class="avatar"
            />
          </div>
        </div>
      </div>

      <div class="button-group">
        <button class="btn-save" @click="addNhanVien">Thêm</button>

        <button class="btn-update" @click="updateNhanVien">Cập nhật</button>

        <button class="btn-reset" @click="clearForm">Làm mới</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import nhanVienService from '@/services/nhanVienService'
import { success, error, confirmDelete } from '@/utils/notify'
import uploadService from '@/services/uploadService'

// ================== DATA ==================

const preview = ref('')

const danhSach = ref([])

const loading = ref(false)

const saving = ref(false)

const keyword = ref('')

const filterTrangThai = ref('')

const onImageError = (e) => {
  console.log('Không tải được ảnh:', e.target.src)

  e.target.src = 'https://ui-avatars.com/api/?background=dc2626&color=fff&name=GX'
}

const form = ref({
  id: null,
  idPhanQuyen: 2,
  tenQuyen: '',

  maNhanVien: '',
  hoTen: '',
  email: '',
  matKhau: '',
  soDienThoai: '',

  gioiTinh: true,

  diaChi: '',

  anhDaiDien: '',

  trangThai: true,
})

// ================== THỐNG KÊ ==================

const tongNhanVien = computed(() => danhSach.value.length)

const dangHoatDong = computed(() => danhSach.value.filter((i) => i.trangThai).length)

const ngungHoatDong = computed(() => danhSach.value.filter((i) => !i.trangThai).length)

// ================== SEARCH + FILTER ==================

const filteredNhanVien = computed(() => {
  let data = [...danhSach.value]

  // tìm kiếm

  if (keyword.value.trim()) {
    const key = keyword.value.toLowerCase()

    data = data.filter((item) => {
      return (
        item.maNhanVien?.toLowerCase().includes(key) ||
        item.hoTen?.toLowerCase().includes(key) ||
        item.email?.toLowerCase().includes(key) ||
        item.soDienThoai?.includes(key)
      )
    })
  }

  // lọc trạng thái

  if (filterTrangThai.value !== '') {
    data = data.filter((item) => item.trangThai == (filterTrangThai.value === 'true'))
  }

  return data
})

// ================== LOAD ==================

const loadNhanVien = async () => {
  loading.value = true

  try {
    const res = await nhanVienService.getAll()
    console.log(res.data)
    danhSach.value = res.data
  } catch (e) {
    console.log(e)

    error('Không tải được dữ liệu')
  } finally {
    loading.value = false
  }
}

// ================== CLEAR ==================

const clearForm = () => {
  preview.value = ''
  form.value = {
    id: null,
    idPhanQuyen: 2,
    tenQuyen: '',

    maNhanVien: '',
    hoTen: '',
    email: '',
    matKhau: '',
    soDienThoai: '',

    gioiTinh: true,

    diaChi: '',

    anhDaiDien: '',

    trangThai: true,
  }
}

// ================== CHỌN ==================

const selectNhanVien = (item) => {
  form.value = {
    ...item,
    matKhau: '',
  }

  window.scrollTo({
    top: document.body.scrollHeight,
    behavior: 'smooth',
  })
}

// ================== VALIDATE ==================

const validate = () => {
  if (!form.value.maNhanVien?.trim()) {
    error('Không được để trống mã nhân viên')
    return false
  }

  if (!form.value.hoTen?.trim()) {
    error('Không được để trống họ tên')
    return false
  }

  if (!form.value.email?.trim()) {
    error('Không được để trống email')
    return false
  }

  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/

  if (!emailRegex.test(form.value.email)) {
    error('Email không hợp lệ')
    return false
  }

  // Chỉ kiểm tra mật khẩu khi THÊM MỚI
  if (!form.value.id) {
    if (!form.value.matKhau?.trim()) {
      error('Không được để trống mật khẩu')
      return false
    }

    if (form.value.matKhau.length < 6) {
      error('Mật khẩu tối thiểu 6 ký tự')
      return false
    }
  }

  if (!form.value.soDienThoai?.trim()) {
    error('Không được để trống số điện thoại')
    return false
  }

  const phoneRegex = /^[0-9]{10}$/

  if (!phoneRegex.test(form.value.soDienThoai)) {
    error('Số điện thoại gồm đúng 10 số')
    return false
  }

  if (!form.value.diaChi?.trim()) {
    error('Không được để trống địa chỉ')
    return false
  }

  return true
}
// ================== ADD ==================

const addNhanVien = async () => {
  if (!validate()) return

  try {
    await nhanVienService.add(form.value)

    success('Thêm nhân viên thành công')

    clearForm()

    await loadNhanVien()
  } catch (e) {
    console.log(e)

    error('Thêm thất bại')
  }
}

// ================== UPDATE ==================

const updateNhanVien = async () => {
  if (!validate()) return

  if (!form.value.id) {
    error('Chọn nhân viên')
    return
  }

  console.log('===== UPDATE =====')
  console.log(form.value)

  try {
    await nhanVienService.update(form.value.id, form.value)

    success('Cập nhật thành công')

    clearForm()

    await loadNhanVien()
  } catch (e) {
    console.log(e)
    console.log(e.response)
    console.log(e.response?.data)

    error('Cập nhật thất bại')
  }
}

// ================== DELETE ==================

const deleteNhanVien = async (id) => {
  const result = await confirmDelete()

  if (!result.isConfirmed) return

  try {
    await nhanVienService.delete(id)

    success('Đã xóa nhân viên')

    clearForm()

    await loadNhanVien()
  } catch (e) {
    console.log(e)

    error('Không thể xóa')
  }
}

const chooseAvatar = async (event) => {
  const file = event.target.files[0]

  if (!file) return

  preview.value = URL.createObjectURL(file)

  try {
    const res = await uploadService.upload(file)

    form.value.anhDaiDien = res.data

    success('Upload ảnh thành công')
  } catch (e) {
    console.log(e)

    error('Upload thất bại')
  }
}

// ================== INIT ==================

onMounted(() => {
  loadNhanVien()
})
</script>

<style scoped>
/* ====================== PAGE ====================== */

.page {
  padding: 30px;
  background: #f5f7fb;
  min-height: 100vh;
}

/* ====================== HEADER ====================== */

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
}

.page-header h2 {
  margin: 0;
  color: #dc2626;
  font-size: 30px;
  font-weight: 700;
}

.page-header p {
  margin-top: 6px;
  color: #6b7280;
}

/* ====================== SUMMARY ====================== */

.summary {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-bottom: 30px;
}

.summary-card {
  background: white;
  border-radius: 14px;
  padding: 24px;
  box-shadow: 0 4px 18px rgba(0, 0, 0, 0.08);
  transition: 0.25s;
}

.summary-card:hover {
  transform: translateY(-5px);
}

.summary-card h4 {
  margin-bottom: 10px;
  color: #666;
  font-weight: 500;
}

.summary-card h2 {
  margin: 0;
  font-size: 34px;
  color: #dc2626;
}

.summary-card.success h2 {
  color: #16a34a;
}

.summary-card.danger h2 {
  color: #ef4444;
}

/* ====================== TOOLBAR ====================== */

.toolbar {
  display: flex;
  gap: 15px;
  margin-bottom: 25px;
}

.toolbar input,
.toolbar select {
  padding: 12px 15px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background: white;
  transition: 0.25s;
}

.toolbar input {
  flex: 1;
}

.toolbar input:focus,
.toolbar select:focus {
  outline: none;
  border-color: #dc2626;
  box-shadow: 0 0 0 4px rgba(220, 38, 38, 0.12);
}

/* ====================== TABLE ====================== */

.table-card {
  background: white;
  border-radius: 12px;
  overflow-x: auto;
  box-shadow: 0 4px 18px rgba(0, 0, 0, 0.08);
  margin-bottom: 30px;
}

table {
  width: 100%;
  border-collapse: collapse;
  min-width: 1200px;
}

thead th {
  background: #111827;
  color: white;
  padding: 15px;
  text-align: center;
  white-space: nowrap;
}

tbody td {
  padding: 14px;
  border-bottom: 1px solid #eee;
  text-align: center;
}

tbody tr {
  transition: 0.25s;
}

tbody tr:hover {
  background: #f8fafc;
  cursor: pointer;
}

.empty {
  padding: 40px;
  text-align: center;
  color: #999;
}

/* ====================== AVATAR ====================== */

.avatar {
  width: 46px;
  height: 46px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #e5e7eb;
}

/* ====================== BADGE ====================== */

.badge {
  display: inline-block;
  padding: 6px 14px;
  border-radius: 30px;
  font-size: 13px;
  color: white;
  font-weight: 600;
}

.active {
  background: #16a34a;
}

.inactive {
  background: #ef4444;
}

.role {
  background: #2563eb;
}

/* ====================== FORM ====================== */

.form-card {
  background: white;
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 4px 18px rgba(0, 0, 0, 0.08);
}

.form-card h3 {
  margin-bottom: 25px;
  color: #dc2626;
}

.grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-group label {
  margin-bottom: 8px;
  font-weight: 600;
}

.form-group input,
.form-group select {
  padding: 12px;
  border-radius: 8px;
  border: 1px solid #ddd;
  transition: 0.25s;
}

.form-group input:focus,
.form-group select:focus {
  outline: none;
  border-color: #dc2626;
  box-shadow: 0 0 0 4px rgba(220, 38, 38, 0.12);
}

/* ====================== BUTTON ====================== */

.button-group {
  display: flex;
  gap: 15px;
  margin-top: 30px;
}

button {
  border: none;
  padding: 11px 22px;
  border-radius: 8px;
  cursor: pointer;
  color: white;
  font-weight: 600;
  transition: 0.25s;
}

button:hover {
  transform: translateY(-2px);
  opacity: 0.92;
}

button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-add,
.btn-save {
  background: #16a34a;
}

.btn-update {
  background: #2563eb;
}

.btn-refresh {
  background: #6b7280;
}

.btn-edit {
  background: #f59e0b;
  color: black;
  margin-right: 6px;
}

.btn-delete {
  background: #dc2626;
}

.btn-reset,
.btn-cancel {
  background: #6b7280;
}

/* ====================== LOADING ====================== */

.loading {
  background: white;
  padding: 30px;
  border-radius: 10px;
  text-align: center;
  font-size: 18px;
  color: #2563eb;
  font-weight: bold;
}

/* ====================== SCROLL ====================== */

.table-card::-webkit-scrollbar {
  height: 8px;
}

.table-card::-webkit-scrollbar-thumb {
  background: #c4c4c4;
  border-radius: 20px;
}

.table-card::-webkit-scrollbar-track {
  background: #f1f1f1;
}

/* ====================== RESPONSIVE ====================== */

@media (max-width: 992px) {
  .summary {
    grid-template-columns: 1fr;
  }

  .grid {
    grid-template-columns: 1fr;
  }

  .toolbar {
    flex-direction: column;
  }

  .button-group {
    flex-wrap: wrap;
  }
}

@media (max-width: 768px) {
  .page {
    padding: 15px;
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }

  .page-header h2 {
    font-size: 24px;
  }

  table {
    min-width: 1100px;
    font-size: 14px;
  }

  .button-group {
    flex-direction: column;
  }

  .button-group button {
    width: 100%;
  }
}

.btn-cancel {
  background: #6b7280;
}

/* ================= LOADING ================= */

.loading {
  background: white;
  padding: 30px;
  text-align: center;
  border-radius: 10px;
  color: #2563eb;
  font-size: 18px;
  font-weight: bold;
}

/* ================= SCROLL ================= */

.table-card::-webkit-scrollbar {
  height: 8px;
}

.table-card::-webkit-scrollbar-thumb {
  background: #bbb;
  border-radius: 20px;
}

/* ================= RESPONSIVE ================= */

@media (max-width: 992px) {
  .summary {
    grid-template-columns: 1fr;
  }

  .grid {
    grid-template-columns: 1fr;
  }

  .toolbar {
    flex-direction: column;
  }

  .actions {
    flex-wrap: wrap;
  }
}

@media (max-width: 768px) {
  .page {
    padding: 15px;
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }

  .page-header h2 {
    font-size: 24px;
  }

  table {
    font-size: 14px;
  }

  button {
    width: 100%;
  }

  .actions {
    flex-direction: column;
  }
}

.avatar-box {
  margin-top: 15px;
}

.avatar {
  width: 130px;

  height: 130px;

  object-fit: cover;

  border-radius: 50%;

  border: 3px solid #ddd;
}
</style>
