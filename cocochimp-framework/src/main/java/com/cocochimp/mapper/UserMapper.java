package com.cocochimp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cocochimp.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2023-02-14 17:00:03
 */
public interface UserMapper extends BaseMapper<User> {

}

