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

        /**
         * Required no-argument constructor for JPA and proxying.
         *
         * <p>Intended for use by JPA and frameworks that instantiate entities via reflection.
         */
        public Coupon() {
        }

        /**
         * Constructs a Coupon with the given code, name, initial stock, and probability weight.
         *
         * The remaining stock is initialized to the provided initialStock.
         *
         * @param code        unique coupon code (max length 32)
         * @param name        human-readable coupon name (max length 64)
         * @param initialStock initial quantity available for this coupon
         * @param probWeight  relative probability weight used for selection
         */
        public Coupon(String code, String name, int initialStock, int probWeight) {
                this.code = code;
                this.name = name;
                this.initialStock = initialStock;
                this.remainingStock = initialStock;
                this.probWeight = probWeight;
        }

        /**
         * JPA lifecycle callback invoked before the entity is first persisted.
         *
         * Sets both `createAt` and `updatedAt` to the current `Instant` so the entity
         * records its creation and initial modification time.
         */
        @PrePersist
        void onCreate() {
                Instant now = Instant.now();
                this.createAt = now;
                this.updatedAt = now;
        }

        /**
         * JPA lifecycle callback executed before the entity is updated; sets updatedAt to the current instant.
         */
        @PreUpdate
        void onUpdate(){
            this.updatedAt = Instant.now();
        }

        /**
         * Resets the coupon's remaining stock to its initial stock value.
         *
         * This updates the entity's mutable state by assigning {@code initialStock}
         * to {@code remainingStock}.
         */
        public void resetStock() {
                this.remainingStock = this.initialStock;
        }
}
