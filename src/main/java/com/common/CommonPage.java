package com.common;
import com.constants.Constants;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import com.github.pagehelper.PageInfo;


import java.util.ArrayList;
import java.util.List;

@Data
public class CommonPage<T> {
    private Integer page = Constants.DEFAULT_PAGE;
    private Integer limit = Constants.DEFAULT_LIMIT;
    private Integer totalPage = 0;
    private Long total = 0L ;
    private List<T> list = new ArrayList<>();


    /**
     * 将PageHelper分页后的list转为分页信息
     */
    public static <T> CommonPage<T> restPage(List<T> list) {
        CommonPage<T> result = new CommonPage<T>();
        PageInfo<T> pageInfo = new PageInfo<T>(list);
        result.setTotalPage(pageInfo.getPages());
        result.setPage(pageInfo.getPageNum());
        result.setLimit(pageInfo.getPageSize());
        result.setTotal(pageInfo.getTotal());
        result.setList(pageInfo.getList());
        return result;
    }

    /**
     * 将SpringData分页后的list转为分页信息
     */
    public static <T> CommonPage<T> restPage(Page<T> pageInfo) {
        CommonPage<T> result = new CommonPage<T>();
        result.setTotalPage(pageInfo.getTotalPages());
        result.setPage(pageInfo.getNumber());
        result.setLimit(pageInfo.getSize());
        result.setTotal(pageInfo.getTotalElements());
        result.setList(pageInfo.getContent());
        return result;
    }

    /**
     * 将PageHelper分页后的 PageInfo 转为分页信息
     */
    public static <T> CommonPage<T> restPage(PageInfo<T> pageInfo) {
        CommonPage<T> result = new CommonPage<T>();
        result.setTotalPage(pageInfo.getPages());
        result.setPage(pageInfo.getPageNum());
        result.setLimit(pageInfo.getPageSize());
        result.setTotal(pageInfo.getTotal());
        result.setList(pageInfo.getList());
        return result;
    }

    /**
     * 对象A复制对象B的分页信息 //TODO 多次数据查询导致分页数据异常解决办法
     */
    public static <T> PageInfo<T> copyPageInfo(com.github.pagehelper.Page originPageInfo, List<T> list) {
        PageInfo<T> pageInfo = new PageInfo<>(list);
        BeanUtils.copyProperties(originPageInfo, pageInfo, "list");
        return pageInfo;
    }
}
