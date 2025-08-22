package jiohh.couponrush.controller;

import jiohh.couponrush.dto.ErrorResponseDto;
import jiohh.couponrush.exception.SoldOutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error(ex.getMessage());
        ErrorResponseDto response = ErrorResponseDto.builder()
                .code("DATA_INTEGRITY_VIOLATION")
                .message("요청이 중복되었습니다.")
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidation(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage());
        ErrorResponseDto response = ErrorResponseDto.builder()
                .code("BAD_REQUEST")
                .message("요청 형식이 올바르지 않습니다.")
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleOthers(Exception ex) {
        log.error(ex.getMessage());
        ErrorResponseDto response = ErrorResponseDto.builder()
                .code("SERVER_ERROR")
                .message("잠시후 다시 시도해주세요.")
                .build();
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(response);
    }
}
