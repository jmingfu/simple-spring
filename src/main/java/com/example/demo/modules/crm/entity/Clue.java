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
* 线索表
* @TableName clue
*/
@TableName("clue")
@Data
public class Clue implements Serializable {

    /**
    * 主键ID
    */
    @NotNull(message="[主键ID]不能为空")
    @ApiModelProperty("主键ID")
    private Long id;
    /**
    * 客户姓名/企业名称
    */
    @NotBlank(message="[客户姓名/企业名称]不能为空")
    @Size(max= 64,message="编码长度不能超过64")
    @ApiModelProperty("客户姓名/企业名称")
    @Length(max= 64,message="编码长度不能超过64")
    private String name;
    /**
    * 联系电话
    */
    @NotBlank(message="[联系电话]不能为空")
    @Size(max= 16,message="编码长度不能超过16")
    @ApiModelProperty("联系电话")
    @Length(max= 16,message="编码长度不能超过16")
    private String phone;
    /**
    * 公司名称
    */
    @Size(max= 128,message="编码长度不能超过128")
    @ApiModelProperty("公司名称")
    @Length(max= 128,message="编码长度不能超过128")
    private String company;
    /**
    * 状态: 0待分配 1跟进中 2已转化 3已流失
    */
    @ApiModelProperty("状态: 0待分配 1跟进中 2已转化 3已流失")
    private Integer status;
    /**
    * 负责人ID（销售ID）
    */
    @ApiModelProperty("负责人ID（销售ID）")
    private Long ownerId;
    /**
    * 最后跟进时间
    */
    @ApiModelProperty("最后跟进时间")
    private Date lastFollowTime;
    /**
    * 下次跟进时间
    */
    @ApiModelProperty("下次跟进时间")
    private Date nextFollowTime;
    /**
    * 跟进次数
    */
    @ApiModelProperty("跟进次数")
    private Integer followCount;
    /**
    * 创建时间
    */
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
    * 更新时间
    */
    @ApiModelProperty("更新时间")
    private Date updateTime;
    /**
    * 逻辑删除
    */
    @ApiModelProperty("逻辑删除")
    private Integer isDeleted;


}
