package jiohh.couponrush.service;

import jiohh.couponrush.domain.Coupon;
import jiohh.couponrush.dto.IssueResult;
import jiohh.couponrush.exception.SoldOutException;
import jiohh.couponrush.repository.CouponIssuesRepository;
import jiohh.couponrush.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@SpringBootTest
class CouponServiceTest {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private ResetUtil resetUtil;

    @Autowired
    private CouponService couponService;

    @BeforeEach
//    @Transactional -> 테스트에서 Transactional은 롤백되는 것을 까먹지 말것!
    void setup() {
        resetUtil.resetCouponAll();
    }

    @Test
    void 랜덤테스트 () {
        Random random = new Random(5); //시드가 같다면 동일, 시드 설정안하면 현재시간으로
        log.info("random : {} ",random.nextInt());
        log.info("random so easy : {}", ThreadLocalRandom.current().nextInt());
    }

    @Test
    void 사용자_정상_발급(){
        //given
        String uid = "u1";
        //when
        IssueResult result = couponService.issueCoupon(uid);
        //then
        Assertions.assertThat(result.getUid()).isEqualTo(uid);
        List<Coupon> all = couponRepository.findAll();
        int initialCount = all.stream().mapToInt(Coupon::getInitialStock).sum();
        int remainingCount = all.stream().mapToInt(Coupon::getRemainingStock).sum();
        Assertions.assertThat(initialCount - 1).isEqualTo(remainingCount);
    }

    @Test
    void 중복_요청은_같은_결과_반환() {
        //given
        String uid = "u1";
        //when
        IssueResult result1 = couponService.issueCoupon(uid);
        IssueResult result2 = couponService.issueCoupon(uid);
        //then
        Assertions.assertThat(result1.getCouponCode()).isEqualTo(result2.getCouponCode());
        Assertions.assertThat(result1.getIssuedAt()).isEqualTo(result2.getIssuedAt());
    }

    @Test
    void 전체_품절이면_SoldOut() {
        //given
        for(int i = 0; i < 100; i++) couponService.issueCoupon(String.valueOf(i));
        //when
        //then
        org.junit.jupiter.api.Assertions.assertThrows(SoldOutException.class,() -> couponService.issueCoupon("101"));
    }
}