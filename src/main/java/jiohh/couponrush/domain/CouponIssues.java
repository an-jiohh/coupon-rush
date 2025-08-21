package jiohh.couponrush.domain;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "coupon_issues",
        indexes = {@Index(name = "coupon_uid_index", columnList = "uid", unique = true)}
)
public class CouponIssues {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 128, unique = true)
    private String uid;

    @Column(name = "coupon_code", nullable = false, length = 32)
    private String couponCode;

    @Column(name = "issued_at", nullable = false)
    private Instant issuedAt;

    public CouponIssues() {
    }

    public CouponIssues(Long id, String uid, String couponCode, Instant issuedAt) {
        this.id = id;
        this.uid = uid;
        this.couponCode = couponCode;
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
