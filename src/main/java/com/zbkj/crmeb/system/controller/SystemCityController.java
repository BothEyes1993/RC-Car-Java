package com.zbkj.crmeb.system.controller;

import com.common.CommonPage;
import com.common.CommonResult;
import com.common.PageParamRequest;
import com.zbkj.crmeb.category.vo.CategoryTreeVo;
import com.zbkj.crmeb.system.request.SystemCityRequest;
import com.zbkj.crmeb.system.request.SystemCitySearchRequest;
import com.zbkj.crmeb.system.vo.SystemCityTreeVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.zbkj.crmeb.system.service.SystemCityService;
import com.zbkj.crmeb.system.model.SystemCity;

import java.util.List;


/**
 * 城市表 前端控制器
 */
@Slf4j
@RestController
@RequestMapping("api/admin/system/city")
@Api(tags = "城市管理")
public class SystemCityController {

    @Autowired
    private SystemCityService systemCityService;

    /**
     * 分页显示城市表
     * @param request 搜索条件
     * @author Mr.Zhang
     * @since 2020-04-17
     */
    @ApiOperation(value = "分页列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<Object>  getList(@Validated SystemCitySearchRequest request){
        return CommonResult.success(systemCityService.getList(request));
    }

//    /**
//     * 新增城市表
//     * @param systemCityRequest 新增参数
//     * @author Mr.Zhang
//     * @since 2020-04-17
//     */
//    @ApiOperation(value = "新增")
//    @RequestMapping(value = "/save", method = RequestMethod.POST)
//    public CommonResult<String> save(@Validated SystemCityRequest systemCityRequest){
//        SystemCity systemCity = new SystemCity();
//        BeanUtils.copyProperties(systemCityRequest, systemCity);
//
//        if(systemCityService.save(systemCity)){
//            return CommonResult.success();
//        }else{
//            return CommonResult.failed();
//        }
//    }
//
//    /**
//     * 删除城市表
//     * @param id Integer
//     * @author Mr.Zhang
//     * @since 2020-04-17
//     */
//    @ApiOperation(value = "删除")
//    @RequestMapping(value = "/delete", method = RequestMethod.GET)
//    public CommonResult<String> delete(@RequestParam(value = "id") Integer id){
//        if(systemCityService.removeById(id)){
//            return CommonResult.success();
//        }else{
//            return CommonResult.failed();
//        }
//    }

    /**
     * 修改城市
     * @param id integer id
     * @param request 修改参数
     * @author Mr.Zhang
     * @since 2020-04-17
     */
    @ApiOperation(value = "修改")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult<String> update(@RequestParam Integer id, @Validated SystemCityRequest request){
        if(systemCityService.update(id, request)){
            return CommonResult.success();
        }else{
            return CommonResult.failed();
        }
    }

    /**
     * 修改状态
     * @param id integer id
     * @param status boolean 状态
     * @author Mr.Zhang
     * @since 2020-04-17
     */
    @ApiOperation(value = "修改状态")
    @RequestMapping(value = "/update/status", method = RequestMethod.POST)
    public CommonResult<String> update(@RequestParam Integer id, @RequestParam Boolean status){
        if(systemCityService.updateStatus(id, status)){
            return CommonResult.success();
        }else{
            return CommonResult.failed();
        }
    }

    /**
     * 查询城市表信息
     * @param id Integer
     * @author Mr.Zhang
     * @since 2020-04-17
     */
    @ApiOperation(value = "详情")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public CommonResult<SystemCity> info(@RequestParam(value = "id") Integer id){
        SystemCity systemCity = systemCityService.getById(id);
        return CommonResult.success(systemCity);
    }

    /**
     * 获取tree结构的列表
     * @author Mr.Zhang
     * @since 2020-04-16
     */
    @ApiOperation(value = "获取tree结构的列表")
    @RequestMapping(value = "/list/tree", method = RequestMethod.GET)
    public CommonResult<Object> getListTree(){
        return CommonResult.success(systemCityService.getListTree());
    }
}



