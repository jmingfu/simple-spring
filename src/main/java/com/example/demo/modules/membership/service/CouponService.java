package com.example.demo.modules.membership.service;

import com.example.demo.modules.membership.dto.CouponDTO;

/**
 * 基于SpringBoot框架的个人练手项目-
 *
 * @author JMF
 * @date 2026-04-08 13:33
 * @date 2026-04-08
 */
public interface CouponService {
    CouponDTO addOrEdit(CouponDTO couponDTO) throws Exception;
    Boolean delete(Long id);
    CouponDTO getCoupon(Long id) throws Exception;
}
