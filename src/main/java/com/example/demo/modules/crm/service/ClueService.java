package com.example.demo.modules.crm.service;

import com.example.demo.modules.crm.dto.ClueDTO;

/**
 * 基于SpringBoot框架的个人练手项目-线索服务类
 *
 * @author JMF
 * @date 2026-04-12 13:43
 * @date 2026-04-12
 */

public interface ClueService {
    Boolean deleteClue(Long id);
    ClueDTO editClue(ClueDTO clueDTO);
}
