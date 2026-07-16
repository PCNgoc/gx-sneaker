import axios from 'axios'

const API = 'http://localhost:8080/api/upload'

export default {
  upload(file) {
    const formData = new FormData()

    formData.append('file', file)

    return axios.post(API + '/avatar', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
  },
}
