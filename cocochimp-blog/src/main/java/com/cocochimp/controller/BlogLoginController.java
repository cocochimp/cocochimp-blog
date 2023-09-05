package com.cocochimp.controller;

import com.cocochimp.domain.ResponseResult;
import com.cocochimp.domain.entity.User;
import com.cocochimp.enums.AppHttpCodeEnum;
import com.cocochimp.exception.SystemException;
import com.cocochimp.service.BlogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogLoginController {
    @Autowired
    private BlogLoginService blogLoginService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName()))
            //提示必须要传的用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);

        return blogLoginService.login(user);
    }

    @PostMapping("/logout")
    public ResponseResult logout(){
        return blogLoginService.logout();
    }
}
