package jiohh.couponrush.service;

import jiohh.couponrush.domain.Coupon;
import jiohh.couponrush.domain.CouponIssue;
import jiohh.couponrush.domain.CouponSortIssued;
import jiohh.couponrush.dto.IssueResult;
import jiohh.couponrush.exception.OverflowCouponException;
import jiohh.couponrush.exception.SoldOutException;
import jiohh.couponrush.repository.CouponIssuesRepository;
import jiohh.couponrush.repository.CouponSortIssuedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponServiceSortedCoupon implements CouponService {

    private final CouponSortIssuedRepository couponSortIssuedRepository;
    private final StringRedisTemplate redisCounter;

    private static final String REDIS_COUNTER_KEY = "REDIS_COUNTER_KEY";

    @Override
    @Transactional
    public IssueResult issueCoupon(String uid) {
        Optional<CouponSortIssued> existing = couponSortIssuedRepository.findByUid(uid);
        if(existing.isPresent()){
            CouponSortIssued result = existing.get();
            return IssueResult.builder()
                    .uid(result.getUid())
                    .couponName(result.getCoupon().getName())
                    .couponCode(result.getCoupon().getCode())
                    .issuedAt(result.getIssuedAt().toString())
                    .build();
        }
        Long increment = redisCounter.opsForValue().increment(REDIS_COUNTER_KEY);
        if (increment > 100) {
            throw new SoldOutException();
        }
        Optional<CouponSortIssued> orderResult = couponSortIssuedRepository.findByIssueOrder(increment);
        if (orderResult.isEmpty()) throw new OverflowCouponException();
        CouponSortIssued issueCoupon = orderResult.get();
        Instant now = Instant.now();
        CouponSortIssued purchaseCoupon = CouponSortIssued.builder()
                .id(issueCoupon.getId())
                .coupon(issueCoupon.getCoupon())
                .uid(uid)
                .issueOrder(issueCoupon.getIssueOrder())
                .issuedAt(now)
                .build();
        couponSortIssuedRepository.save(purchaseCoupon);

        return IssueResult.builder()
                .uid(uid)
                .couponName(issueCoupon.getCoupon().getName())
                .couponCode(issueCoupon.getCoupon().getCode())
                .issuedAt(now.toString())
                .build();
    }
}
