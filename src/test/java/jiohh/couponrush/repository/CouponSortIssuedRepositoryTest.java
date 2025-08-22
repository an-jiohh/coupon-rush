package jiohh.couponrush.repository;

import jiohh.couponrush.domain.Coupon;
import jiohh.couponrush.domain.CouponSortIssued;
import lombok.Data;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CouponSortIssuedRepositoryTest {

    @Autowired
    private CouponSortIssuedRepository couponSortIssuedRepository;
    @Autowired
    private CouponRepository couponRepository;

    @Test
    void 발급된_쿠폰개수_가져오기(){
        //given
        Coupon coupon = new Coupon("A", "테스트1", 2,30);
        couponRepository.save(coupon);
        couponSortIssuedRepository.save(CouponSortIssued.builder()
                        .coupon(coupon)
                        .issueOrder(1L)
                        .uid("testUser")
                .build());
        couponSortIssuedRepository.save(new CouponSortIssued(coupon,2L));
        couponSortIssuedRepository.save(new CouponSortIssued(coupon,3L));
        //when
        Long result = couponSortIssuedRepository.findMaxIssueOrder().orElse(0L);
        //then
        Assertions.assertThat(result).isEqualTo(1);
    }
}