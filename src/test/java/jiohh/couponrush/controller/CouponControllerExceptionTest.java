package jiohh.couponrush.controller;

import jiohh.couponrush.exception.ContentionException;
import jiohh.couponrush.exception.SoldOutException;
import jiohh.couponrush.service.CouponService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class CouponControllerExceptionTest {
    @Autowired
    MockMvc mvc;

    @MockitoBean
    CouponService couponService;

    @Test
    void 서비스에서_SoldOut_Excption_발생() throws Exception{
        given(couponService.issueCoupon(anyString())).willThrow(new SoldOutException());

        mvc.perform(post("/api/v1/coupons/u-1/issue"))
                .andExpect(status().isGone())
                .andExpect(jsonPath("$.code").value("SOLD_OUT"))
                .andExpect(jsonPath("$.message").value("모든 쿠폰이 소진되었습니다."));
    }

    @Test
    void 서비스에서_Contention_Excption_발생() throws Exception{
        given(couponService.issueCoupon(anyString())).willThrow(new ContentionException());

        mvc.perform(post("/api/v1/coupons/u-1/issue"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("CONTENTIOUS"))
                .andExpect(jsonPath("$.message").value("잠시 후 다시 시도해 주세요."));
    }

    @Test
    void 서비스에서_Excption_발생() throws Exception{
        given(couponService.issueCoupon(anyString())).willThrow(new RuntimeException());

        mvc.perform(post("/api/v1/coupons/u-1/issue"))
                .andExpect(status().isIAmATeapot())
                .andExpect(jsonPath("$.code").value("SERVER_ERROR"))
                .andExpect(jsonPath("$.message").value("잠시후 다시 시도해주세요."));
    }
}