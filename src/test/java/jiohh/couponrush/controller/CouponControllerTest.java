package jiohh.couponrush.controller;

import jiohh.couponrush.service.ResetUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ResetUtil resetUtil;

    @BeforeEach
    void setUp() {resetUtil.resetCouponAll();}

    @Test
    void 발급_성공() throws Exception{
        mockMvc.perform(post("/api/v1/coupons/u-1/issue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uid").value("u-1"))
                .andExpect(jsonPath("$.couponCode").exists());
    }

    @Test
    void 중복_요청시_같은_결과() throws Exception{
        mockMvc.perform(post("/api/v1/coupons/u-1/issue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uid").value("u-1"))
                .andExpect(jsonPath("$.couponCode").exists());

        mockMvc.perform(post("/api/v1/coupons/u-1/issue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uid").value("u-1"))
                .andExpect(jsonPath("$.couponCode").exists());
    }
}