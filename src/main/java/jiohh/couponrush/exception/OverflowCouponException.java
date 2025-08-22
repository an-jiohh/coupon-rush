package jiohh.couponrush.exception;

public class OverflowCouponException extends RuntimeException {
    public OverflowCouponException() {
        super("쿠폰이 초과발급 되었습니다.");
    }
}
