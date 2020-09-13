package com.zbkj.crmeb.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.PageParamRequest;
import com.zbkj.crmeb.express.vo.LogisticsResultVo;
import com.zbkj.crmeb.store.model.StoreOrder;
import com.zbkj.crmeb.store.request.StoreOrderRefundRequest;
import com.zbkj.crmeb.store.request.StoreOrderSearchRequest;
import com.zbkj.crmeb.store.request.StoreOrderSendRequest;
import com.zbkj.crmeb.store.response.RetailShopOrderDataResponse;
import com.zbkj.crmeb.store.response.StoreOrderInfoResponse;
import com.zbkj.crmeb.store.response.StoreOrderResponse;
import com.zbkj.crmeb.system.request.SystemWriteOffOrderSearchRequest;
import com.zbkj.crmeb.system.response.SystemWriteOffOrderResponse;
import com.zbkj.crmeb.user.model.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
* @author Mr.Zhang
* @description StoreOrderService 接口
* @date 2020-05-28
*/
public interface StoreOrderService extends IService<StoreOrder> {

    List<StoreOrder> getList(StoreOrderSearchRequest request, PageParamRequest pageParamRequest);

    StoreOrderResponse getAdminList(StoreOrderSearchRequest request, PageParamRequest pageParamRequest);

    SystemWriteOffOrderResponse getWriteOffList(SystemWriteOffOrderSearchRequest request, PageParamRequest pageParamRequest);

    BigDecimal getSumBigDecimal(Integer userId, String date);

    Map<Integer, StoreOrder> getMapInId(List<Integer> orderIdList);

    /**
     * H5订单列表
     * @param storeOrder 查询参数
     * @param pageParamRequest 分页参数
     * @return 订单结果列表
     */
    List<StoreOrder> getUserOrderList(StoreOrder storeOrder, PageParamRequest pageParamRequest);
    /**
     * 创建订单
     * @param storeOrder 订单参数
     * @return 创建结果
     */
    boolean create(StoreOrder storeOrder);

    /**
     *  根据参数直接下单
     * @param userId 用户id
     * @param productId 产品id
     * @param cartNum 商品数量
     * @param productAttrUnique 商品唯一标识
     * @param type 商品默认类型
     * @param isNew isNew
     * @param combinationId 拼团id
     * @param skillId 秒杀id
     * @param bargainId 砍价id
     * @return 是否成功下单
     */
    List<String> addCartAgain(Integer userId, Integer productId, Integer cartNum, String productAttrUnique, String type,
                              boolean isNew, Integer combinationId, Integer skillId, Integer bargainId);

    /**
     * 订单基本查询
     * @param storeOrder 订单参数
     * @return 订单查询结果
     */
    List<StoreOrder> getByEntity(StoreOrder storeOrder);

    /**
     * 根据属性仅仅获取一条
     * @param storeOrder 参数
     * @return 当前查询结果
     */
    StoreOrder getByEntityOne(StoreOrder storeOrder);

    /**
     * 基本更新
     * @param storeOrder 更新参数
     * @return 更新结果
     */
    boolean updateByEntity(StoreOrder storeOrder);

    /**
     * 余额支付
     * @param storeOrder 待支付订单
     * @param currentUser 当前用户
     * @param formId 购买平台标识
     * @return 支付结果
     */
    boolean yuePay(StoreOrder storeOrder, User currentUser, String formId);

    /**
     * h5 top data 工具方法
     * @param status 状态参数
     * @return 查询到的订单结果
     */
    List<StoreOrder> getTopDataUtil(int status, int userId);

    int getOrderCount(Integer userId, String date);

    List<StoreOrder> getOrderGroupByDate(String dateLimit, int lefTime);

    boolean refund(StoreOrderRefundRequest request);

    StoreOrderInfoResponse info(Integer id);

    boolean send(StoreOrderSendRequest request);

    boolean mark(Integer id, String mark);

    boolean refundRefuse(Integer id, String reason);

    RetailShopOrderDataResponse getOrderDataByUserId(Integer userId);

    List<StoreOrder> getOrderByUserIdsForRetailShop(List<Integer> ids);

    StoreOrder getInfoByEntity(StoreOrder storeOrder);

    /**
     * 根据条件获取
     * @param storeOrder 订单条件
     * @return 结果
     */
    StoreOrder getInfoJustOrderInfo(StoreOrder storeOrder);

    LogisticsResultVo getLogisticsInfo(Integer id);

    Map<String, String> getStatus(StoreOrder storeOrder);
}
