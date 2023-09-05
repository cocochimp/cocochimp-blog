package com.cocochimp.domain.vo;

import com.cocochimp.domain.entity.Role;
import com.cocochimp.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailVo {
    private List<String> roleIds;
    private List<Role> roles;
    private User user;
}
