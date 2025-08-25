package jiohh.couponrush.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IssueResult {
    private String uid;
    private String couponName;
    private String couponCode;
    private String issuedAt;

    /**
     * Creates a new IssueResult with all fields initialized to null.
     *
     * This no-argument constructor is provided for frameworks and deserialization.
     */
    public IssueResult() {
    }

    /**
     * Constructs an IssueResult with the provided values.
     *
     * @param uid       unique identifier for the issuance (e.g., user or transaction id)
     * @param couponName human-readable name of the coupon
     * @param couponCode code assigned to the issued coupon
     * @param issuedAt  issuance timestamp as a string
     */
    public IssueResult(String uid, String couponName, String couponCode, String issuedAt) {
        this.uid = uid;
        this.couponName = couponName;
        this.couponCode = couponCode;
        this.issuedAt = issuedAt;
    }
}
