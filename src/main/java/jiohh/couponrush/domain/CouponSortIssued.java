package jiohh.couponrush.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Entity
@Table(
        name = "coupon_sort_issued",
        indexes = {
                @Index(name = "sorted_coupon_order_index", columnList = "issue_order"),
                @Index(name = "sorted_coupon_uid_index", columnList = "uid")
        }
)
@Getter
@Builder
public class CouponSortIssued {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, length = 128, unique = true)
    private String uid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_code", nullable = false)
    private Coupon coupon;

    @Column(name = "issued_at", nullable = true)
    private Instant issuedAt;

    @Column(name = "issue_order", nullable = false)
    private Long issueOrder;

    /**
     * No-argument constructor required by JPA and other persistence frameworks.
     *
     * Creates an empty CouponSortIssued instance. Prefer the builder or the parameterized constructors for application code.
     */
    public CouponSortIssued() {
    }

    /**
     * Creates a new CouponSortIssued with the given coupon and ordering.
     *
     * @param coupon the associated Coupon (required; corresponds to the non-null foreign key)
     * @param issueOrder the non-null ordering value used for sorting/sequencing issued coupons
     */
    public CouponSortIssued(Coupon coupon, Long issueOrder) {
        this.coupon = coupon;
        this.issueOrder = issueOrder;
    }

    /**
     * Full-value constructor for creating a CouponSortIssued instance with all mapped fields.
     *
     * @param id         primary key value (may be null for new/transient instances)
     * @param uid        optional unique identifier (nullable, max length 128 as persisted)
     * @param coupon     associated Coupon (non-null; mapped as a many-to-one relation)
     * @param issuedAt   optional issuance timestamp (nullable)
     * @param issueOrder non-null ordering/sequencing value used for sorting
     */
    public CouponSortIssued(Long id, String uid, Coupon coupon, Instant issuedAt,Long issueOrder) {
        this.issuedAt = issuedAt;
        this.id = id;
        this.uid = uid;
        this.coupon = coupon;
        this.issueOrder = issueOrder;
    }
}
