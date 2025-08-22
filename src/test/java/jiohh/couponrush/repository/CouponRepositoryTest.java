package jiohh.couponrush.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jiohh.couponrush.domain.Coupon;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
class CouponRepositoryTest {
    @Autowired
    private CouponRepository couponRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    void 조건부_탐색_테스트(){
        //given
        Coupon coupon1 = new Coupon("A", "테스트1", 2,30);
        Coupon coupon2 = new Coupon("B", "테스트1", 2,30);
        Coupon coupon3 = new Coupon("C", "테스트1", 0,30);
        couponRepository.save(coupon1);
        couponRepository.save(coupon2);
        couponRepository.save(coupon3);

        //when
        List<Coupon> allAvailable = couponRepository.findAllAvailable();

        //then
        Assertions.assertThat(allAvailable.size()).isEqualTo(2);
    }

    @Test
    void 감소_성공_테스트(){
        //given
        Coupon coupon = new Coupon("X", "테스트", 2, 30);
        couponRepository.save(coupon);
        //when
        int result = couponRepository.tryDecrement("X");

//        em.clear(); // JQPL사용으로 벌크연산 때문에 영속성 컨텍스트가 초기화 되지 않음

        //then
        Assertions.assertThat(result).isEqualTo(1);
        Coupon resultCoupon = couponRepository.findByCode("X").orElseThrow();
        Assertions.assertThat(resultCoupon.getRemainingStock()).isEqualTo(1);
    }

    @Test
    void 감소_실패_테스트(){
        //given
        Coupon coupon = new Coupon("X", "테스트", 0, 30);
        couponRepository.save(coupon);
        //when
        int result = couponRepository.tryDecrement("X");

        //then
        Assertions.assertThat(result).isEqualTo(0);
    }
}