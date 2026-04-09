package com.example.demo.modules.membership.dto;

import com.example.demo.common.PageParam;
import com.example.demo.enums.CouponTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 基于SpringBoot框架的个人练手项目-优惠券模板，前后端参数
 *
 * @author JMF
 * @date 2026-04-08 12:57
 * @date 2026-04-08
 */
@Data
@Api(tags = "优惠券模板，前后端参数")
@Validated
public class CouponDTO extends PageParam {
    /**
     * 主键ID
     */
    @NotNull(message="[主键ID]不能为空")
    @ApiModelProperty("主键ID")
    private Long id;
    /**
     * 优惠券名称
     */
    @NotBlank(message="[优惠券名称]不能为空")
    @Size(max= 64,message="编码长度不能超过64")
    @ApiModelProperty("优惠券名称")
    @Length(max= 64,message="编码长度不能超过64")
    private String name;
    /**
     * 类型 1满减 2折扣 3无门槛
     */
    @NotNull(message="[类型 1满减 2折扣 3无门槛]不能为空")
    @ApiModelProperty("类型 1满减 2折扣 3无门槛")
    private CouponTypeEnum type;
    /**
     * 满减门槛（满减券必填）
     */
    @ApiModelProperty("满减门槛（满减券必填）")
    private BigDecimal fullAmount;
    /**
     * 减免金额（满减/无门槛）
     */
    @ApiModelProperty("减免金额（满减/无门槛）")
    private BigDecimal discountAmount;
    /**
     * 折扣率（折扣券，如0.8表示8折）
     */
    @ApiModelProperty("折扣率（折扣券，如0.8表示8折）")
    private BigDecimal discountRate;
    /**
     * 发放总量
     */
    @NotNull(message="[发放总量]不能为空")
    @ApiModelProperty("发放总量")
    private Integer totalCount;
    /**
     * 剩余库存
     */
    @NotNull(message="[剩余库存]不能为空")
    @ApiModelProperty("剩余库存")
    private Integer remainCount;
    /**
     * 有效期开始
     */
    @NotNull(message="[有效期开始]不能为空")
    @ApiModelProperty("有效期开始")
    private Date validStartTime;
    /**
     * 有效期结束
     */
    @NotNull(message="[有效期结束]不能为空")
    @ApiModelProperty("有效期结束")
    private Date validEndTime;
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
     * 是否删除 0否1是
     */
    @ApiModelProperty("是否删除 0否1是")
    private Integer isDeleted;
}
