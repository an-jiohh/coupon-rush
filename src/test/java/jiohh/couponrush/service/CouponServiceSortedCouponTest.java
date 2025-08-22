package jiohh.couponrush.service;

import jiohh.couponrush.dto.IssueResult;
import jiohh.couponrush.exception.SoldOutException;
import jiohh.couponrush.repository.CouponSortIssuedRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class CouponServiceSortedCouponTest {

    @Autowired
    private CouponServiceSortedCoupon couponServiceSortedCoupon;

    @Autowired
    private CouponSortIssuedRepository couponSortIssuedRepository;

    @Autowired
    private ResetUtil resetUtil;

    @BeforeEach
    void setup() {
        resetUtil.resetSortedCouponAll();
    }

    @Test
    void 사용자_정상_발급(){
        //given
        String uid = "u1";
        //when
        IssueResult result = couponServiceSortedCoupon.issueCoupon(uid);
        //then
        Assertions.assertThat(result.getUid()).isEqualTo(uid);
        Long count = couponSortIssuedRepository.countByUidIsNotNull();
        Assertions.assertThat(count).isEqualTo(1);
    }

    @Test
    void 중복_요청은_같은_결과_반환() {
        //given
        String uid = "u1";
        //when
        IssueResult result1 = couponServiceSortedCoupon.issueCoupon(uid);
        IssueResult result2 = couponServiceSortedCoupon.issueCoupon(uid);
        //then
        Assertions.assertThat(result1.getCouponCode()).isEqualTo(result2.getCouponCode());
        Assertions.assertThat(result1.getIssuedAt()).isEqualTo(result2.getIssuedAt());
    }

    @Test
    void 전체_품절이면_SoldOut() {
        //given
        for(int i = 0; i < 100; i++) couponServiceSortedCoupon.issueCoupon(String.valueOf(i));
        //when
        //then
        org.junit.jupiter.api.Assertions.assertThrows(SoldOutException.class,() -> couponServiceSortedCoupon.issueCoupon("101"));
    }

}