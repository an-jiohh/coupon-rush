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

    /**
     * Creates an empty ErrorResponseDto.
     *
     * <p>Constructs a new instance with null `code` and `message`. Intended for frameworks
     * that require a no-argument constructor (e.g., deserialization) or manual population.
     */
    public ErrorResponseDto() {
    }

    /**
     * Creates a new ErrorResponseDto with the specified error code and message.
     *
     * @param code    machine-readable error code or identifier
     * @param message human-readable description of the error
     */
    public ErrorResponseDto(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
