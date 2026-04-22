package com.example.demo.modules.crm.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
* 跟进记录表
* @TableName follow_record
*/
@TableName("follow_record")
@Data
public class FollowRecord implements Serializable {

    /**
    * 
    */
    @NotNull(message="[主键id]不能为空")
    @ApiModelProperty("主键id")
    private Long id;
    /**
    * 线索ID
    */
    @NotNull(message="[线索ID]不能为空")
    @ApiModelProperty("线索ID")
    private Long clueId;
    /**
    * 跟进内容
    */
    @NotBlank(message="[跟进内容]不能为空")
    @Size(max= 500,message="编码长度不能超过500")
    @ApiModelProperty("跟进内容")
    @Length(max= 500,message="编码长度不能超过500")
    private String content;
    /**
    * 下次跟进时间
    */
    @ApiModelProperty("下次跟进时间")
    private Date nextFollowTime;
    /**
    * 操作人ID（销售ID）
    */
    @NotNull(message="[操作人ID（销售ID）]不能为空")
    @ApiModelProperty("操作人ID（销售ID）")
    private Long operatorId;
    /**
    * 
    */
    @ApiModelProperty("")
    private Date createTime;



}
