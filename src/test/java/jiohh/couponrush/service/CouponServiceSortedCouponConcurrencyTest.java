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
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
class CouponServiceSortedCouponConcurrencyTest {

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
                    couponServiceSortedCoupon.issueCoupon(uid);
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
        Assertions.assertThat(couponSortIssuedRepository.countByUidIsNotNull()).isEqualTo(100);
        // 각 코드별 발급 수량
//        long aCount = couponSortIssuedRepository.findAll().stream().filter(c -> c.getCoupon().getCode().equals("A")).count();
//        long bCount = couponSortIssuedRepository.findAll().stream().filter(c -> c.getCoupon().getCode().equals("B")).count();
//        long cCount = couponSortIssuedRepository.findAll().stream().filter(c -> c.getCoupon().getCode().equals("C")).count();
//        Assertions.assertThat(aCount).isEqualTo(1);
//        Assertions.assertThat(bCount).isEqualTo(30);
//        Assertions.assertThat(cCount).isEqualTo(69);
    }

    @Test
    void 동시_100명_요청시_오류_테스트() throws Exception {
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
                    couponServiceSortedCoupon.issueCoupon(uid);
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
        Assertions.assertThat(couponSortIssuedRepository.countByUidIsNotNull()).isEqualTo(100);
    }
}