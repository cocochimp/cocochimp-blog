package com.cocochimp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cocochimp.constants.SystemConstants;
import com.cocochimp.domain.ResponseResult;
import com.cocochimp.domain.entity.Article;
import com.cocochimp.domain.entity.Category;
import com.cocochimp.domain.vo.CategoryVo;
import com.cocochimp.domain.vo.PageVo;
import com.cocochimp.mapper.CategoryMapper;
import com.cocochimp.service.ArticleService;
import com.cocochimp.service.CategoryService;
import com.cocochimp.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2023-02-14 12:49:21
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;

    @Override
    public ResponseResult getCategoryList() {
        //查询文章表  状态为已发布的文章
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(articleWrapper);
        //获取文章的分类id，并且去重
        Set<Long> categoryIds = articleList.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());

        //查询分类表
        List<Category> categories = listByIds(categoryIds);
        categories = categories.stream().
                filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        //封装vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);

        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public List<CategoryVo> listAllCategory() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus, SystemConstants.NORMAL);
        List<Category> list = list(wrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(list, CategoryVo.class);
        return categoryVos;
    }


    @Override
    public ResponseResult getAllCategoryList(Integer pageNum, Integer pageSize, String name, Integer status) {
        //查询所有审核通过的
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(StringUtils.hasText(name), Category::getName, name);
        queryWrapper.like(status!=null,Category::getStatus,status);

        Page<Category> page = new Page<>(pageNum,pageSize);

        page(page, queryWrapper);

        //转换成vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(page.getRecords(), CategoryVo.class);

        //封装数据返回
        PageVo pageVo = new PageVo(categoryVos,page.getTotal());

        //封装返回
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addCategory(CategoryVo categoryVo) {
        Category category = BeanCopyUtils.copyBean(categoryVo, Category.class);
        save(category);
        return ResponseResult.okResult();
    }


}


