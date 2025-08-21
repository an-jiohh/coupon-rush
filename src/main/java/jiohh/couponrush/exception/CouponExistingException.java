package jiohh.couponrush.exception;

public class CouponExistingException extends RuntimeException {
    public CouponExistingException() {
        super("이미 쿠폰이 발급된 유저입니다.");
    }
}
