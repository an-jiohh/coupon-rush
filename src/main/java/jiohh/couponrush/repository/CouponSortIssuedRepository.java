package jiohh.couponrush.repository;

import jiohh.couponrush.domain.CouponSortIssued;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CouponSortIssuedRepository extends JpaRepository<CouponSortIssued, Long> {
    Optional<CouponSortIssued> findByUid(String uid);
    Long countByUidIsNotNull();

    @Query("""
    SELECT max(c.issueOrder) FROM CouponSortIssued c WHERE c.uid IS NOT null
    """)
    Optional<Long> findMaxIssueOrder();

    Optional<CouponSortIssued> findByIssueOrder(Long order);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE CouponSortIssued c SET c.uid = null")
    int resetIssue();
}
