package com.zbkj.crmeb.system.request;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 设置用户等级表
 * </p>
 *
 * @author Mr.Zhang
 * @since 2020-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("eb_system_user_level")
@ApiModel(value="SystemUserLevelRequest对象", description="设置用户等级表")
public class SystemUserLevelRequest implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "会员名称")
    @NotBlank
    private String name;

//    @ApiModelProperty(value = "是否为永久会员")
//    @NotNull
//    private Boolean isForever;

//    @ApiModelProperty(value = "有效时间")
//    @NotNull
//    @Min(value = 0)
//    private Integer validDate;

    @ApiModelProperty(value = "达到多少升级经验")
    private Integer experience;

    @ApiModelProperty(value = "会员等级")
    @NotNull(message = "会员等级不能为空")
    @Min(value = 0)
    private Integer grade;

    @ApiModelProperty(value = "享受折扣")
    @NotNull
    @DecimalMin(value = "0.00", message = "请输入正确的金额")
    private BigDecimal discount;

    @ApiModelProperty(value = "会员图标")
    @NotBlank(message = "会员图标不能为空")
    private String icon;

    @ApiModelProperty(value = "会员卡背景")
    @NotBlank(message = "会员卡背景不能为空")
    private String image;

    @ApiModelProperty(value = "是否显示 1=显示,0=隐藏")
    @NotNull(message = "是否现实不能为空")
    private Boolean isShow;

    @ApiModelProperty(value = "说明")
//    @NotNull(message = "说明不能为空")
    private String memo;
}
