package com.example.demo.modules.membership.service;

import com.example.demo.modules.membership.dto.MemberDTO;
import org.springframework.stereotype.Service;

/**
 * 基于SpringBoot框架的个人练手项目-
 *
 * @author JMF
 * @date 2026-04-05 16:22
 * @date 2026-04-05
 */

public interface MemberService {
    MemberDTO wxLogin(MemberDTO memberDTO);
}
