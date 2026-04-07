package com.example.demo.modules.membership.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.config.WechatMiniConfig;
import com.example.demo.exception.ReturnException;
import com.example.demo.modules.admin.controller.AdminController;
import com.example.demo.modules.membership.dto.MemberDTO;
import com.example.demo.modules.membership.dto.WechatCode2SessionRes;
import com.example.demo.modules.membership.entity.Member;
import com.example.demo.modules.membership.mapper.MemberMapper;
import com.example.demo.modules.membership.service.MemberService;
import com.example.demo.modules.user.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.UUID;

/**
 * 基于SpringBoot框架的个人练手项目-
 *
 * @author JMF
 * @date 2026-04-05 16:24
 * @date 2026-04-05
 */
@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private WechatMiniConfig wechatMiniConfig;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    @Override
    public MemberDTO wxLogin(MemberDTO memberDTO) throws Exception {
//        String url = String.format("https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
//                wechatMiniConfig.getAppId(),
//                wechatMiniConfig.getAppSecret(),
//                memberDTO.getCode());
//        WechatCode2SessionRes res=restTemplate.getForObject(url,WechatCode2SessionRes.class);
//        if(res==null|| StringUtils.isNotBlank(res.getErrCode())){
//            log.error("小程序授权失败"+res.getErrMsg());
//        }
        String openId = memberDTO.getOpenid();
        if (StringUtils.isBlank(openId)) {
            throw new ReturnException("用户openId获取失败");
        }
        LambdaQueryWrapper<Member> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Member::getOpenid, memberDTO.getOpenid());
        Member member = memberMapper.selectOne(wrapper);
        String token = UUID.randomUUID().toString().replace("-", "");
        if (member != null) {
            BeanUtils.copyProperties(member, memberDTO);
        } else {
            Member newMember = new Member();
            newMember.setOpenid(openId);
            newMember.setNickname(memberDTO.getNickname());
            newMember.setPhone(memberDTO.getPhone());
            try {
                memberMapper.insert(newMember);
            } catch (Exception e) {
                log.error("插入失败，原因:" + e.getMessage());
            }
        }
        memberDTO.setToken(token);
        String s = stringRedisTemplate.opsForValue().get("LOGIN:token:latest" + member.getOpenid());
        if(StringUtils.isNotBlank(s)){
            MemberDTO oldMem = objectMapper.readValue(s,MemberDTO.class);
            String oldToken=oldMem.getToken();
            stringRedisTemplate.delete("LOGIN:"+oldToken);
        }
        stringRedisTemplate.opsForValue().set("LOGIN:token:latest" + member.getOpenid(), objectMapper.writeValueAsString(memberDTO));
        stringRedisTemplate.opsForValue().set("LOGIN:"+token,objectMapper.writeValueAsString(memberDTO));
        return memberDTO;
    }

    @Override
    public MemberDTO getById(Long id) {
        Member member = memberMapper.selectById(id);
        MemberDTO memberDTO = new MemberDTO();
        BeanUtils.copyProperties(member, memberDTO);
        return memberDTO;
    }

    @Override
    public IPage<MemberDTO> selectPage(MemberDTO dto) {
        Page<Member> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<Member> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(dto.getPhone()), Member::getPhone, dto.getPhone());
        wrapper.like(StringUtils.isNotEmpty(dto.getNickname()), Member::getNickname, dto.getNickname());
        //待定，是否有优惠券
        // TODO: 2026/4/6
        wrapper.between(dto.getBeginTime() != null && dto.getEndTime() != null
                , Member::getCreateTime, dto.getBeginTime(), dto.getEndTime());
        Page<Member> memberPage = memberMapper.selectPage(page, wrapper);
        IPage<MemberDTO> dtoPage = memberPage.convert(member -> {
            MemberDTO memberDTO = new MemberDTO();
            BeanUtils.copyProperties(member, memberDTO);
            return memberDTO;
        });
        return dtoPage;
    }
}
