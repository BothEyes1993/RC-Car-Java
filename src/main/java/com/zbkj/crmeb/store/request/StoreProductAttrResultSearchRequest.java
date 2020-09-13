package com.zbkj.crmeb.store.request;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author stivepeim
 * @title: StoreProductAttrResultSearchRequest
 * @projectName crmeb
 * @description: TODO
 * @date 2020/5/2716:05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("eb_store_product_attr_result")
@ApiModel(value="StoreProductAttrResult对象", description="商品属性详情")
public class StoreProductAttrResultSearchRequest {
    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "商品ID")
    private Integer productId;

    @ApiModelProperty(value = "商品属性参数")
    private String result;

    @ApiModelProperty(value = "上次修改时间")
    private Integer changeTime;

    @ApiModelProperty(value = "活动类型 0=商品，1=秒杀，2=砍价，3=拼团")
    private Boolean type;
}
