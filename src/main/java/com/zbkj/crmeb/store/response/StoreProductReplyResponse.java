package com.zbkj.crmeb.store.response;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zbkj.crmeb.store.model.StoreProduct;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class StoreProductReplyResponse {
    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "评论ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户ID")
    private Integer uid;

    @ApiModelProperty(value = "订单ID")
    private Integer oid;

    @ApiModelProperty(value = "商品id")
    private Integer productId;

    @ApiModelProperty(value = "某种商品类型(普通商品、秒杀商品）")
    private String replyType;

    @ApiModelProperty(value = "商品分数")
    private Integer productScore;

    @ApiModelProperty(value = "服务分数")
    private Integer serviceScore;

    @ApiModelProperty(value = "评论内容")
    private String comment;

    @ApiModelProperty(value = "评论图片")
    private List<String> pics;

    @ApiModelProperty(value = "管理员回复内容")
    private String merchantReplyContent;

    @ApiModelProperty(value = "管理员回复时间")
    private Integer merchantReplyTime;

    @ApiModelProperty(value = "0未删除1已删除")
    private Boolean isDel;

    @ApiModelProperty(value = "0未回复1已回复")
    private Boolean isReply;

    @ApiModelProperty(value = "用户名称")
    private String nickname;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "评论时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    private StoreProduct storeProduct;
}
