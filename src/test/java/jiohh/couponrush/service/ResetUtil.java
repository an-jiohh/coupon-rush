package jiohh.couponrush.service;

import jiohh.couponrush.domain.Coupon;
import jiohh.couponrush.repository.CouponIssuesRepository;
import jiohh.couponrush.repository.CouponRepository;
import jiohh.couponrush.repository.CouponSortIssuedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ResetUtil {
    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponIssuesRepository couponIssuesRepository;

    @Autowired
    private CouponSortIssuedRepository couponSortIssuedRepository;

    @Autowired
    private StringRedisTemplate redis;

    private final static String REDIS_COUNTER_KEY = "REDIS_COUNTER_KEY";

    @Transactional
    public void resetCouponAll() {
        couponIssuesRepository.deleteAllInBatch();
        List<Coupon> all = couponRepository.findAll();
        all.forEach(coupon -> coupon.resetStock());
        couponRepository.flush();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void resetSortedCouponAll() {
        couponSortIssuedRepository.resetIssue();
        redis.delete(REDIS_COUNTER_KEY);
        redis.opsForValue().set(REDIS_COUNTER_KEY, String.valueOf(0L));
    }
}
