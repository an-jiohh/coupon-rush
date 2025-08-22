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

    @ExceptionHandler(SoldOutException.class)
    public ResponseEntity<ErrorResponseDto> handleSoldOutException(SoldOutException ex) {
        log.info("쿠폰 소진");
        ErrorResponseDto response = ErrorResponseDto.builder()
                .code("SOLD_OUT")
                .message("모든 쿠폰이 소진되었습니다.")
                .build();
        return ResponseEntity.status(HttpStatus.GONE).body(response);
    }

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
