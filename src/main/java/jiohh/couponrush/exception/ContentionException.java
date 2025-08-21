package jiohh.couponrush.exception;

public class ContentionException extends RuntimeException {
    public ContentionException() {
        super("현재 너무 많은 사람이 접속되어 있습니다.");
    }
}
