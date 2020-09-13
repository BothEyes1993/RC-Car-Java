package com.zbkj.crmeb.front.controller;

import com.common.CommonResult;
import com.common.PageParamRequest;
import com.zbkj.crmeb.front.request.UserCouponReceiveRequest;
import com.zbkj.crmeb.marketing.request.StoreCouponUserRequest;
import com.zbkj.crmeb.marketing.response.StoreCouponUserResponse;
import com.zbkj.crmeb.marketing.service.StoreCouponUserService;
import com.zbkj.crmeb.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 优惠券发放记录表 前端控制器
 */
@Slf4j
@RestController
@RequestMapping("api/front/coupon")
@Api(tags = "营销 -- 优惠券")

public class UserCouponController {

    @Autowired
    private StoreCouponUserService storeCouponUserService;

    @Autowired
    private UserService userService;

    /**
     * 我的优惠券
     * @author Mr.Zhang
     * @since 2020-05-18
     */
    @ApiOperation(value = "我的优惠券")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<List<StoreCouponUserResponse>>  getList(){
        return CommonResult.success(storeCouponUserService.getListFront(userService.getUserIdException(), new PageParamRequest()));
    }

    /**
     * 领券
     * @param request UserCouponReceiveRequest 新增参数
     * @author Mr.Zhang
     * @since 2020-05-18
     */
    @ApiOperation(value = "领券")
    @RequestMapping(value = "/receive", method = RequestMethod.POST)
    public CommonResult<String> receive(@RequestBody @Validated UserCouponReceiveRequest request){
        StoreCouponUserRequest storeCouponUserRequest = new StoreCouponUserRequest();
        storeCouponUserRequest.setUid(userService.getUserIdException().toString());
        storeCouponUserRequest.setCouponId(request.getCouponId()[0]);
        if(storeCouponUserService.receive(storeCouponUserRequest)){
            return CommonResult.success();
        }else{
            return CommonResult.failed();
        }
    }

    /**
     * 领券
     * @param request UserCouponReceiveRequest 新增参数
     * @author Mr.Zhang
     * @since 2020-05-18
     */
    @ApiOperation(value = "批量领券")
    @RequestMapping(value = "/receive/batch", method = RequestMethod.POST)
    public CommonResult<String> receiveAll(@RequestBody @Validated UserCouponReceiveRequest request){
        if(storeCouponUserService.receiveAll(request, userService.getUserIdException(), "get")){
            return CommonResult.success();
        }else{
            return CommonResult.failed();
        }
    }
}



