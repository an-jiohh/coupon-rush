package jiohh.couponrush.exception;

public class ContentionException extends RuntimeException {
    /**
     * Constructs a new ContentionException with a fixed message indicating high concurrent access.
     *
     * <p>The exception message is: "현재 너무 많은 사람이 접속되어 있습니다."</p>
     */
    public ContentionException() {
        super("현재 너무 많은 사람이 접속되어 있습니다.");
    }
}
