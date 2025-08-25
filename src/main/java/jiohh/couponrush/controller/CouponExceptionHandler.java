package jiohh.couponrush.controller;

import jiohh.couponrush.dto.ErrorResponseDto;
import jiohh.couponrush.exception.ContentionException;
import jiohh.couponrush.exception.SoldOutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CouponExceptionHandler {

    /**
     * Handles SoldOutException by returning a standardized error response indicating coupons are exhausted.
     *
     * <p>Responds with HTTP 410 (Gone) and an ErrorResponseDto containing code "SOLD_OUT" and
     * message "모든 쿠폰이 소진되었습니다.".</p>
     *
     * @param ex the thrown SoldOutException (handled and not propagated)
     * @return ResponseEntity with status 410 Gone and the error payload
     */
    @ExceptionHandler(SoldOutException.class)
    public ResponseEntity<ErrorResponseDto> handleSoldOutException(SoldOutException ex) {
        log.info("쿠폰 소진");
        ErrorResponseDto response = ErrorResponseDto.builder()
                .code("SOLD_OUT")
                .message("모든 쿠폰이 소진되었습니다.")
                .build();
        return ResponseEntity.status(HttpStatus.GONE).body(response);
    }

    /**
     * Handles ContentionException and maps it to an HTTP 409 Conflict response.
     *
     * <p>Responds with an ErrorResponseDto having code "CONTENTIOUS" and message
     * "잠시 후 다시 시도해 주세요." to indicate a temporary contention that should be retried.
     *
     * @return ResponseEntity containing the ErrorResponseDto and HTTP status 409 (Conflict).
     */
    @ExceptionHandler(ContentionException.class)
    public ResponseEntity<ErrorResponseDto> handleContentionException(ContentionException ex) {
        log.error("경합 문제 발생 {}",ex.getMessage());
        ErrorResponseDto response = ErrorResponseDto.builder()
                .code("CONTENTIOUS")
                .message("잠시 후 다시 시도해 주세요.")
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
