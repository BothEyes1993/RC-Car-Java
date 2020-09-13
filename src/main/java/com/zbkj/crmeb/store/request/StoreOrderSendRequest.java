package com.zbkj.crmeb.store.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author stivepeim
 * @title: StoreOrderRequest
 * @projectName crmeb
 * @description: TODO
 * @date 2020/5/2816:11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="StoreOrderSendRequest对象", description="发货")
public class StoreOrderSendRequest {
    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "订单ID")
    @TableId(value = "id", type = IdType.AUTO)
    @Min(value = 1, message = "请选择订单")
    private Integer id;

    @ApiModelProperty(value = "类型， 1，2，3", allowableValues = "range[1,2,3]")
    @NotBlank(message = "请选择类型")
    private String type;

    @ApiModelProperty(value = "快递公司")
    private String expressId;

    @ApiModelProperty(value = "快递单号")
    private String expressCode;

}
