package com.cocochimp.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cocochimp.domain.ResponseResult;
import com.cocochimp.domain.dto.TagListDto;
import com.cocochimp.domain.entity.Tag;
import com.cocochimp.domain.vo.TagVo;
import com.cocochimp.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }

    //查询所有标签接口
    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        List<TagVo> list = tagService.listAllTag();
        return ResponseResult.okResult(list);
    }

    @PostMapping
    public ResponseResult addTag(@RequestBody TagListDto tagListDto){
        return tagService.addTag(tagListDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable("id") String id){
        String[] split = id.split(",");
        for(String s:split){
            tagService.removeByIds(Collections.singleton(s));
        }
        return ResponseResult.okResult();
    }

    @PutMapping
    public ResponseResult update(@RequestBody Tag tag){
        LambdaUpdateWrapper<Tag> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Tag::getId,tag.getId());

        tagService.updateById(tag);
        return ResponseResult.okResult(tag);
    }

    @GetMapping("/{id}")
    public ResponseResult updateTag(@PathVariable("id") Long id){
        return ResponseResult.okResult(tagService.getById(id));
    }

}

