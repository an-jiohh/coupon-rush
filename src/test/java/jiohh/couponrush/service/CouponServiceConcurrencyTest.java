package jiohh.couponrush.service;

import jiohh.couponrush.repository.CouponIssuesRepository;
import jiohh.couponrush.repository.CouponRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class CouponServiceConcurrencyTest {

    @Autowired
    CouponService couponService;
    @Autowired
    ResetUtil resetUtil;
    @Autowired
    CouponIssuesRepository couponIssuesRepository;
    @Autowired
    private CouponRepository couponRepository;

    @BeforeEach
    void setUp() {
        resetUtil.resetCouponAll();
    }

    @Test
    void 동시_200명_요청시_초과발급_테스트() throws Exception {
        int threads = 200;

        ExecutorService executorService = Executors.newFixedThreadPool(threads);

        CountDownLatch ready = new CountDownLatch(threads);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done  = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            String uid = "user-" + i;
            executorService.submit(() -> {
                try {
                    ready.countDown();
                    start.await();
                    couponService.issueCoupon(uid);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
        }

        ready.await(10, TimeUnit.SECONDS);
        start.countDown();
        done.await(60, TimeUnit.SECONDS);

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        // then
        // 쿠폰 발급 수
        Assertions.assertThat(couponIssuesRepository.count()).isEqualTo(100);
        // 잔여 재고 합
        int sum = couponRepository.findAll().stream().mapToInt(c -> c.getRemainingStock()).sum();
        Assertions.assertThat(sum).isEqualTo(0);
        // 각 코드별 발급 수량
        long aCount = couponIssuesRepository.findAll().stream().filter(c -> c.getCoupon().getCode().equals("A")).count();
        long bCount = couponIssuesRepository.findAll().stream().filter(c -> c.getCoupon().getCode().equals("B")).count();
        long cCount = couponIssuesRepository.findAll().stream().filter(c -> c.getCoupon().getCode().equals("C")).count();
        Assertions.assertThat(aCount).isEqualTo(1);
        Assertions.assertThat(bCount).isEqualTo(30);
        Assertions.assertThat(cCount).isEqualTo(69);
    }
}
