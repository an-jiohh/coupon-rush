package jiohh.couponrush.config;

import jiohh.couponrush.domain.Coupon;
import jiohh.couponrush.domain.CouponIssue;
import jiohh.couponrush.domain.CouponSortIssued;
import jiohh.couponrush.repository.CouponRepository;
import jiohh.couponrush.repository.CouponSortIssuedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CouponRunner implements ApplicationRunner {

    private final CouponRepository couponRepository;
    private final CouponSortIssuedRepository couponSortIssuedRepository;
    private final StringRedisTemplate redisTemplate;

    private final static String REDIS_COUNTER_KEY = "REDIS_COUNTER_KEY";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Coupon couponA = new Coupon("A", "m340i", 1, 1);
        Coupon couponB = new Coupon("B", "맥북 프로", 30, 10);
        Coupon couponC = new Coupon("C", "마이쮸", 69, 89);

        if(couponRepository.existsByCode("A")) return;

        if(!couponRepository.existsByCode("A")) {
            couponRepository.save(couponA);
        }
        if(!couponRepository.existsByCode("B")) {
            couponRepository.save(couponB);
        }
        if(!couponRepository.existsByCode(("C"))){
            couponRepository.save(couponC);
        }
        log.info("1. 초기 쿠폰 생성 성공");

        //2. 미리 쿠폰을 확률대로 발급하여 순번대로 배치
        List<Coupon> random = new ArrayList<>(100);
        random.add(couponA);
        for (int B = 0; B < 30; B++) random.add(couponB);
        for (int C = 0; C < 69; C++) random.add(couponC);
        Collections.shuffle(random);
        List<CouponSortIssued> initCoupon = new ArrayList<>(100);
        for (int order = 0; order <100; order++) {
            initCoupon.add(new CouponSortIssued(random.get(order), (long) order + 1));
        }
        couponSortIssuedRepository.saveAll(initCoupon);
        Long counted = couponSortIssuedRepository.findMaxIssueOrder().orElse(0L);
        redisTemplate.opsForValue().set(REDIS_COUNTER_KEY, String.valueOf(counted));
        log.info("2. 초기 쿠폰 생성 및 REDIS 초기화 성공");
    }
}
