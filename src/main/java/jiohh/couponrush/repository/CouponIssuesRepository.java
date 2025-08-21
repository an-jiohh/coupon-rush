package jiohh.couponrush.repository;

import jiohh.couponrush.domain.CouponIssue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponIssuesRepository extends JpaRepository<CouponIssue, Long> {
    Optional<CouponIssue> findByUid(String uid);
}
