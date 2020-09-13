package com.zbkj.crmeb.express.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author Mr.Zhang
 * @since 2020-04-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("eb_shipping_templates_region")
@ApiModel(value="ShippingTemplatesRegionRequest对象", description="付费")
public class ShippingTemplatesRegionRequest implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "城市ID, 多个逗号分割。全国 all", required = true, example = "1,2,3,4")
    @NotNull(message = "请选择城市")
    private String cityId;

    @ApiModelProperty(value = "城市名称描述")
    private String title;

    @ApiModelProperty(value = "首件", required = true, example = "0.1")
    @DecimalMin(value = "0.1", message = "首件金额不能低于0.1")
    private BigDecimal first;

    @ApiModelProperty(value = "首件运费", required = true, example = "0.1")
    @DecimalMin(value = "0.1", message = "首件运费金额不能低于0.1")
    private BigDecimal firstPrice;

    @ApiModelProperty(value = "续件", required = true, example = "0.1")
    @DecimalMin(value = "0.1", message = "续件不能低于0.1")
    private BigDecimal renewal;

    @ApiModelProperty(value = "续件运费", required = true, example = "0.1")
    @DecimalMin(value = "0.1", message = "续件运费金额不能低于0.1")
    private BigDecimal renewalPrice;

    @ApiModelProperty(value = "分组唯一值")
    private String uniqid;
}
