package jiohh.couponrush.repository;

import jiohh.couponrush.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    //1. 재시도 로직(락 X)
    Optional<Coupon> findByCode(String code);

    @Query("SELECT c FROM Coupon c WHERE c.remainingStock > 0")
    List<Coupon> findAllAvailable();

    @Modifying(flushAutomatically = true, clearAutomatically = true) //flush는 이전값이 변경지않아 오버헤드없음, clear는 영속성 문제 발생할 수 있으므로 설정
    @Query("UPDATE Coupon c SET c.remainingStock = c.remainingStock - 1 WHERE c.code = :code and c.remainingStock > 0")
    int tryDecrement(@Param("code") String code);
}
