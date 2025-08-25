package jiohh.couponrush.repository;

import jiohh.couponrush.domain.CouponSortIssued;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CouponSortIssuedRepository extends JpaRepository<CouponSortIssued, Long> {
    /**
 * Finds a CouponSortIssued by its unique identifier.
 *
 * @param uid the unique identifier to look up
 * @return an Optional containing the matching CouponSortIssued if found, otherwise Optional.empty()
 */
Optional<CouponSortIssued> findByUid(String uid);
    /**
 * Count CouponSortIssued entities that have a non-null `uid`.
 *
 * @return the number of CouponSortIssued rows where `uid` is not null
 */
Long countByUidIsNotNull();

    /**
     * Returns the maximum issueOrder value among CouponSortIssued rows that have a non-null uid.
     *
     * @return an {@link Optional} containing the maximum issueOrder, or {@link Optional#empty()} if no such rows exist
     */
    @Query("""
    SELECT max(c.issueOrder) FROM CouponSortIssued c WHERE c.uid IS NOT null
    """)
    Optional<Long> findMaxIssueOrder();

    /**
 * Finds a CouponSortIssued by its issueOrder value.
 *
 * @param order the issueOrder value to look up
 * @return an Optional containing the matching CouponSortIssued if one exists, otherwise Optional.empty()
 */
Optional<CouponSortIssued> findByIssueOrder(Long order);

    /**
     * Clears the `uid` field on all CouponSortIssued rows in the database.
     *
     * Performs a bulk update setting `uid = null` for every CouponSortIssued entity.
     * The operation flushes pending changes and clears the persistence context automatically.
     *
     * @return the number of rows updated
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE CouponSortIssued c SET c.uid = null")
    int resetIssue();
}
