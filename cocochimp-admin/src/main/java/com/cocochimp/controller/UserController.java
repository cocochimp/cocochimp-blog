package com.cocochimp.controller;

import com.cocochimp.domain.ResponseResult;
import com.cocochimp.domain.entity.User;
import com.cocochimp.domain.vo.UserListVo;
import com.cocochimp.domain.vo.UserStatusVo;
import com.cocochimp.enums.AppHttpCodeEnum;
import com.cocochimp.service.UserService;
import com.cocochimp.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    private UserService UserService;

    @GetMapping("/list")
    public ResponseResult getAllUserList(Integer pageNum, Integer pageSize ,String userName,String phonenumber, Integer status){
        return UserService.getAllUserList(pageNum,pageSize,userName,phonenumber,status);
    }

    @PostMapping
    public ResponseResult addUser(@RequestBody UserListVo userListVo){
        return UserService.addUser(userListVo);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteUser(@PathVariable("id") String id){
        String[] split = id.split(",");
        //得到当前用户id（不能删除当前用户id）
        Long userId = SecurityUtils.getUserId();
        for(String s:split){
            if (s.equals(userId.toString()))
                return ResponseResult.errorResult(AppHttpCodeEnum.NOT_DELETE_NOWUSER.getCode(),AppHttpCodeEnum.NOT_DELETE_NOWUSER.getMsg());
            UserService.removeByIds(Collections.singleton(s));
        }
        return ResponseResult.okResult();
    }

    @PutMapping("/changeStatus")
    public ResponseResult updateUserStatus(@RequestBody UserStatusVo statusVo){
        User user = new User();
        user.setId(statusVo.getUserId());
        user.setStatus(statusVo.getStatus());
        UserService.updateById(user);
        return ResponseResult.okResult(statusVo);
    }

    @GetMapping("{id}")
    private ResponseResult userDetail(@PathVariable Long id){
        return UserService.userDetail(id);
    }

    @PutMapping
    private ResponseResult updateUser(@RequestBody User user){
        return UserService.updateUserInfo(user);
    }

}
