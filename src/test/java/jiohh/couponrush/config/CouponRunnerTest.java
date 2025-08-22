package jiohh.couponrush.config;

import jiohh.couponrush.domain.Coupon;
import jiohh.couponrush.repository.CouponRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class CouponRunnerTest {

    @Autowired
    private CouponRepository couponRepository;

    @Test
    void 초기입력값_확인 () {
        List<Coupon> all = couponRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(3);
    }
}