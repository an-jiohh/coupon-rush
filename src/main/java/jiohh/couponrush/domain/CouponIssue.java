package jiohh.couponrush.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Entity
@Table(
        name = "coupon_issues",
        indexes = {@Index(name = "coupon_uid_index", columnList = "uid", unique = true)}
)
@Getter
@Builder
public class CouponIssue {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 128, unique = true)
    private String uid;

    @ManyToOne(fetch = FetchType.EAGER) //같이 조회하는 경우가 많은 것으로 판단
    @JoinColumn(name = "coupon_code", nullable = false)
    private Coupon coupon;

    @Column(name = "issued_at", nullable = false)
    private Instant issuedAt;

    /**
     * JPA-required no-argument constructor for entity instantiation by frameworks.
     *
     * <p>Declared public for JPA and proxying libraries; application code should use the
     * builder or other constructors when creating instances with meaningful state.
     */
    public CouponIssue() {
    }

    /**
     * Creates a CouponIssue instance with all fields initialized.
     *
     * @param id        primary key value (may be null for transient/new entities)
     * @param uid       unique identifier for the issued coupon (non-null, max length 128)
     * @param coupon    associated Coupon entity (non-null)
     * @param issuedAt  timestamp when the coupon was issued (represents the business event time)
     */
    public CouponIssue(Long id, String uid, Coupon coupon, Instant issuedAt) {
        this.id = id;
        this.uid = uid;
        this.coupon = coupon;
        this.issuedAt = issuedAt;
    }

    /**
     * Creates a CouponIssue with the given id, uid, and issuance timestamp, omitting the associated Coupon.
     *
     * Useful when the coupon association is not available or not required (for projections, partial updates, or tests).
     *
     * @param id        the primary key (may be null for a new/transient entity)
     * @param uid       the unique coupon issuance identifier (max length 128, non-null in persisted entities)
     * @param issuedAt  the timestamp when the coupon was issued
     */
    public CouponIssue(Long id, String uid, Instant issuedAt) {
        this.id = id;
        this.uid = uid;
        this.issuedAt = issuedAt;
    }
    /*
    @PrePersist
    void onCreate() {
        this.issuedAt = Instant.now();
    }
    “쿠폰 발급”이라는 행위가 일어난 시점이기 때문에 @PrePersist 사용 X
    -> 단순 DB 저장 시점과는 다를 수 있음, Redis 큐를 사용시 뒤늦게 반영될 수 있음
    -> 로그와 시간 불일치 가능
    */
}
