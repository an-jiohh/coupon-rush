package jiohh.couponrush.exception;

public class SoldOutException extends RuntimeException {
    public SoldOutException() {
        super("남은 쿠폰 재고가 없습니다.");
    }
}
