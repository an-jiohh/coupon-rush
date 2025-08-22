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

    public CouponSortIssued() {
    }

    public CouponSortIssued(Coupon coupon, Long issueOrder) {
        this.coupon = coupon;
        this.issueOrder = issueOrder;
    }

    public CouponSortIssued(Long id, String uid, Coupon coupon, Instant issuedAt,Long issueOrder) {
        this.issuedAt = issuedAt;
        this.id = id;
        this.uid = uid;
        this.coupon = coupon;
        this.issueOrder = issueOrder;
    }
}
