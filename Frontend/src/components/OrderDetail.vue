<template>
  <div class="checkout-page bg-light-gray pb-5 pt-4">
    <div class="container narrow-container">
      
      <div class="checkout-stepper d-flex justify-content-center align-items-center gap-4 mb-5 fs-7 fw-bold text-uppercase">
        <div class="step-item text-muted">1. Giỏ hàng</div> <div class="step-divider"></div>
        <div class="step-item text-neon">2. Thanh toán</div> <div class="step-divider"></div>
        <div class="step-item text-muted">3. Hoàn tất</div> </div>

      <div class="row g-4">
        <div class="col-lg-7">
          <div class="card border-0 shadow-sm rounded-4 p-4 p-md-5">
            <h4 class="fw-black text-dark mb-4 text-uppercase">Thông tin nhận hàng</h4> <form>
              <div class="row g-3">
                <div class="col-12">
                  <label class="form-label fs-8 fw-bold text-muted text-uppercase">Họ và tên</label> <input type="text" class="form-control custom-input" placeholder="Nhập họ tên người nhận"> </div>

                <div class="col-md-6">
                  <label class="form-label fs-8 fw-bold text-muted text-uppercase">Số điện thoại</label> <input type="text" class="form-control custom-input" placeholder="0xxx xxx XXX"> </div>
                <div class="col-md-6">
                  <label class="form-label fs-8 fw-bold text-muted text-uppercase">Email (Tùy chọn)</label> <input type="email" class="form-control custom-input" placeholder="example@gmail.com"> </div>

                <div class="col-12">
                  <label class="form-label fs-8 fw-bold text-muted text-uppercase">Địa chỉ chi tiết</label> <input type="text" class="form-control custom-input" placeholder="Số nhà, tên đường, Phường/Xã..."> </div>

                <div class="col-12">
                  <label class="form-label fs-8 fw-bold text-muted text-uppercase">Ghi chú đơn hàng</label> <textarea class="form-control custom-input" rows="3" placeholder="Ghi chú về thời gian giao hàng, chỉ dẫn địa chỉ..."></textarea> </div>
              </div>
            </form>
          </div>
        </div>

        <div class="col-lg-5">
          
          <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
            <h5 class="fw-black text-uppercase mb-4">Đơn hàng của bạn (2)</h5> <div class="order-items-list d-flex flex-column gap-3 mb-4">
              <div class="d-flex align-items-center gap-3">
                <div class="bg-light rounded-3 p-1" style="width: 60px; height: 60px;">
                  <img src="https://images.unsplash.com/photo-1595225476474-87563907a212?w=100" class="img-fluid object-fit-contain" alt="Product">
                </div>
                <div class="flex-grow-1">
                  <h6 class="fw-bold fs-7 mb-0">Bàn phím cơ TechZone Pro K3</h6> <small class="text-muted fs-8">Số lượng: 1</small>
                </div>
                <div class="fw-bold fs-7">1.250.000₫</div> </div>
              
              <div class="d-flex align-items-center gap-3">
                <div class="bg-light rounded-3 p-1" style="width: 60px; height: 60px;">
                  <img src="https://images.unsplash.com/photo-1527814050087-379381547928?w=100" class="img-fluid object-fit-contain" alt="Product">
                </div>
                <div class="flex-grow-1">
                  <h6 class="fw-bold fs-7 mb-0">Chuột Gaming Wireless Phantom</h6> <small class="text-muted fs-8">Số lượng: 1</small> </div>
                <div class="fw-bold fs-7">890.000₫</div> </div>
            </div>

            <label class="form-label fs-8 fw-bold text-muted text-uppercase">Mã giảm giá</label> <div class="input-group mb-4">
              <input type="text" class="form-control custom-input fs-7" placeholder="Nhập mã của bạn"> <button class="btn btn-dark fw-bold px-3 fs-7 border-0">ÁP DỤNG</button> </div>

            <div class="d-flex flex-column gap-2 fs-7 mb-3">
              <div class="d-flex justify-content-between">
                <span class="text-muted">Tạm tính</span> <span class="fw-bold">2.140.000₫</span> </div>
              <div class="d-flex justify-content-between">
                <span class="text-muted">Phí vận chuyển</span> <span class="text-success fw-bold">Miễn phí</span> </div>
              <hr class="my-2 opacity-10">
              <div class="d-flex justify-content-between align-items-end">
                <span class="fw-black text-uppercase">Tổng cộng</span> <h4 class="text-neon fw-black mb-0">2.140.000₫</h4> </div>
            </div>
          </div>

          <div class="card border-0 shadow-sm rounded-4 p-4">
            <h5 class="fw-black text-uppercase mb-4">Phương thức thanh toán</h5> <div class="payment-methods d-flex flex-column gap-3">
              <div class="form-check custom-radio-payment border rounded-3 p-3 position-relative">
                <input class="form-check-input ms-0 me-3 shadow-none" type="radio" name="payment" id="cod" checked>
                <label class="form-check-label d-block cursor-pointer ps-4" for="cod">
                  <span class="fw-bold fs-7 d-block">Thanh toán khi nhận hàng (COD)</span> <small class="text-muted fs-8">Nhận hàng rồi mới thanh toán tiền mặt</small>
                </label>
              </div>

              <div class="form-check custom-radio-payment border rounded-3 p-3 position-relative">
                <input class="form-check-input ms-0 me-3 shadow-none" type="radio" name="payment" id="bank">
                <label class="form-check-label d-block cursor-pointer ps-4" for="bank">
                  <span class="fw-bold fs-7 d-block">Chuyển khoản ngân hàng</span> <small class="text-muted fs-8">Miễn phí phí chuyển tiền liên ngân hàng</small> </label>
              </div>

              <div class="form-check custom-radio-payment border rounded-3 p-3 position-relative">
                <input class="form-check-input ms-0 me-3 shadow-none" type="radio" name="payment" id="ewallet">
                <label class="form-check-label d-block cursor-pointer ps-4" for="ewallet">
                  <span class="fw-bold fs-7 d-block">Ví điện tử (MoMo/ZaloPay)</span> <small class="text-muted fs-8">Thanh toán nhanh chóng qua QR Code</small>
                </label>
              </div>
            </div>

            <button class="btn btn-neon w-100 fw-black py-3 rounded-3 mt-4 text-dark shadow-sm">
              XÁC NHẬN ĐẶT HÀNG </button>
            <router-link to="/cart" class="btn btn-link text-muted fw-bold fs-8 w-100 mt-2 text-decoration-none">
              <i class="bi bi-arrow-left me-1"></i> Quay lại giỏ hàng </router-link>
          </div>

        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
