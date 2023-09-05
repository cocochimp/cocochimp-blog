package com.cocochimp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cocochimp.domain.ResponseResult;
import com.cocochimp.domain.entity.Role;
import com.cocochimp.domain.entity.User;
import com.cocochimp.domain.entity.UserRole;
import com.cocochimp.domain.vo.PageVo;
import com.cocochimp.domain.vo.UserDetailVo;
import com.cocochimp.domain.vo.UserInfoVo;
import com.cocochimp.domain.vo.UserListVo;
import com.cocochimp.enums.AppHttpCodeEnum;
import com.cocochimp.exception.SystemException;
import com.cocochimp.mapper.UserMapper;
import com.cocochimp.service.RoleService;
import com.cocochimp.service.UserRoleService;
import com.cocochimp.service.UserService;
import com.cocochimp.utils.BeanCopyUtils;
import com.cocochimp.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-02-16 10:38:20
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    @Override
    public ResponseResult userInfo() {
        //获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询用户信息
        User user = getById(userId);
        //封装成UserInfoVo
        UserInfoVo vo = BeanCopyUtils.copyBean(user,UserInfoVo.class);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        //...
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getAllUserList(Integer pageNum, Integer pageSize, String userName, String phonenumber, Integer status) {
        //查询所有审核通过的
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(StringUtils.hasText(userName), User::getUserName, userName);
        queryWrapper.like(StringUtils.hasText(phonenumber),User::getPhonenumber,phonenumber);
        queryWrapper.like(status!=null,User::getStatus,status);

        Page<User> page = new Page<>(pageNum,pageSize);

        page(page, queryWrapper);

        //转换成vo
        List<UserListVo> userListVos = BeanCopyUtils.copyBeanList(page.getRecords(), UserListVo.class);

        //封装数据返回
        PageVo pageVo = new PageVo(userListVos,page.getTotal());

        //封装返回
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addUser(UserListVo userListVo) {
        User link = BeanCopyUtils.copyBean(userListVo, User.class);
        save(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult userDetail(Long id) {
        UserDetailVo userDetailVo = new UserDetailVo();
        LambdaQueryWrapper<UserRole> Wrapper = new LambdaQueryWrapper<>();
        Wrapper.eq(UserRole::getUserId,id);
        List<UserRole> role = userRoleService.list(Wrapper);
        List<String> roleCollect = role.stream().map(m -> m.getRoleId().toString()).collect(Collectors.toList());
        userDetailVo.setRoleIds(roleCollect);
        //再放入角色列表
        List<Role> roles = roleCollect.stream().map(m -> roleService.getById(Long.valueOf(m))).collect(Collectors.toList());
        userDetailVo.setRoles(roles);
        //放入user信息
        User user = getById(id);
        userDetailVo.setUser(user);
        return ResponseResult.okResult(userDetailVo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName);
        return count(queryWrapper)>0;
    }

    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName,nickName);
        return count(queryWrapper)>0;
    }

}

