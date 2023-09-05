package com.cocochimp.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cocochimp.domain.ResponseResult;
import com.cocochimp.domain.dto.AddArticleDto;
import com.cocochimp.domain.entity.Article;
import com.cocochimp.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto article){
        return articleService.add(article);
    }

    @GetMapping("/list")
    public ResponseResult getAllLink(Integer pageNum, Integer pageSize ,String title, String summary){
        return articleService.getAllArticleList(pageNum,pageSize,title,summary);
    }

    @PutMapping
    public ResponseResult update(@RequestBody Article article){
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId,article.getId());

        articleService.updateById(article);
        return ResponseResult.okResult(article);
    }

    @GetMapping("/{id}")
    public ResponseResult updateArticle(@PathVariable("id")Long id, Article article){
//        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
//        updateWrapper.eq(Article::getId,id);
//
//        articleService.updateById(article);
//        return ResponseResult.okResult(article);
        return ResponseResult.okResult(articleService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable("id") String id){
        String[] split = id.split(",");
        for(String s:split){
            articleService.removeByIds(Collections.singleton(s));
        }
        return ResponseResult.okResult();
    }

}