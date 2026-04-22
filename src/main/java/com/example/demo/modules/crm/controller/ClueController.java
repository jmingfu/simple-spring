package com.example.demo.modules.crm.controller;

import com.example.demo.common.Result;
import com.example.demo.modules.crm.dto.ClueDTO;
import com.example.demo.modules.crm.entity.Clue;
import com.example.demo.modules.crm.service.ClueService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 基于SpringBoot框架的个人练手项目-crm系统，线索管理器
 *
 * @author JMF
 * @date 2026-04-12 13:39
 * @date 2026-04-12
 */
@Api(tags = "crm系统，线索管理器")
@RestController
@RequestMapping("/api/v1/clue")
public class ClueController {
    @Autowired
    ClueService clueService;
    @DeleteMapping()
    public Result<Boolean> deleteClue(@RequestParam Long clueId){
        return Result.success(clueService.deleteClue(clueId));
    }

    @PostMapping()
    public Result<ClueDTO> editClue(@RequestBody ClueDTO clueDTO){
        return Result.success(clueService.editClue(clueDTO));
    }
}
