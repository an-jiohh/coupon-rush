package jiohh.couponrush.service;

import jiohh.couponrush.dto.IssueResult;

public interface CouponService {
    /**
 * Attempts to issue a coupon for the given user identifier.
 *
 * @param uid the user identifier for whom to issue a coupon
 * @return an IssueResult representing the outcome of the issuance, including any associated data such as a coupon identifier or error information
 */
public IssueResult issueCoupon(String uid);
}
