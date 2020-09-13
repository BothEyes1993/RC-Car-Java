package com.zbkj.crmeb.front.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author stivepeim
 * @title: StoreCartRequest
 * @projectName crmeb
 * @description: 添加购物车参数
 * @date 2020/7/4
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CartRequest对象", description="购物车")
public class CartRequest {
    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "商品ID", required = true)
    @NotNull(message = "商品id不能为空")
    private Integer productId;

    @ApiModelProperty(value = "商品属性 -- attr 对象的id")
    @NotBlank(message = "商品属性id不能为空")
    private String productAttrUnique;

    @ApiModelProperty(value = "商品数量", required = true)
    @NotNull
    @Min(value = 1, message = "商品数量不能小于1")
    @Max(value = 99, message = "单个商品数量不能大于99")
    private Integer cartNum;

    @ApiModelProperty(value = "是否为立即购买", required = true)
    @NotNull(message = "是否为立即购买不能为空")
    private Boolean isNew;

//    @ApiModelProperty(value = "拼团id")
//    private Integer combinationId;
//
//    @ApiModelProperty(value = "秒杀商品ID")
//    private Integer seckillId;
//
//    @ApiModelProperty(value = "砍价id")
//    private Integer bargainId;
}
