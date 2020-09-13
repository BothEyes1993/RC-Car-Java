package com.zbkj.crmeb.marketing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.PageParamRequest;
import com.github.pagehelper.PageInfo;
import com.zbkj.crmeb.front.request.UserCouponReceiveRequest;
import com.zbkj.crmeb.marketing.model.StoreCouponUser;
import com.zbkj.crmeb.marketing.request.StoreCouponUserRequest;
import com.zbkj.crmeb.marketing.request.StoreCouponUserSearchRequest;
import com.zbkj.crmeb.marketing.response.StoreCouponUserOrder;
import com.zbkj.crmeb.marketing.response.StoreCouponUserResponse;
import com.zbkj.crmeb.store.model.StoreOrder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
* @author Mr.Zhang
* @Description StoreCouponUserService 接口
* @since  2020-05-18
*/
public interface StoreCouponUserService extends IService<StoreCouponUser> {

    PageInfo<StoreCouponUserResponse> getList(StoreCouponUserSearchRequest request, PageParamRequest pageParamRequest);

    /**
     * 基本条件查询
     * @param storeCouponUser 基本参数
     * @return 查询的优惠券结果
     */
    List<StoreCouponUser> getList(StoreCouponUser storeCouponUser);

    boolean receive(StoreCouponUserRequest storeCouponUserRequest);

    boolean use(Integer id, List<Integer> productIdList, BigDecimal price);

    boolean receiveAll(UserCouponReceiveRequest request, Integer userId, String type);

    boolean rollbackByCancelOrder(StoreOrder storeOrder);

    HashMap<Integer, StoreCouponUser> getMapByUserId(Integer userId);

    /**
     * 根据购物车id获取可用优惠券
     * @param cartIds 购物车id
     * @return 可用优惠券集合
     */
    List<StoreCouponUserOrder> getListByCartIds(List<Integer> cartIds);

    List<StoreCouponUserResponse> getListFront(Integer userId, PageParamRequest pageParamRequest);
}
