package jiohh.couponrush.controller;

import jiohh.couponrush.dto.IssueResult;
import jiohh.couponrush.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    /**
     * Issues a coupon for the specified user and returns the issuance result.
     *
     * Delegates to the CouponService to perform issuance and returns the resulting
     * IssueResult wrapped in an HTTP 200 (OK) response.
     *
     * @param uid the user identifier extracted from the path variable for whom the coupon is issued
     * @return a ResponseEntity containing the IssueResult and an HTTP 200 status
     */
    @PostMapping("/{uid}/issue")
    public ResponseEntity<IssueResult> issue(@PathVariable String uid) {
        IssueResult result = couponService.issueCoupon(uid);
        return ResponseEntity.ok(result);
    }
}
