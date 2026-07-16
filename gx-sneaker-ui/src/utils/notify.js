import Swal from 'sweetalert2'

export const success = (text) => {
  Swal.fire({
    icon: 'success',

    title: 'Thành công',

    text,

    timer: 1500,

    showConfirmButton: false,
  })
}

export const error = (text) => {
  Swal.fire({
    icon: 'error',

    title: 'Lỗi',

    text,
  })
}

export const confirmDelete = () => {
  return Swal.fire({
    title: 'Bạn chắc chắn?',

    text: 'Dữ liệu sẽ bị xóa!',

    icon: 'warning',

    showCancelButton: true,

    confirmButtonText: 'Xóa',

    cancelButtonText: 'Hủy',

    confirmButtonColor: '#dc3545',
  })
}