// Trang này tập trung vào giao diện, bạn có thể bổ sung logic thanh toán tại đây.
</script>

<style scoped>
.fw-black { font-weight: 900; }
.fs-7 { font-size: 0.85rem; }
.fs-8 { font-size: 0.75rem; }
.cursor-pointer { cursor: pointer; }

.bg-light-gray { background-color: #F8F9FA; }
.text-neon { color: #00FF33 !important; }
.bg-neon-light { background-color: rgba(0, 255, 51, 0.1); }
.btn-neon { background-color: #00FF33; color: #000; border: none; transition: all 0.2s; }
.btn-neon:hover { background-color: #00cc29; transform: translateY(-2px); }

/* Custom Container */
.narrow-container { max-width: 1000px !important; margin: 0 auto; }

/* Checkout Stepper */
.step-divider { width: 40px; height: 2px; background-color: #dee2e6; }

/* Custom Inputs */
.custom-input {
  background-color: #F9FAFB;
  border: 1px solid #EAEAEA;
  padding: 0.75rem 1rem;
  border-radius: 8px;
  font-size: 0.85rem;
}
.custom-input:focus {
  background-color: #FFF;
  border-color: #00FF33;
  box-shadow: none;
}

/* Custom Payment Radios */
.custom-radio-payment {
  transition: all 0.2s ease;
  cursor: pointer;
}
.custom-radio-payment:has(.form-check-input:checked) {
  border-color: #00FF33 !important;
  background-color: rgba(0, 255, 51, 0.05);
}
.custom-radio-payment .form-check-input:checked {
  background-color: #00FF33;
  border-color: #00FF33;
}
</style>