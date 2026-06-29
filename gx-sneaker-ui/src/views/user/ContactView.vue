<template>
  <div class="contact-page animate-fade-in">
    <!-- Header Banner -->
    <div class="contact-hero">
      <div class="contact-hero-overlay"></div>
      <div class="contact-hero-content">
        <h1>LIÊN HỆ VỚI CHÚNG TÔI</h1>
        <p>GX Sneaker luôn sẵn sàng hỗ trợ và lắng nghe ý kiến từ bạn</p>
      </div>
    </div>

    <!-- Main Content Container -->
    <div class="contact-container">
      <!-- Info Column -->
      <div class="contact-info-card">
        <h2>Thông Tin Liên Hệ</h2>
        <p class="contact-subtitle">
          Mọi thắc mắc về sản phẩm, đơn hàng hoặc chính sách hợp tác, xin vui lòng liên hệ với chúng tôi qua các kênh dưới đây:
        </p>

        <div class="info-list">
          <div class="info-item">
            <div class="info-icon">
              <i class="fa-solid fa-location-dot"></i>
            </div>
            <div class="info-text">
              <h3>Địa chỉ cửa hàng</h3>
              <p>Hà Nội, Việt Nam</p>
            </div>
          </div>

          <div class="info-item">
            <div class="info-icon">
              <i class="fa-solid fa-phone"></i>
            </div>
            <div class="info-text">
              <h3>Số điện thoại</h3>
              <p><a href="tel:0123456789">0123 456 789</a></p>
            </div>
          </div>

          <div class="info-item">
            <div class="info-icon">
              <i class="fa-solid fa-envelope"></i>
            </div>
            <div class="info-text">
              <h3>Email</h3>
              <p><a href="mailto:gxsneaker@gmail.com">gxsneaker@gmail.com</a></p>
            </div>
          </div>

          <div class="info-item">
            <div class="info-icon">
              <i class="fa-solid fa-clock"></i>
            </div>
            <div class="info-text">
              <h3>Giờ làm việc</h3>
              <p>Thứ 2 - Chủ Nhật: 08:30 - 22:00</p>
            </div>
          </div>
        </div>

        <!-- Social Icons -->
        <div class="social-section">
          <h3>Kết nối với GX Sneaker</h3>
          <div class="social-icons">
            <a href="https://facebook.com" target="_blank" aria-label="Facebook">
              <i class="fab fa-facebook-f"></i>
            </a>
            <a href="https://tiktok.com" target="_blank" aria-label="TikTok">
              <i class="fab fa-tiktok"></i>
            </a>
            <a href="https://instagram.com" target="_blank" aria-label="Instagram">
              <i class="fab fa-instagram"></i>
            </a>
            <a href="#" aria-label="Chat">
              <i class="fa-solid fa-comment"></i>
            </a>
          </div>
        </div>
      </div>

      <!-- Map Column -->
      <div class="contact-map-card">
        <div class="map-wrapper">
          <iframe 
            src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3724.0392336338575!2d105.84813247596956!3d21.031110980617066!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x3135abbe30000001%3A0xed402db3b72c67f4!2zSOG7kyBIb8OgbiBLaeG6vG0!5e0!3m2!1svi!2s!4v1700000000000!5m2!1svi!2s" 
            width="100%" 
            height="100%" 
            style="border:0;" 
            allowfullscreen="" 
            loading="lazy" 
            referrerpolicy="no-referrer-when-downgrade"
            title="Bản đồ cửa hàng GX Sneaker"
          ></iframe>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
// No reactive states needed for display only page
</script>

<style scoped>
/* Contact Page Styles */
.contact-page {
  max-width: 1200px;
  margin: 40px auto;
  padding: 0 20px;
  font-family: 'Inter', sans-serif;
}

/* Animations */
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(15px); }
  to { opacity: 1; transform: translateY(0); }
}

.animate-fade-in {
  animation: fadeIn 0.6s ease forwards;
}

/* Hero Banner */
.contact-hero {
  position: relative;
  border-radius: 16px;
  overflow: hidden;
  height: 250px;
  background-image: linear-gradient(135deg, #111 0%, #222 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  color: white;
  margin-bottom: 40px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.contact-hero-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle, rgba(230, 0, 35, 0.15) 0%, rgba(0,0,0,0) 70%);
}

.contact-hero-content {
  position: relative;
  z-index: 2;
  padding: 0 20px;
}

.contact-hero-content h1 {
  font-size: 38px;
  font-weight: 800;
  letter-spacing: 1px;
  margin-bottom: 12px;
  text-shadow: 0 2px 4px rgba(0,0,0,0.3);
}

.contact-hero-content p {
  font-size: 16px;
  color: #ccc;
  font-weight: 400;
}

/* Responsive Container Grid */
.contact-container {
  display: grid;
  grid-template-columns: 1.1fr 1.3fr;
  gap: 40px;
  margin-bottom: 60px;
}

@media (max-width: 992px) {
  .contact-container {
    grid-template-columns: 1fr;
    gap: 30px;
  }
  .contact-hero {
    height: 200px;
  }
  .contact-hero-content h1 {
    font-size: 30px;
  }
}

/* Info Card */
.contact-info-card {
  background: #ffffff;
  border-radius: 16px;
  padding: 40px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05);
  border: 1px solid #f1f5f9;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.contact-info-card h2 {
  font-size: 26px;
  font-weight: 700;
  color: #1e293b;
  margin-bottom: 16px;
  position: relative;
  padding-bottom: 10px;
}

.contact-info-card h2::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 50px;
  height: 4px;
  background: #e60023;
  border-radius: 2px;
}

.contact-subtitle {
  color: #64748b;
  line-height: 1.6;
  font-size: 14.5px;
  margin-bottom: 30px;
}

.info-list {
  display: flex;
  flex-direction: column;
  gap: 24px;
  margin-bottom: 35px;
}

.info-item {
  display: flex;
  align-items: flex-start;
  gap: 16px;
}

.info-icon {
  background: #fee2e2;
  color: #e60023;
  width: 44px;
  height: 44px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  flex-shrink: 0;
  transition: all 0.3s ease;
}

.info-item:hover .info-icon {
  transform: translateY(-3px);
  background: #e60023;
  color: white;
}

.info-text h3 {
  font-size: 16px;
  font-weight: 600;
  color: #334155;
  margin-bottom: 4px;
}

.info-text p {
  color: #64748b;
  font-size: 14.5px;
  line-height: 1.5;
}

.info-text p a {
  color: #e60023;
  text-decoration: none;
  font-weight: 500;
  transition: text-decoration 0.2s;
}

.info-text p a:hover {
  text-decoration: underline;
}

/* Social Media Section */
.social-section h3 {
  font-size: 16px;
  font-weight: 600;
  color: #334155;
  margin-bottom: 15px;
}

.social-icons {
  display: flex;
  gap: 12px;
}

.social-icons a {
  width: 40px;
  height: 40px;
  background: #f1f5f9;
  color: #475569;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 50%;
  text-decoration: none;
  transition: all 0.3s ease;
  font-size: 16px;
}

.social-icons a:hover {
  background: #e60023;
  color: white;
  transform: scale(1.1);
}

/* Map Card */
.contact-map-card {
  background: #ffffff;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05);
  border: 1px solid #f1f5f9;
  padding: 12px;
  height: 500px;
}

.map-wrapper {
  width: 100%;
  height: 100%;
  border-radius: 12px;
  overflow: hidden;
}

@media (max-width: 992px) {
  .contact-map-card {
    height: 400px;
  }
}
</style>
