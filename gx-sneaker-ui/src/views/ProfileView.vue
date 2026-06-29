<script setup>
import { ref, onMounted } from 'vue'
import { getMeApi } from '@/api/authApi'
import { getByKhachHangId as getAddresses, create as createAddress, update as updateAddress, remove as removeAddress } from '@/services/diaChiService'

const user = ref(null)
const errorMessage = ref('')
const addressList = ref([])
const showForm = ref(false)

const addressForm = ref({
  id: null,
  hoTenNguoiNhan: "",
  soDienThoai: "",
  tinhThanh: "",
  quanHuyen: "",
  phuongXa: "",
  diaChiChiTiet: ""
})

const loadProfileAndAddresses = async () => {
  try {
    const response = await getMeApi()
    user.value = response.data
    
    if (user.value && user.value.id) {
      const addrRes = await getAddresses(user.value.id)
      addressList.value = addrRes.data || []
    }
  } catch (error) {
    errorMessage.value = 'Không thể lấy thông tin tài khoản hoặc địa chỉ'
    console.error(error)
  }
}

onMounted(loadProfileAndAddresses)

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
  showForm.value = false
}

const editAddress = (addr) => {
  addressForm.value = { ...addr }
  showForm.value = true
}

const saveAddress = async () => {
  if (!addressForm.value.hoTenNguoiNhan || !addressForm.value.hoTenNguoiNhan.trim()) {
    alert("Tên người nhận không được để trống!")
    return
  }
  if (!addressForm.value.soDienThoai || !addressForm.value.soDienThoai.trim()) {
    alert("Số điện thoại không được để trống!")
    return
  }
  if (!addressForm.value.tinhThanh || !addressForm.value.tinhThanh.trim()) {
    alert("Tỉnh / Thành phố không được để trống!")
    return
  }
  if (!addressForm.value.phuongXa || !addressForm.value.phuongXa.trim()) {
    alert("Phường / Xã không được để trống!")
    return
  }
  if (!addressForm.value.diaChiChiTiet || !addressForm.value.diaChiChiTiet.trim()) {
    alert("Địa chỉ chi tiết không được để trống!")
    return
  }

  try {
    const data = {
      ...addressForm.value,
      idKhachHang: user.value.id
    }
    if (addressForm.value.id) {
      await updateAddress(addressForm.value.id, data)
    } else {
      await createAddress(data)
    }
    resetAddressForm()
    await loadProfileAndAddresses()
    alert("Lưu địa chỉ thành công!")
  } catch (err) {
    console.error(err)
    alert("Lỗi khi lưu địa chỉ!")
  }
}

const deleteAddress = async (addrId) => {
  if (!confirm("Bạn có chắc muốn xóa địa chỉ này?")) return
  try {
    await removeAddress(addrId)
    await loadProfileAndAddresses()
  } catch (err) {
    console.error(err)
    alert("Lỗi khi xóa địa chỉ!")
  }
}
</script>

