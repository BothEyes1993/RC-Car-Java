package com.zbkj.crmeb.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 身份管理表
 * </p>
 *
 * @author Mr.Zhang
 * @since 2020-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("eb_system_role")
@ApiModel(value="SystemRole对象", description="身份管理表")
public class SystemRole implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "身份管理id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "身份管理名称")
    private String roleName;

    @ApiModelProperty(value = "身份管理权限(menus_id)")
    private String rules;

    private Integer level;

    @ApiModelProperty(value = "状态")
    private Boolean status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

}
