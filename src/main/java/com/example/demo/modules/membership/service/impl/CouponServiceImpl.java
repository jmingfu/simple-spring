package com.example.demo.modules.membership.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.demo.common.RedisConstant;
import com.example.demo.exception.ReturnException;
import com.example.demo.modules.membership.dto.CouponDTO;
import com.example.demo.modules.membership.entity.CouponTemplate;
import com.example.demo.modules.membership.mapper.CouponMapper;
import com.example.demo.modules.membership.service.CouponService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 基于SpringBoot框架的个人练手项目-
 *
 * @author JMF
 * @date 2026-04-08 13:34
 * @date 2026-04-08
 */
@Service
public class CouponServiceImpl implements CouponService {
    @Autowired
    CouponMapper couponMapper;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    ObjectMapper objectMapper;

    @Override
    public CouponDTO addOrEdit(CouponDTO couponDTO) throws Exception {
        CouponTemplate coupon = new CouponTemplate();
        BeanUtils.copyProperties(couponDTO, coupon);
        if (couponDTO.getId() == null) {
            couponMapper.insert(coupon);
        } else {
            int rows = couponMapper.updateById(coupon);

            if (rows == 0) {
                throw new ReturnException("优惠券id不存在，更新失败");
            }
            //更新redis
            redisTemplate.opsForValue().set(RedisConstant.ALL_COUPON + coupon.getId(), objectMapper.writeValueAsString(couponDTO));
        }
        //更新redis优惠券id列表
        redisTemplate.opsForSet().add(RedisConstant.COUPON_IDS, String.valueOf(coupon.getId()));
        return couponDTO;
    }

    @Override
    public Boolean delete(Long id) {
        Boolean success = couponMapper.deleteById(id) == 0;
        //清除缓存
        if (success) {
            redisTemplate.opsForSet().remove(RedisConstant.COUPON_IDS, String.valueOf(id));
            redisTemplate.delete(RedisConstant.ALL_COUPON + id);
        }
        return success;
    }

    @Override
    public CouponDTO getCoupon(Long id) throws Exception {
        /*先做防穿透处理，由于线下门店，客户到店使用、查看优惠券对于一致性要求高，使用空值缓存会产生临界问题：当优惠券缓存过期前很短时间内，产生
        了一个空值缓存，这时就会导致已有的优惠券，但是小程序端提示优惠券不存在；而数据量少的情况下，不需要用布隆过滤器，因为维护麻烦.因此这里我使用
        RedisSet直接缓存已存在的优惠券id*/
        if (Objects.equals(redisTemplate.opsForSet().isMember(RedisConstant.COUPON_IDS, String.valueOf(id)), Boolean.FALSE)) {
            throw new ReturnException("优惠券不存在");
        }
        String stringCoupon = redisTemplate.opsForValue().get(RedisConstant.ALL_COUPON);
        if (StringUtils.isNotBlank(stringCoupon)) {
            return objectMapper.readValue(stringCoupon, CouponDTO.class);
        }
        CouponTemplate couponTemplate = couponMapper.selectById(id);
        CouponDTO couponDTO = new CouponDTO();
        BeanUtils.copyProperties(couponTemplate, couponDTO);
        //存入redis
        redisTemplate.opsForValue().set(RedisConstant.ALL_COUPON, objectMapper.writeValueAsString(couponDTO));
        return couponDTO;
    }
}
