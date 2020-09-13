package com.zbkj.crmeb.front.service;

import com.common.PageParamRequest;
import com.zbkj.crmeb.front.request.*;
import com.zbkj.crmeb.front.response.ConfirmOrderResponse;
import com.zbkj.crmeb.front.response.OrderDataResponse;
import com.zbkj.crmeb.front.response.OrderPayResponse;

import java.util.HashMap;

import com.zbkj.crmeb.front.response.StoreOrderDetailResponse;
import com.zbkj.crmeb.front.vo.OrderAgainVo;
import com.zbkj.crmeb.store.model.StoreOrder;
import com.zbkj.crmeb.store.request.StoreProductReplyAddRequest;
import com.zbkj.crmeb.store.response.StoreOrderListResponse;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Classname StoreOrderService
 * @Description H5端订单操作
 * @Date 2020/7/4 11:16 上午
 * @Created by stivepeim
 */
public interface OrderService {
    /**
     * 订单确认
     * @param cartIds 购物车id集合
     * @return 确认订单信息
     */
    ConfirmOrderResponse confirmOrder(List<Integer> cartIds, boolean isNew);


    /**
     * 创建订单
     * @param request 创建订单参数
     * @param key 订单key
     * @return payResponse
     */
    OrderPayResponse createOrder(OrderCreateRequest request, String key, String ip);


    /**
     * 再次下单
     * @param request 参数
     * @return 下单结果
     */
    HashMap<String, Object> againOrder(OrderAgainRequest request);

    /**
     * 计算订单金额
     * @param request 订单提交参数
     * @param orderKey 订单key
     * @return
     */
    HashMap<String, Object> computedOrder(OrderComputedRequest request, String orderKey);

    /**
     * 支付
     * @param request 支付参数
     * @return 支付结果
     */
    HashMap<String, Object> payOrder(OrderPayRequest request, String ip);

    /**
     * 订单列表
     * @param type 类型
     * @param pageRequest 分页
     * @return 订单集合
     */
    List<OrderAgainVo> list(Integer type, PageParamRequest pageRequest);

    /**
     * 订单详情
     * @param orderId 订单id
     */
    StoreOrderDetailResponse detailOrder(String orderId);

    /**
     * 订单状态数量
     * @return 订单状态数据量
     */
    OrderDataResponse orderData();

    /**
     * 查询退款理由
     * @return 退款理由集合
     */
    List<String> getRefundReason();

    Boolean delete(Integer id);

    boolean reply(StoreProductReplyAddRequest request);

    boolean take(Integer id);

    boolean cancel(Integer id);

    boolean refundApply(OrderRefundApplyRequest request);

    /**
     * 订单退款前验证
     * @param request 退款参数
     */
    boolean refundVerify(OrderRefundVerifyRequest request);

    /**
     * 订单物流查看
     */
    Object expressOrder(String orderId);

    /**
     * 获取待评价商品信息
     * @param getProductReply 订单详情参数
     * @return 待评价
     */
    Object getReplyProduct(GetProductReply getProductReply);

    /**
     * 更换支付类型
     * @param payType 支付类型
     */
    boolean changePayType(String payType,String orderId);

}
