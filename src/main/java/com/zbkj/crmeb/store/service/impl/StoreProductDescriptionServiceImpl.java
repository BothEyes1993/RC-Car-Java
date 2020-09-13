package com.zbkj.crmeb.store.service.impl;

import com.common.PageParamRequest;
import com.github.pagehelper.PageHelper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbkj.crmeb.store.dao.StoreProductDescriptionDao;
import com.zbkj.crmeb.store.model.StoreProductDescription;
import com.zbkj.crmeb.store.request.StoreProductDescriptionSearchRequest;
import com.zbkj.crmeb.store.service.StoreProductDescriptionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author Mr.Zhang
* @description StoreProductDescriptionServiceImpl 接口实现
* @date 2020-05-27
*/
@Service
public class StoreProductDescriptionServiceImpl extends ServiceImpl<StoreProductDescriptionDao, StoreProductDescription> implements StoreProductDescriptionService {

    @Resource
    private StoreProductDescriptionDao dao;


    /**
    * 列表
    * @param request 请求参数
    * @param pageParamRequest 分页类参数
    * @author Mr.Zhang
    * @since 2020-05-27
    * @return List<StoreProductDescription>
    */
    @Override
    public List<StoreProductDescription> getList(StoreProductDescriptionSearchRequest request, PageParamRequest pageParamRequest) {
        PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());

        //带 StoreProductDescription 类的多条件查询
        LambdaQueryWrapper<StoreProductDescription> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        StoreProductDescription model = new StoreProductDescription();
        BeanUtils.copyProperties(request, model);
        lambdaQueryWrapper.setEntity(model);
        return dao.selectList(lambdaQueryWrapper);
    }

    @Override
    public void deleteByProductId(int productId) {
        LambdaQueryWrapper<StoreProductDescription> lmq = new LambdaQueryWrapper<>();
        lmq.eq(StoreProductDescription::getProductId, productId);
        dao.delete(lmq);
    }
}

