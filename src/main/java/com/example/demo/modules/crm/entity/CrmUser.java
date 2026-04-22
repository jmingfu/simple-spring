package com.example.demo.modules.crm.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.demo.enums.RoleEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
* 用户表
* @TableName crm_user
*/
@TableName("crm_user")
@Data
public class CrmUser implements Serializable {

    /**
    * 主键id
    */
    @NotNull(message="[主键id]不能为空")
    @ApiModelProperty("主键id")
    private Long id;
    /**
    * 账号
    */
    @NotBlank(message="[账号]不能为空")
    @Size(max= 32,message="编码长度不能超过32")
    @ApiModelProperty("账号")
    @Length(max= 32,message="编码长度不能超过32")
    private String username;
    /**
    * 密码（MD5加密）
    */
    @NotBlank(message="[密码（MD5加密）]不能为空")
    @Size(max= 64,message="编码长度不能超过64")
    @ApiModelProperty("密码（MD5加密）")
    @Length(max= 64,message="编码长度不能超过64")
    private String password;
    /**
    * 0销售 1主管 2管理员
    */
    @ApiModelProperty("0销售 1主管 2管理员")
    private RoleEnum role;
    /**
    * 主管ID（销售专属，指向主管的用户ID）
    */
    @ApiModelProperty("主管ID（销售专属，指向主管的用户ID）")
    private Long managerId;
    /**
    * 真实姓名
    */
    @Size(max= 32,message="编码长度不能超过32")
    @ApiModelProperty("真实姓名")
    @Length(max= 32,message="编码长度不能超过32")
    private String name;
    /**
    * 
    */
    @ApiModelProperty("")
    private Date createTime;


}
