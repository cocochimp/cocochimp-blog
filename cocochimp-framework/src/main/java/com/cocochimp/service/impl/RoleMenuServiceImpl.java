package com.cocochimp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cocochimp.domain.entity.RoleMenu;
import com.cocochimp.mapper.RoleMenuMapper;
import com.cocochimp.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * 角色和菜单关联表(RoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2023-02-21 12:40:25
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

}

