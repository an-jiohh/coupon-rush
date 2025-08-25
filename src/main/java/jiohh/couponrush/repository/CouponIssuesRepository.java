package jiohh.couponrush.repository;

import jiohh.couponrush.domain.CouponIssue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponIssuesRepository extends JpaRepository<CouponIssue, Long> {
    /**
 * Finds a CouponIssue by its unique identifier.
 *
 * @param uid the unique identifier (uid) of the coupon issue
 * @return an Optional containing the matching CouponIssue, or empty if none is found
 */
Optional<CouponIssue> findByUid(String uid);
}
