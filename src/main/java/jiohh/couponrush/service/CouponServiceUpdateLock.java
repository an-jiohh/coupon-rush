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

    /**
     * Issues a coupon for the given user ID using an update-lock style flow.
     *
     * <p>If a coupon has already been issued for the provided uid, returns that existing
     * issuance (idempotent behavior). Otherwise the method attempts up to {@code MAX_RETRY}
     * times to pick an available coupon (weighted by probability), decrement its stock,
     * persist a new CouponIssue, and return the issuance details.
     *
     * <p>Side effects: decrements coupon stock in the repository and saves a new
     * CouponIssue when issuance succeeds.
     *
     * @param uid the unique identifier of the user requesting a coupon
     * @return an IssueResult containing uid, coupon name, coupon code, and issuance timestamp
     * @throws SoldOutException if there are no available coupons at the time of checking
     * @throws ContentionException if issuance fails due to contention after {@code MAX_RETRY} attempts
     */
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

    /**
     * Selects a coupon from the provided list using weighted random selection based on each
     * coupon's `probWeight`.
     *
     * The probability of returning a particular coupon is its `probWeight` divided by the
     * sum of all coupons' `probWeight`. Uses ThreadLocalRandom to draw an integer in
     * [1, sum] and returns the first coupon whose accumulated weight meets or exceeds
     * that draw.
     *
     * @param available list of candidate coupons; expected to contain at least one coupon
     *                  with positive `probWeight`
     * @return a selected Coupon chosen proportionally to its `probWeight`
     * @throws SoldOutException if no coupon can be selected (e.g., empty list or all weights zero)
     */
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
