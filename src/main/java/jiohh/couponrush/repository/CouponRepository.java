package jiohh.couponrush.repository;

import jiohh.couponrush.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    /**
 * Checks whether a Coupon with the given code exists.
 *
 * @param code the coupon code to look up
 * @return true if a Coupon with the specified code exists, false otherwise
 */
Boolean existsByCode(String code);
    /**
 * Finds a coupon by its code.
 *
 * This lookup returns an Optional containing the Coupon if found, or an empty Optional if not.
 * Intended for retry scenarios that do not acquire database locks (non-locking retry semantics).
 *
 * @param code the coupon code to search for
 * @return an Optional with the matching Coupon if present, otherwise an empty Optional
 */
    Optional<Coupon> findByCode(String code);

    /**
     * Retrieves all coupons that currently have remaining stock (> 0).
     *
     * <p>Returns all Coupon entities whose {@code remainingStock} is greater than zero.
     *
     * @return a list of coupons that are available (remainingStock &gt; 0); empty if none found
     */
    @Query("SELECT c FROM Coupon c WHERE c.remainingStock > 0")
    List<Coupon> findAllAvailable();

    /**
     * Attempts to decrement the remaining stock for the coupon identified by the given code.
     *
     * Executes a conditional JPQL update that subtracts 1 from `remainingStock` only if the coupon exists
     * and its `remainingStock` is greater than 0. Returns the number of rows updated (typically 1 on success,
     * 0 if the code was not found or stock was already 0).
     *
     * @param code coupon code to decrement stock for
     * @return number of rows updated (1 if stock was decremented, 0 otherwise)
     */
    @Modifying(flushAutomatically = true, clearAutomatically = true) //flush는 이전값이 변경지않아 오버헤드없음, clear는 영속성 문제 발생할 수 있으므로 설정
    @Query("UPDATE Coupon c SET c.remainingStock = c.remainingStock - 1 WHERE c.code = :code and c.remainingStock > 0")
    int tryDecrement(@Param("code") String code);
}
