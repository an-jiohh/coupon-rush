package jiohh.couponrush.repository;

import jiohh.couponrush.domain.CouponIssues;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponIssuesRepository extends JpaRepository<CouponIssues, Long> {
    Optional<CouponIssues> findByUid(String uid);
}
