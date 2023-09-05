package com.cocochimp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cocochimp.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2023-02-17 16:18:04
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(Long userId);
}

