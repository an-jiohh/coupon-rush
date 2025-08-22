package jiohh.couponrush.config;

import jiohh.couponrush.repository.CouponIssuesRepository;
import jiohh.couponrush.repository.CouponRepository;
import jiohh.couponrush.service.CouponService;
import jiohh.couponrush.service.CouponServiceUpdateLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public CouponService couponService(CouponRepository couponRepository, CouponIssuesRepository couponIssuesRepository) {
        return new CouponServiceUpdateLock(couponRepository, couponIssuesRepository);
    }
}
