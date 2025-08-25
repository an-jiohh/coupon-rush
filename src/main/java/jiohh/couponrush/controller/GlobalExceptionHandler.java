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
    /**
     * Handles DataIntegrityViolationException and returns HTTP 400 with an ErrorResponseDto
     * containing code "DATA_INTEGRITY_VIOLATION" and message "요청이 중복되었습니다.".
     *
     * @param ex the caught DataIntegrityViolationException
     * @return ResponseEntity containing the ErrorResponseDto with HTTP 400 (Bad Request)
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error(ex.getMessage());
        ErrorResponseDto response = ErrorResponseDto.builder()
                .code("DATA_INTEGRITY_VIOLATION")
                .message("요청이 중복되었습니다.")
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles MethodArgumentNotValidException thrown when controller method argument validation fails.
     *
     * Builds an ErrorResponseDto with code "BAD_REQUEST" and message "요청 형식이 올바르지 않습니다." and
     * returns it with HTTP 400 (Bad Request).
     *
     * @param ex the validation exception containing binding and validation errors
     * @return a ResponseEntity containing the ErrorResponseDto and HTTP 400 status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidation(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage());
        ErrorResponseDto response = ErrorResponseDto.builder()
                .code("BAD_REQUEST")
                .message("요청 형식이 올바르지 않습니다.")
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles any uncaught exception from controller methods and maps it to an error response.
     *
     * Returns an ErrorResponseDto with code "SERVER_ERROR" and message "잠시후 다시 시도해주세요." and
     * a HTTP 418 (I_AM_A_TEAPOT) status.
     *
     * @return ResponseEntity containing the ErrorResponseDto and HTTP 418 status
     */
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
