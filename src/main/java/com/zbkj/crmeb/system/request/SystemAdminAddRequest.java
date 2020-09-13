package com.zbkj.crmeb.system.request;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 后台管理员表
 * </p>
 *
 * @author Mr.Zhang
 * @since 2020-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("eb_system_admin")
@ApiModel(value="SystemAdminAddRequest对象", description="后台管理员表")
public class SystemAdminAddRequest implements Serializable {

    private static final long serialVersionUID=1L;

//    @ApiModelProperty(value = "后台管理员表ID")
//    @TableId(value = "id", type = IdType.AUTO)
//    private Integer id;

    @ApiModelProperty(value = "后台管理员账号", required = true)
    @NotNull(message = "后台管理员账号不能为空")
    private String account;

    @ApiModelProperty(value = "后台管理员密码", required = true)
    @NotNull(message = "管理员密码不能为空")
    private String pwd;

    @ApiModelProperty(value = "后台管理员姓名", required = true)
    @NotNull(message = "管理姓名不能为空")
    private String realName;

    @ApiModelProperty(value = "后台管理员权限(menus_id)", required = true)
    @NotNull(message = "menus_id不能为空")
    private String roles;

//    @ApiModelProperty(value = "后台管理员最后一次登录ip")
//    private String lastIp;

//    @ApiModelProperty(value = "后台管理员最后一次登录时间")
//    private Integer lastTime;

//    @ApiModelProperty(value = "后台管理员添加时间")
//    private Integer addTime;

//    @ApiModelProperty(value = "登录次数")
//    private Integer loginCount;

    @ApiModelProperty(value = "后台管理员级别", required = true)
    @Min(value=0, message = "管理员级别不能小于0")
    private Integer level;

    @ApiModelProperty(value = "后台管理员状态 1有效0无效", required = true)
    @NotNull(message = "status 字段不能为空")
    @Min(value=0, message = "不能小于0")
    @Max(value = 1, message = "不能大于1")
    private Boolean status;

//    @ApiModelProperty(value = "是否删除 1删除 0未删除")
//    private Boolean isDel;


}
