package com.example.demo.modules.user.entity;


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
* 
* @TableName quality_detail
*/
@TableName("quality_detail")
@Data
public class QualityDetail implements Serializable {

    /**
    * 
    */
    @NotNull(message="[]不能为空")
    @ApiModelProperty("")
    private Long id;
    /**
    * 
    */
    @Size(max= 32,message="编码长度不能超过32")
    @ApiModelProperty("")
    @Length(max= 32,message="编码长度不能超过32")
    private String reportId;
    /**
    * 
    */
    @Size(max= 32,message="编码长度不能超过32")
    @ApiModelProperty("")
    @Length(max= 32,message="编码长度不能超过32")
    private String batchNo;
    /**
    * 
    */
    @Size(max= 64,message="编码长度不能超过64")
    @ApiModelProperty("")
    @Length(max= 64,message="编码长度不能超过64")
    private String processStage;
    /**
    * 
    */
    @Size(max= 128,message="编码长度不能超过128")
    @ApiModelProperty("")
    @Length(max= 128,message="编码长度不能超过128")
    private String itemName;
    /**
    * 
    */
    @Size(max= 64,message="编码长度不能超过64")
    @ApiModelProperty("")
    @Length(max= 64,message="编码长度不能超过64")
    private String itemCategory;
    /**
    * 
    */
    @Size(max= 128,message="编码长度不能超过128")
    @ApiModelProperty("")
    @Length(max= 128,message="编码长度不能超过128")
    private String standardValue;
    /**
    * 
    */
    @Size(max= 128,message="编码长度不能超过128")
    @ApiModelProperty("")
    @Length(max= 128,message="编码长度不能超过128")
    private String testValue;
    /**
    * 
    */
    @Size(max= 32,message="编码长度不能超过32")
    @ApiModelProperty("")
    @Length(max= 32,message="编码长度不能超过32")
    private String unit;
    /**
    * 
    */
    @Size(max= 16,message="编码长度不能超过16")
    @ApiModelProperty("")
    @Length(max= 16,message="编码长度不能超过16")
    private String result;
    /**
    * 
    */
    @Size(max= 128,message="编码长度不能超过128")
    @ApiModelProperty("")
    @Length(max= 128,message="编码长度不能超过128")
    private String testMethod;
    /**
    * 
    */
    @Size(max= 64,message="编码长度不能超过64")
    @ApiModelProperty("")
    @Length(max= 64,message="编码长度不能超过64")
    private String methodVersion;
    /**
    * 
    */
    @ApiModelProperty("")
    private Date testDate;
    /**
    * 
    */
    @Size(max= 64,message="编码长度不能超过64")
    @ApiModelProperty("")
    @Length(max= 64,message="编码长度不能超过64")
    private String operator;
    /**
    * 
    */
    @Size(max= 64,message="编码长度不能超过64")
    @ApiModelProperty("")
    @Length(max= 64,message="编码长度不能超过64")
    private String reviewer;
    /**
    * 
    */
    @Size(max= 64,message="编码长度不能超过64")
    @ApiModelProperty("")
    @Length(max= 64,message="编码长度不能超过64")
    private String approver;
    /**
    * 
    */
    @Size(max= 64,message="编码长度不能超过64")
    @ApiModelProperty("")
    @Length(max= 64,message="编码长度不能超过64")
    private String equipmentId;
    /**
    * 
    */
    @Size(max= 64,message="编码长度不能超过64")
    @ApiModelProperty("")
    @Length(max= 64,message="编码长度不能超过64")
    private String reagentLot;
    /**
    * 
    */
    @ApiModelProperty("")
    private Date createTime;
    /**
    * 
    */
    @ApiModelProperty("")
    private Date updateTime;
    /**
    * 
    */
    @Size(max= 256,message="编码长度不能超过256")
    @ApiModelProperty("")
    @Length(max= 256,message="编码长度不能超过256")
    private String remark;



}
