package jiohh.couponrush.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ErrorResponseDto {
    private String code;
    private String message;

    public ErrorResponseDto() {
    }

    public ErrorResponseDto(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
