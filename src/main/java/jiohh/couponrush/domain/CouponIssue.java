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

    public CouponIssue() {
    }

    public CouponIssue(Long id, String uid, Coupon coupon, Instant issuedAt) {
        this.id = id;
        this.uid = uid;
        this.coupon = coupon;
        this.issuedAt = issuedAt;
    }

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
