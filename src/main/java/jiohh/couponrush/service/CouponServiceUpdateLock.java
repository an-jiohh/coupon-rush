package jiohh.couponrush.service;

import jiohh.couponrush.domain.Coupon;
import jiohh.couponrush.domain.CouponIssue;
import jiohh.couponrush.dto.IssueResult;
import jiohh.couponrush.exception.ContentionException;
import jiohh.couponrush.exception.SoldOutException;
import jiohh.couponrush.repository.CouponIssuesRepository;
import jiohh.couponrush.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class CouponServiceUpdateLock implements CouponService{

    private final CouponRepository couponRepository;
    private final CouponIssuesRepository couponIssuesRepository;

    private static final int MAX_RETRY = 3;

    @Transactional
    public IssueResult issueCoupon(String uid) {
        Optional<CouponIssue> existing = couponIssuesRepository.findByUid(uid);
        if (existing.isPresent()){
//            throw new CouponExistingException();
//            다른 서비스를 참고하면 재시도하면 받은 쿠폰이 보여줬던 것으로 생각됨
            CouponIssue result = existing.get();
            return IssueResult.builder()
                    .uid(result.getUid())
                    .couponName(result.getCoupon().getName())
                    .couponCode(result.getCoupon().getCode())
                    .issuedAt(result.getIssuedAt().toString())
                    .build();
        }

        for(int action = 0; action < MAX_RETRY; action++){
            List<Coupon> available = couponRepository.findAllAvailable();
            if (available.isEmpty()){
                throw new SoldOutException();
            }

            Coupon picked = pickCoupon(available);

            int updated = couponRepository.tryDecrement(picked.getCode());

            if (updated == 1){
                Instant now = Instant.now();
                CouponIssue created = CouponIssue.builder()
                        .issuedAt(now)
                        .coupon(picked)
                        .uid(uid)
                        .build();
                couponIssuesRepository.save(created);
                return IssueResult.builder()
                        .uid(uid)
                        .couponName(picked.getName())
                        .couponCode(picked.getCode())
                        .issuedAt(now.toString())
                        .build();
            }
        }
        throw new ContentionException();
    }

    private Coupon pickCoupon(List<Coupon> available) {
        int sum = 0;
        for(Coupon c: available) sum += c.getProbWeight();
        int check = ThreadLocalRandom.current().nextInt(1, sum + 1);
        int acc = 0;
        for(Coupon c: available) {
            acc += c.getProbWeight();
            if (check <= acc) return c;
        }
        throw new SoldOutException();
    }
}
