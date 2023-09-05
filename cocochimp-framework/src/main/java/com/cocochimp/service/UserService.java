package com.cocochimp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cocochimp.domain.ResponseResult;
import com.cocochimp.domain.entity.User;
import com.cocochimp.domain.vo.UserListVo;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-02-16 10:38:19
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult register(User user);

    ResponseResult getAllUserList(Integer pageNum, Integer pageSize, String userName, String phonenumber, Integer status);

    ResponseResult addUser(UserListVo userListVo);

    ResponseResult userDetail(Long id);

    ResponseResult updateUserInfo(User user);

}

