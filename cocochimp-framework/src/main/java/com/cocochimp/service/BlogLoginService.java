package com.cocochimp.service;

import com.cocochimp.domain.ResponseResult;
import com.cocochimp.domain.entity.User;

public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
