package com.cocochimp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cocochimp.domain.ResponseResult;
import com.cocochimp.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2023-02-16 10:06:43
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}