<template>
  <div class="profile-container">
    <div class="profile-header">
      <h2>👤 Hồ sơ tài khoản</h2>
      <p class="subtitle">Quản lý thông tin cá nhân và sổ địa chỉ nhận hàng của bạn.</p>
    </div>

    <p v-if="errorMessage" class="error-msg">
      ⚠️ {{ errorMessage }}
    </p>

    <div class="profile-grid" v-if="user">
      <!-- Cột trái: Thông tin tài khoản -->
      <div class="profile-card info-section">
        <h3>Thông tin cá nhân</h3>
        <div class="info-group">
          <label>Mã khách hàng:</label>
          <span>{{ user.maKhachHang }}</span>
        </div>
        <div class="info-group">
          <label>Họ và tên:</label>
          <span>{{ user.hoTen }}</span>
        </div>
        <div class="info-group">
          <label>Email:</label>
          <span>{{ user.email }}</span>
        </div>
        <div class="info-group">
          <label>Số điện thoại:</label>
          <span>{{ user.soDienThoai }}</span>
        </div>
        <div class="info-group">
          <label>Giới tính:</label>
          <span>{{ user.gioiTinh ? "Nam" : "Nữ" }}</span>
        </div>
        <div class="info-group" v-if="user.ngaySinh">
          <label>Ngày sinh:</label>
          <span>{{ new Date(user.ngaySinh).toLocaleDateString("vi-VN") }}</span>
        </div>
      </div>

      <!-- Cột phải: Danh sách địa chỉ nhận hàng -->
      <div class="profile-card address-section">
        <div class="section-header">
          <h3>Sổ địa chỉ nhận hàng</h3>
          <button v-if="!showForm" class="btn-add" @click="showForm = true">
            ➕ Thêm địa chỉ mới
          </button>
        </div>

        <!-- Form Địa chỉ -->
        <div v-if="showForm" class="address-form-box">
          <h4>{{ addressForm.id ? "✏️ Chỉnh sửa địa chỉ" : "➕ Thêm địa chỉ nhận hàng" }}</h4>
          
          <div class="form-grid">
            <div class="form-item">
              <label>Họ tên người nhận <span class="req">*</span></label>
              <input v-model="addressForm.hoTenNguoiNhan" placeholder="Nhập họ tên" />
            </div>
            
            <div class="form-item">
              <label>Số điện thoại nhận <span class="req">*</span></label>
              <input v-model="addressForm.soDienThoai" placeholder="Nhập số điện thoại" />
            </div>
          </div>

          <div class="form-grid">
            <div class="form-item">
              <label>Tỉnh / Thành phố <span class="req">*</span></label>
              <input v-model="addressForm.tinhThanh" placeholder="Tỉnh / Thành phố" />
            </div>

            <div class="form-item">
              <label>Phường / Xã <span class="req">*</span></label>
              <input v-model="addressForm.phuongXa" placeholder="Phường / Xã" />
            </div>
          </div>

          <div class="form-item">
            <label>Địa chỉ chi tiết <span class="req">*</span></label>
            <textarea v-model="addressForm.diaChiChiTiet" rows="3" placeholder="Số nhà, tên đường, ngõ ngách..."></textarea>
          </div>

          <div class="form-actions">
            <button class="btn-save" @click="saveAddress">💾 Lưu địa chỉ</button>
            <button class="btn-cancel" @click="resetAddressForm">Hủy</button>
          </div>
        </div>

        <!-- Danh sách địa chỉ hiển thị -->
        <div class="address-list">
          <div v-if="addressList.length === 0" class="no-address">
            <p>Chưa có địa chỉ giao hàng nào được lưu. Hãy thêm mới để thanh toán nhanh hơn!</p>
          </div>
          
          <div v-for="addr in addressList" :key="addr.id" class="address-item-card">
            <div class="addr-details">
              <p class="addr-recipient">
                <strong>{{ addr.hoTenNguoiNhan }}</strong>
                <span class="addr-phone">📞 {{ addr.soDienThoai }}</span>
              </p>
              <p class="addr-text">
                {{ addr.diaChiChiTiet }}, {{ addr.phuongXa }}, {{ addr.tinhThanh }}
              </p>
            </div>
            <div class="addr-actions">
              <button class="btn-edit" @click="editAddress(addr)" title="Chỉnh sửa">✏️</button>
              <button class="btn-delete" @click="deleteAddress(addr.id)" title="Xóa">🗑️</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.profile-container {
  max-width: 1200px;
  margin: 40px auto;
  padding: 0 20px;
  font-family: Inter, sans-serif;
}

.profile-header {
  margin-bottom: 30px;
}

.profile-header h2 {
  font-size: 32px;
  font-weight: 800;
  color: #1e293b;
  margin-bottom: 6px;
}

.subtitle {
  color: #64748b;
  font-size: 15px;
}

