package jiohh.couponrush.exception;

public class SoldOutException extends RuntimeException {
    /**
     * Constructs a SoldOutException with a default message indicating no remaining coupon stock.
     *
     * The exception is unchecked (extends RuntimeException) and is intended to signal that coupon inventory is depleted.
     */
    public SoldOutException() {
        super("남은 쿠폰 재고가 없습니다.");
    }
}
