package com.zbkj.crmeb.category.controller;

import com.common.CommonPage;
import com.common.CommonResult;
import com.common.PageParamRequest;
import com.exception.CrmebException;
import com.utils.CrmebUtil;
import com.zbkj.crmeb.category.model.Category;
import com.zbkj.crmeb.category.request.CategoryRequest;
import com.zbkj.crmeb.category.request.CategorySearchRequest;
import com.zbkj.crmeb.category.service.CategoryService;
import com.zbkj.crmeb.category.vo.CategoryTreeVo;
import com.zbkj.crmeb.system.service.SystemAttachmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 分类表 前端控制器
 */
@Slf4j
@RestController
@RequestMapping("api/admin/category")
@Api(tags = "分类服务")

    public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SystemAttachmentService systemAttachmentService;

    /**
     * 分页显示分类表
     * @param request 搜索条件
     * @param pageParamRequest 分页参数
     * @author Mr.Zhang
     * @since 2020-04-16
     */
    @ApiOperation(value = "分页列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<Category>>  getList(@ModelAttribute CategorySearchRequest request, @ModelAttribute PageParamRequest pageParamRequest){
        CommonPage<Category> categoryCommonPage = CommonPage.restPage(categoryService.getList(request, pageParamRequest));
        return CommonResult.success(categoryCommonPage);
    }

    /**
     * 新增分类表
     * @param categoryRequest 新增参数
     * @author Mr.Zhang
     * @since 2020-04-16
     */
    @ApiOperation(value = "新增")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResult<String> save(@Validated CategoryRequest categoryRequest){
        Category category = new Category();

        //检测标题是否存在
        if(categoryService.checkName(categoryRequest.getName(), category.getType()) > 0){
            throw new CrmebException("此分类已存在");
        }

        BeanUtils.copyProperties(categoryRequest, category);
        category.setPath(categoryService.getPathByPId(category.getPid()));
        category.setExtra(systemAttachmentService.clearPrefix(category.getExtra()));
        if(categoryService.save(category)){
            return CommonResult.success();
        }else{
            return CommonResult.failed();
        }
    }

    /**
     * 删除分类表
     * @param id Integer
     * @author Mr.Zhang
     * @since 2020-04-16
     */
    @ApiOperation(value = "删除")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ApiImplicitParam(name="id", value="分类ID")
    public CommonResult<String> delete(@RequestParam(value = "id") Integer id){
        if(categoryService.delete(id) > 0){
            return CommonResult.success();
        }else{
            return CommonResult.failed();
        }
    }

    /**
     * 修改分类表
     * @param id integer id
     * @param categoryRequest 修改参数
     * @author Mr.Zhang
     * @since 2020-04-16
     */
    @ApiOperation(value = "修改")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiImplicitParam(name="id", value="分类ID")
    public CommonResult<String> update(@RequestParam Integer id, @ModelAttribute CategoryRequest categoryRequest){
        if(null == id || id <= 0) throw new CrmebException("id 参数不合法");
        categoryRequest.setExtra(systemAttachmentService.clearPrefix(categoryRequest.getExtra()));
        if(categoryService.update(categoryRequest, id)){
            return CommonResult.success();
        }else{
            return CommonResult.failed();
        }
    }

    /**
     * 查询分类表信息
     * @param id Integer
     * @author Mr.Zhang
     * @since 2020-04-16
     */
    @ApiOperation(value = "详情")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ApiImplicitParam(name="id", value="分类ID")
    public CommonResult<Category> info(@RequestParam(value = "id") Integer id){
        Category category = categoryService.getById(id);
        return CommonResult.success(category);
    }


    /**
     * 查询分类表信息
     * @author Mr.Zhang
     * @since 2020-04-16
     */
    @ApiOperation(value = "获取tree结构的列表")
    @RequestMapping(value = "/list/tree", method = RequestMethod.GET)
    @ApiImplicitParams({
        @ApiImplicitParam(name="type", value="类型ID | 类型，1 产品分类，2 附件分类，3 文章分类， 4 设置分类， 5 菜单分类， 6 配置分类， 7 秒杀配置", example = "1"),
        @ApiImplicitParam(name="status", value="-1=全部，0=未生效，1=已生效", example = "1")
    })
    public CommonResult<List<CategoryTreeVo>> getListTree(@RequestParam(name = "type") Integer type,
                                                          @RequestParam(name = "status") Integer status){
        List<CategoryTreeVo> listTree = categoryService.getListTree(type, status);
        return CommonResult.success(listTree);
    }

    /**
     * 根据分类id集合获取分类数据
     * @param ids String id集合字符串
     * @since 2020-04-16
     */
    @ApiOperation(value = "根据id集合获取分类列表")
    @RequestMapping(value = "/list/ids", method = RequestMethod.GET)
    @ApiImplicitParam(name = "ids", value="分类id集合")
    public CommonResult<List<Category>> getByIds(@Validated @RequestParam(name = "ids") String ids){
        return CommonResult.success(categoryService.getByIds(CrmebUtil.stringToArray(ids)));
    }

}



