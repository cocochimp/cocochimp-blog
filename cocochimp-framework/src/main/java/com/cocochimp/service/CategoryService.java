package com.cocochimp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cocochimp.domain.ResponseResult;
import com.cocochimp.domain.entity.Category;
import com.cocochimp.domain.vo.CategoryVo;

import java.util.List;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-02-14 12:49:20
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    List<CategoryVo> listAllCategory();

    ResponseResult getAllCategoryList(Integer pageNum, Integer pageSize, String name, Integer status);

    ResponseResult addCategory(CategoryVo category);
}

