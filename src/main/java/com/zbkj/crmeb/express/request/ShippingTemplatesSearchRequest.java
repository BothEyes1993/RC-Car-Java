package com.zbkj.crmeb.express.request;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
@TableName("eb_shipping_templates")
@ApiModel(value="ShippingTemplatesSearchRequest对象", description="模板搜索")
public class ShippingTemplatesSearchRequest implements Serializable {

    private static final long serialVersionUID=1L;


    @ApiModelProperty(value = "模板名称")
    private String keywords;
}
