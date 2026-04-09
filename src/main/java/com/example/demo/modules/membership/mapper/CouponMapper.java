package com.example.demo.modules.membership.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.modules.membership.entity.CouponTemplate;
import com.example.demo.modules.membership.entity.Member;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 基于SpringBoot框架的个人练手项目-
 *
 * @author JMF
 * @date 2026-04-05 17:08
 * @date 2026-04-05
 */
//@Api(tags = "会员mapper类")
@Repository
@Mapper
public interface CouponMapper extends BaseMapper<CouponTemplate> {

}