.error-msg {
  background: #fef2f2;
  color: #ef4444;
  padding: 12px 16px;
  border-radius: 10px;
  margin-bottom: 20px;
  border: 1px solid #fee2e2;
  font-size: 14px;
}

.profile-grid {
  display: grid;
  grid-template-columns: 1fr 2fr;
  gap: 30px;
}

@media (max-width: 992px) {
  .profile-grid {
    grid-template-columns: 1fr;
  }
}

.profile-card {
  background: #fff;
  padding: 28px;
  border-radius: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05);
  border: 1px solid rgba(0, 0, 0, 0.04);
}

.profile-card h3 {
  font-size: 20px;
  font-weight: 800;
  color: #1e293b;
  margin-bottom: 20px;
  border-bottom: 2px solid #f1f5f9;
  padding-bottom: 12px;
}

.info-group {
  display: flex;
  margin-bottom: 16px;
  font-size: 14px;
}

.info-group label {
  width: 130px;
  font-weight: 600;
  color: #64748b;
}

.info-group span {
  color: #1e293b;
  font-weight: 500;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  border-bottom: 2px solid #f1f5f9;
  padding-bottom: 12px;
}

.section-header h3 {
  border-bottom: none;
  margin-bottom: 0;
  padding-bottom: 0;
}

.btn-add {
  padding: 8px 16px;
  border: none;
  background: #0ea5e9;
  color: white;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s;
}

.btn-add:hover {
  background: #0284c7;
}

.address-form-box {
  background: #f8fafc;
  padding: 20px;
  border-radius: 14px;
  margin-bottom: 24px;
  border: 1px solid #e2e8f0;
}

.address-form-box h4 {
  margin-bottom: 16px;
  font-size: 16px;
  color: #1e293b;
  font-weight: 700;
}

.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.form-grid-three {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 16px;
  margin-top: 16px;
}

@media (max-width: 576px) {
  .form-grid, .form-grid-three {
    grid-template-columns: 1fr;
  }
}

.form-item {
  display: flex;
  flex-direction: column;
  margin-bottom: 16px;
}

.form-item label {
  font-size: 13px;
  font-weight: 600;
  color: #475569;
  margin-bottom: 6px;
}

.req {
  color: #ef4444;
}

.form-item input, .form-item textarea {
  padding: 10px 14px;
  border: 1px solid #cbd5e1;
  border-radius: 8px;
  font-size: 14px;
  font-family: inherit;
}

.form-item input:focus, .form-item textarea:focus {
  outline: none;
  border-color: #0ea5e9;
  box-shadow: 0 0 0 3px rgba(14, 165, 233, 0.15);
}

.form-actions {
  display: flex;
  gap: 12px;
  margin-top: 10px;
}

.btn-save {
  padding: 10px 20px;
  background: #10b981;
  color: white;
  border: none;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
}

.btn-save:hover {
  background: #059669;
}

.btn-cancel {
  padding: 10px 20px;
  background: #64748b;
  color: white;
  border: none;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
}

.btn-cancel:hover {
  background: #475569;
}

.no-address {
  text-align: center;
  padding: 30px;
  color: #64748b;
  font-size: 14px;
  background: #f8fafc;
  border-radius: 12px;
  border: 1px dashed #cbd5e1;
}

.address-item-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 18px;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  margin-bottom: 16px;
  transition: all 0.2s;
}

.address-item-card:hover {
  border-color: #cbd5e1;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.02);
}

.addr-recipient {
  font-size: 16px;
  color: #1e293b;
  margin-bottom: 6px;
}

.addr-phone {
  font-size: 13px;
  color: #64748b;
  margin-left: 12px;
  font-weight: normal;
}

.addr-text {
  font-size: 14px;
  color: #475569;
}

.addr-actions {
  display: flex;
  gap: 8px;
}

.addr-actions button {
  background: #f1f5f9;
  border: none;
  padding: 8px 10px;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.2s;
}

.addr-actions button:hover {
  background: #e2e8f0;
}
</style>
