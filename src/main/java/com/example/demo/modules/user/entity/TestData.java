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
* @TableName test_data
*/
@TableName("test_data")
@Data
public class TestData implements Serializable {

    /**
    * 
    */
    @NotNull(message="[]不能为空")
    @ApiModelProperty("")
    private Long id;
    /**
    * 
    */
    @Size(max= 100,message="编码长度不能超过100")
    @ApiModelProperty("")
    @Length(max= 100,message="编码长度不能超过100")
    private String name;
    /**
    * 
    */
    @ApiModelProperty("编码长度")
    private String content;
    /**
    * 
    */
    @ApiModelProperty("")
    private Integer status;
    /**
    * 
    */
    @ApiModelProperty("")
    private Date createTime;

}
