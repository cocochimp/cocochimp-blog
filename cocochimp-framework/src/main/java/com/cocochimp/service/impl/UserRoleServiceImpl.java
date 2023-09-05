package com.cocochimp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cocochimp.domain.entity.UserRole;
import com.cocochimp.mapper.UserRoleMapper;
import com.cocochimp.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2023-02-21 12:43:12
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}

