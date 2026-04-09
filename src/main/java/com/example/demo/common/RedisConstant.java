package com.example.demo.common;

/**
 * 基于SpringBoot框架的个人练手项目-redis常量
 *
 * @author JMF
 * @date 2026-04-08 15:44
 * @date 2026-04-08
 */
public class RedisConstant {
    // 所有优惠券
    public static final String ALL_COUPON = "coupon:all";

    // 已存在优惠券列表
    public static final String COUPON_IDS = "coupon:ids";

    // 优惠券一小时内访问次数
    public static final String COUPON_COUNTS = "coupon:counts";
}
