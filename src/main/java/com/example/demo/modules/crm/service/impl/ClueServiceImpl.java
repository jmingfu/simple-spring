package com.example.demo.modules.crm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.demo.enums.RoleEnum;
import com.example.demo.exception.ReturnException;
import com.example.demo.modules.crm.dto.ClueDTO;
import com.example.demo.modules.crm.entity.Clue;
import com.example.demo.modules.crm.entity.CrmUser;
import com.example.demo.modules.crm.entity.FollowRecord;
import com.example.demo.modules.crm.mapper.ClueMapper;
import com.example.demo.modules.crm.mapper.CrmUserMapper;
import com.example.demo.modules.crm.mapper.FollowRecordMapper;
import com.example.demo.modules.crm.service.ClueService;
import com.example.demo.util.MemberUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 基于SpringBoot框架的个人练手项目-线索管理实现
 *
 * @author JMF
 * @date 2026-04-12 13:53
 * @date 2026-04-12
 */
@Service
@Repository
public class ClueServiceImpl implements ClueService {
    @Autowired
    ClueMapper clueMapper;
    @Autowired
    FollowRecordMapper followRecordMapper;
    @Autowired
    CrmUserMapper crmUserMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteClue(Long id) {
        CrmUser crmUser = MemberUtil.getCrmUser();
        if (!crmUser.getRole().equals(RoleEnum.ADMIN)) {
            throw new ReturnException("错误，无管理员权限");
        }
        clueMapper.deleteById(id);

        //删除跟进记录关联表
        LambdaQueryWrapper<FollowRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FollowRecord::getClueId,id);
        followRecordMapper.delete(wrapper);
        return true;
    }

    @Override
    public ClueDTO editClue(ClueDTO clueDTO) {
        CrmUser crmUser = MemberUtil.getCrmUser();
        //手动校验接口参数合法性
        checkClue(clueDTO);
        if(clueDTO.getId()==null){
            if(crmUser.getRole().equals(RoleEnum.ADMIN)){
                return clueDTO;
            }
            else{
                throw new ReturnException("无管理员权限");
            }
        }
        else {
            Clue clue = clueMapper.selectById(clueDTO.getId());
            if(crmUser.getRole().equals(RoleEnum.SALES)&&!Objects.equals(clue.getOwnerId(),crmUser.getId())){
                throw new ReturnException("禁止更改他人线索！");
            }
            if(crmUser.getRole().equals(RoleEnum.MANAGER)){
                LambdaQueryWrapper<CrmUser> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(CrmUser::getManagerId,crmUser.getId());
                List<CrmUser> staffUsers = crmUserMapper.selectList(wrapper);
                List<Long> staffIds = staffUsers.stream().map(CrmUser::getId).collect(Collectors.toList());
                if(staffUsers.isEmpty()||!staffIds.contains(clue.getOwnerId())){
                    throw new ReturnException("该线索归属于其他主管，无法更改。");
                }
            }
            clue.setCompany(clueDTO.getCompany());
            clue.setName(clueDTO.getName());
            clue.setPhone(clueDTO.getPhone());
            clueMapper.updateById(clue);
            ClueDTO dto = new ClueDTO();
            BeanUtils.copyProperties(clue,clueDTO);
            return dto;
        }

    }
    private void checkClue(ClueDTO clueDTO){
        if(Objects.isNull(clueDTO.getId())){
            if(StringUtils.isBlank(clueDTO.getCompany())||
                    (StringUtils.isNotBlank(clueDTO.getCompany())&&clueDTO.getCompany().length()>64)){
                throw new ReturnException("无效的客户/企业名称");
            }
            if(StringUtils.isBlank(clueDTO.getCompany())||
                    (StringUtils.isNotBlank(clueDTO.getCompany())
                            &&!clueDTO.getCompany().matches("^(1[3-9]\\d{9})|(0\\d{2,3}-?\\d{7,8}(-\\d{1,6})?)$"))){
                throw new ReturnException("无效的电话号码");
            }
            if(StringUtils.isNotBlank(clueDTO.getCompany())&&clueDTO.getCompany().length()>128){
                throw new ReturnException("公司名称长度过长");
            }
        }
        if(Objects.nonNull(clueDTO.getId())){
            if(StringUtils.isNotBlank(clueDTO.getCompany())&&clueDTO.getCompany().length()>64){
                throw new ReturnException("无效的客户/企业名称");
            }
            if(StringUtils.isNotBlank(clueDTO.getCompany())
                    &&!clueDTO.getCompany().matches("^(1[3-9]\\d{9})|(0\\d{2,3}-?\\d{7,8}(-\\d{1,6})?)$")){
                throw new ReturnException("无效的电话号码");
            }
            if(StringUtils.isNotBlank(clueDTO.getCompany())&&clueDTO.getCompany().length()>128){
                throw new ReturnException("公司名称长度过长");
            }
        }
    }





}
