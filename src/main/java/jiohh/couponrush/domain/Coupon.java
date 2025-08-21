package jiohh.couponrush.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;

@Entity
@Table(
        indexes = {@Index(name = "coupon_code_index", columnList = "code", unique = true)}
)
@Getter
public class Coupon {

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, length = 32, unique = true)
        private String code;

        @Column(nullable = false, length = 64)
        private String name;

        @Column(name = "initial_stock", nullable = false)
        private int initialStock;

        @Column(name = "remaining_stock", nullable = false)
        private int remainingStock;

        @Column(name = "porb_weight", nullable = false)
        private int probWeight;

        @Column(name= "create_at", nullable = false, updatable = false)
        private Instant createAt;

        @Column(name = "updated_at", nullable = false)
        private Instant updatedAt;

        public Coupon() {
        }

        public Coupon(String code, String name, int initialStock, int probWeight) {
                this.code = code;
                this.name = name;
                this.initialStock = initialStock;
                this.remainingStock = initialStock;
                this.probWeight = probWeight;
        }

        @PrePersist
        void onCreate() {
                Instant now = Instant.now();
                this.createAt = now;
                this.updatedAt = now;
        }

        @PreUpdate
        void onUpdate(){
            this.updatedAt = Instant.now();
        }
}
