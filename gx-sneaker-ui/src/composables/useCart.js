import { ref } from 'vue'
import { getCartByKhachHangId } from '@/services/gioHangService'

const cartCount = ref(0)

export function useCart() {
  const fetchCartCount = async (userId) => {
    if (!userId) {
      cartCount.value = 0
      return
    }
    try {
      const res = await getCartByKhachHangId(userId)
      cartCount.value = res.data?.tongSoLuong || 0
    } catch (err) {
      console.error('Lỗi khi tải số lượng giỏ hàng:', err)
      cartCount.value = 0
    }
  }

  return {
    cartCount,
    fetchCartCount,
  }
}
