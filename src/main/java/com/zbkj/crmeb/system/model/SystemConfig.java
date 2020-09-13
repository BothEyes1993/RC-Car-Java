package com.zbkj.crmeb.system.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 配置表
 * </p>
 *
 * @author Mr.Zhang
 * @since 2020-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("eb_system_config")
@ApiModel(value="SystemConfig对象", description="配置表")
public class SystemConfig implements Serializable {

    private static final long serialVersionUID=1L;

    public SystemConfig() {
    }

    public SystemConfig(String name, String title, Integer formId, String value, Boolean status) {
        this.name = name;
        this.title = title;
        this.formId = formId;
        this.value = value;
        this.status = status;
    }

    @ApiModelProperty(value = "配置id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "字段名称")
    private String name;

    @ApiModelProperty(value = "字段提示文字")
    private String title;

    @ApiModelProperty(value = "表单id")
    private Integer formId;

    @ApiModelProperty(value = "值")
    private String value;

    @ApiModelProperty(value = "是否隐藏")
    private Boolean status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
