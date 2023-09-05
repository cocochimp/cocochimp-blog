package com.cocochimp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cocochimp.constants.SystemConstants;
import com.cocochimp.domain.ResponseResult;
import com.cocochimp.domain.entity.Role;
import com.cocochimp.domain.vo.PageVo;
import com.cocochimp.domain.vo.RoleVo;
import com.cocochimp.mapper.RoleMapper;
import com.cocochimp.service.RoleService;
import com.cocochimp.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-02-17 16:18:05
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员 如果是返回集合中只需要有admin
        if(id == 1L){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        //否则查询用户所具有的角色信息
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult getAllRoleList(Integer pageNum, Integer pageSize, String roleName, Integer status) {
        //查询所有审核通过的
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(StringUtils.hasText(roleName),Role::getRoleName, roleName);
        queryWrapper.like(status!=null,Role::getStatus,status);

        Page<Role> page = new Page<>(pageNum,pageSize);

        page(page,queryWrapper);

        //转换成vo
        List<RoleVo> roleVos = BeanCopyUtils.copyBeanList(page.getRecords(), RoleVo.class);

        //封装数据返回
        PageVo pageVo = new PageVo(roleVos,page.getTotal());

        //封装返回
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addRole(RoleVo roleVo) {
        Role link = BeanCopyUtils.copyBean(roleVo, Role.class);
        save(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllRole() {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getStatus, SystemConstants.STATUS_NORMAL);
        List<Role> list = list(wrapper);
        return ResponseResult.okResult(list);
    }
}

