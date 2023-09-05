package com.cocochimp.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cocochimp.domain.ResponseResult;
import com.cocochimp.domain.entity.Role;
import com.cocochimp.domain.vo.RoleStatusVo;
import com.cocochimp.domain.vo.RoleVo;
import com.cocochimp.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public ResponseResult getAllRoleList(Integer pageNum, Integer pageSize,String roleName,Integer status){
        return roleService.getAllRoleList(pageNum,pageSize,roleName,status);
    }

    @PostMapping
    public ResponseResult addRole(@RequestBody RoleVo roleVo){
        return roleService.addRole(roleVo);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteRole(@PathVariable("id") String id){
//        String[] split = id.split(",");
//        for(String s:split){
//            roleService.removeByIds(Collections.singleton(s));
//        }
//        return ResponseResult.okResult();
        return ResponseResult.okResult(roleService.getById(id));
    }

    @PutMapping
    public ResponseResult update(@RequestBody Role role){
        roleService.updateById(role);
        return ResponseResult.okResult(role);
    }

    @GetMapping("/{id}")
    public ResponseResult updateRole(@PathVariable("id") Long id,Role role){
        LambdaUpdateWrapper<Role> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Role::getRoleName,id);

        roleService.updateById(role);
        return ResponseResult.okResult(role);
    }

    @GetMapping("listAllRole")
    public ResponseResult listAllRole(){
        return  roleService.listAllRole();
    }

    @PutMapping("/changeStatus")
    public ResponseResult updateUserStatus(@RequestBody RoleStatusVo roleStatusVo){
        Role role = new Role();
        role.setId(roleStatusVo.getRoleId());
        role.setStatus(roleStatusVo.getStatus());
        roleService.updateById(role);
        return ResponseResult.okResult(roleStatusVo);
    }

}
