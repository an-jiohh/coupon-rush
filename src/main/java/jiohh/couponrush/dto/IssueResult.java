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

    public IssueResult() {
    }

    public IssueResult(String uid, String couponName, String couponCode, String issuedAt) {
        this.uid = uid;
        this.couponName = couponName;
        this.couponCode = couponCode;
        this.issuedAt = issuedAt;
    }
}
