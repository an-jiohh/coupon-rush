package jiohh.couponrush.service;

import jiohh.couponrush.domain.Coupon;
import jiohh.couponrush.repository.CouponIssuesRepository;
import jiohh.couponrush.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ResetUtil {
    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponIssuesRepository couponIssuesRepository;

    @Transactional
    public void resetCouponAll() {
        couponIssuesRepository.deleteAllInBatch();
        List<Coupon> all = couponRepository.findAll();
        all.forEach(coupon -> coupon.resetStock());
        couponRepository.flush();
    }
}
