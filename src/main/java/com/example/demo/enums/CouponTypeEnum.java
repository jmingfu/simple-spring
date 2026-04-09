package com.example.demo.enums;

import io.swagger.annotations.Api;

/**
 * 基于SpringBoot框架的个人练手项目-优惠券类型
 *
 * @author JMF
 * @date 2026-04-08 12:59
 * @date 2026-04-08
 */
@Api(tags = "优惠券类型")
public enum CouponTypeEnum {
    FULL_REDUCTION(1,"满减"),
    DISCOUNT(2,"折扣"),
    NO_THRESHOLD(3,"无门槛");
    private final Integer code;
    private final String desc;
    CouponTypeEnum(Integer code,String desc){
        this.code=code;
        this.desc=desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static CouponTypeEnum getByCode(Integer code){
        for(CouponTypeEnum type:values()){
            if(type.code.equals(code)){
                return type;
            }
        }
        return null;
    }
}
