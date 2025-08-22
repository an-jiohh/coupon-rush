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

    @PostMapping("/{uid}/issue")
    public ResponseEntity<IssueResult> issue(@PathVariable String uid) {
        IssueResult result = couponService.issueCoupon(uid);
        return ResponseEntity.ok(result);
    }
}
