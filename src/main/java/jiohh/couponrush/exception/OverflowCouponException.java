package jiohh.couponrush.exception;

public class OverflowCouponException extends RuntimeException {
    /**
     * Constructs a new OverflowCouponException with a predefined detail message.
     *
     * The exception's detail message is "쿠폰이 초과발급 되었습니다." (indicates that coupons were over-issued).
     */
    public OverflowCouponException() {
        super("쿠폰이 초과발급 되었습니다.");
    }
}
