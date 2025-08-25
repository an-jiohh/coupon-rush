package jiohh.couponrush.exception;

public class CouponExistingException extends RuntimeException {
    /**
     * Constructs a CouponExistingException with a default message indicating the user has already been issued a coupon.
     *
     * The exception is unchecked (extends RuntimeException) and is intended to be thrown when attempting to issue a coupon to a user who already has one.
     */
    public CouponExistingException() {
        super("이미 쿠폰이 발급된 유저입니다.");
    }
}
