import axios from 'axios'

const API_URL = 'http://localhost:8080/api/nhan-vien'

const api = axios.create({
  baseURL: API_URL,
})

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('adminToken')

  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }

  return config
})

export default {
  getAll() {
    return api.get('')
  },

  getById(id) {
    return api.get(`/${id}`)
  },

  add(data) {
    return api.post('', data)
  },

  update(id, data) {
    return api.put(`/${id}`, data)
  },

  delete(id) {
    return api.delete(`/${id}`)
  },
}
