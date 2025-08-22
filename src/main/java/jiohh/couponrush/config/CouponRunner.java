package jiohh.couponrush.config;

import jiohh.couponrush.domain.Coupon;
import jiohh.couponrush.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CouponRunner implements ApplicationRunner {

    private final CouponRepository couponRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(!couponRepository.existsByCode("A")) {
            couponRepository.save(new Coupon("A", "m340i", 1, 1));
        }
        if(!couponRepository.existsByCode("B")) {
            couponRepository.save(new Coupon("B", "맥북 프로", 30, 10));
        }
        if(!couponRepository.existsByCode(("C"))){
            couponRepository.save(new Coupon("C", "마이쮸", 69, 89));
        }
        log.info("초기 쿠폰 생성 성공");
    }
}
