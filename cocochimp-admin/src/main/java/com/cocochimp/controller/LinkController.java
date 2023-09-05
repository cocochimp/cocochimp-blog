package com.cocochimp.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cocochimp.domain.ResponseResult;
import com.cocochimp.domain.entity.Link;
import com.cocochimp.domain.vo.LinkVo;
import com.cocochimp.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @GetMapping("/list")
    public ResponseResult getAllLink(Integer pageNum, Integer pageSize ,String name, Integer status){
        return linkService.getAllLinkList(pageNum,pageSize,name,status);
    }

    @PostMapping
    public ResponseResult addLink(@RequestBody LinkVo linkVo){
        return linkService.addLink(linkVo);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteLink(@PathVariable("id") String id){
        String[] split = id.split(",");
        for(String s:split){
            linkService.removeByIds(Collections.singleton(s));
        }
        return ResponseResult.okResult();
    }

    @PutMapping
    public ResponseResult update(@RequestBody Link link){
        LambdaUpdateWrapper<Link> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Link::getId,link.getId());

        linkService.updateById(link);
        return ResponseResult.okResult(link);
    }

    @GetMapping("/{id}")
    public ResponseResult updateLink(@PathVariable("id") Long id,Link link){
//        LambdaUpdateWrapper<Link> updateWrapper = new LambdaUpdateWrapper<>();
//        updateWrapper.eq(Link::getId,id);
//
//        linkService.updateById(link);
//        return ResponseResult.okResult(link);
        return ResponseResult.okResult(linkService.getById(id));
    }

    @PutMapping("/changeLinkStatus")
    public ResponseResult updateUserStatus(@RequestBody Link link){
        LambdaUpdateWrapper<Link> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Link::getId,link.getId());

        linkService.updateById(link);
        return ResponseResult.okResult(link);
    }

}
