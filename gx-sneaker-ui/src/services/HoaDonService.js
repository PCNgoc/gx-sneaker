import axios from 'axios'

const API_URL = 'http://localhost:8080/api/hoa-don'

const getAuthHeader = () => {
  const token = localStorage.getItem('token')

  return {
    headers: token
      ? {
        Authorization: `Bearer ${token}`,
      }
      : {},
  }
}

export const datHang = (data) => {
  return axios.post(`${API_URL}/dat-hang`, data, getAuthHeader())
}

export const getHoaDonByKhachHang = (idKhachHang) => {
  return axios.get(`${API_URL}/khach-hang/${idKhachHang}`, getAuthHeader())
}

export const getHoaDonById = (idHoaDon) => {
  return axios.get(`${API_URL}/${idHoaDon}`, getAuthHeader())
}

export const apDungMaGiamGia = (data) => {
  return axios.post(`${API_URL}/ap-dung-ma-giam-gia`, data)
}

export default {
  datHang,
  getHoaDonByKhachHang,
  getHoaDonById,
  apDungMaGiamGia,
}
