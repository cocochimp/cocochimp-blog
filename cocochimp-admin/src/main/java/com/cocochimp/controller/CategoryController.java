package com.cocochimp.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cocochimp.domain.ResponseResult;
import com.cocochimp.domain.entity.Category;
import com.cocochimp.domain.vo.CategoryVo;
import com.cocochimp.domain.vo.ExcelCategoryVo;
import com.cocochimp.enums.AppHttpCodeEnum;
import com.cocochimp.service.CategoryService;
import com.cocochimp.utils.BeanCopyUtils;
import com.cocochimp.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        List<CategoryVo> list = categoryService.listAllCategory();
        return ResponseResult.okResult(list);
    }


    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            //获取需要导出的数据
            List<Category> categoryVos = categoryService.list();

            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryVos, ExcelCategoryVo.class);
            //把数据写入到Excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);

        } catch (Exception e) {
            //如果出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }

    @PostMapping
    public ResponseResult addCategory(@RequestBody CategoryVo categoryVo){
        return categoryService.addCategory(categoryVo);
    }


    @GetMapping("/list")
    public ResponseResult getAllCategory(Integer pageNum, Integer pageSize ,String name, Integer status){
        return categoryService.getAllCategoryList(pageNum,pageSize,name,status);
    }

    @PutMapping
    public ResponseResult update(@RequestBody Category category){
        LambdaUpdateWrapper<Category> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Category::getId,category.getId());

        categoryService.updateById(category);
        return ResponseResult.okResult(category);
    }

    @GetMapping("/{id}")
    public ResponseResult updateCategory(@PathVariable("id")Long id, Category category){
//        LambdaUpdateWrapper<Category> updateWrapper = new LambdaUpdateWrapper<>();
//        updateWrapper.eq(Category::getId,id);
//
//        categoryService.updateById(category);
//        return ResponseResult.okResult(category);
        return ResponseResult.okResult(categoryService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteCategory(@PathVariable("id") String id){
        String[] split = id.split(",");
        for(String s:split){
            categoryService.removeByIds(Collections.singleton(s));
        }
        return ResponseResult.okResult();
    }

    @PutMapping("/changeStatus")
    public ResponseResult updateUserStatus(@RequestBody Category category){
        LambdaUpdateWrapper<Category> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Category::getId,category.getId());
        category.setStatus(String.valueOf(Math.abs((Integer.parseInt(category.getStatus())-1))));
        categoryService.updateById(category);
        return ResponseResult.okResult(category);
    }
}