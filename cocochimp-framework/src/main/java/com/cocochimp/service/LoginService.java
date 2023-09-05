package com.cocochimp.service;

import com.cocochimp.domain.ResponseResult;
import com.cocochimp.domain.entity.User;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();

}
