package com.cocochimp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cocochimp.domain.ResponseResult;
import com.cocochimp.domain.entity.Link;
import com.cocochimp.domain.vo.LinkVo;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-02-14 16:44:42
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult getAllLinkList(Integer pageNum, Integer pageSize,String name, Integer status);

    ResponseResult addLink(LinkVo linkVo);
}

