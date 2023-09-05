package com.cocochimp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cocochimp.domain.ResponseResult;
import com.cocochimp.domain.entity.Role;
import com.cocochimp.domain.vo.RoleVo;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-02-17 16:18:05
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult getAllRoleList(Integer pageNum, Integer pageSize, String roleName, Integer status);

    ResponseResult addRole(RoleVo roleVo);

    ResponseResult listAllRole();

}

