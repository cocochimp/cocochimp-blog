package com.cocochimp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cocochimp.constants.SystemConstants;
import com.cocochimp.domain.ResponseResult;
import com.cocochimp.domain.entity.Menu;
import com.cocochimp.domain.entity.RoleMenu;
import com.cocochimp.domain.vo.MenuTreeVo;
import com.cocochimp.enums.AppHttpCodeEnum;
import com.cocochimp.mapper.MenuMapper;
import com.cocochimp.service.MenuService;
import com.cocochimp.service.RoleMenuService;
import com.cocochimp.utils.BeanCopyUtils;
import com.cocochimp.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-02-17 16:12:59
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理员，返回所有的权限: id == 1L
        if(SecurityUtils.isAdmin()){
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType, SystemConstants.MENU,SystemConstants.BUTTON);
            wrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(wrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        //否则返回所具有的权限
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        //判断是否是管理员
        if(SecurityUtils.isAdmin()){
            //如果是 获取所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        }else{
            //否则  获取当前用户所具有的Menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }

        //构建tree
        //先找出第一层的菜单  然后去找他们的子菜单设置到children属性中
        List<Menu> menuTree = builderMenuTree(menus,0L);
        return menuTree;
    }

    private List<Menu> builderMenuTree(List<Menu> menus, Long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    /**
     * 获取存入参数的 子Menu集合
     * @param menu
     * @param menus
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }

    @Override
    public ResponseResult menuList(String status, String menuName) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(!(menuName==""||menuName==null),Menu::getMenuName,menuName);
        wrapper.eq(!(status==""||status==null),Menu::getStatus,status);
        wrapper.orderByAsc(Menu::getParentId,Menu::getOrderNum);
        List<Menu> list = list(wrapper);
        return ResponseResult.okResult(list);
    }

    @Override
    public ResponseResult InsertList(Menu menu) {
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult selectById(Long id) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getId,id);
        List<Menu> menu = list(wrapper);
        return ResponseResult.okResult(menu);
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        if (menu.getId().equals(menu.getParentId())){//把父菜单设置为当前菜单
            return ResponseResult.errorResult(AppHttpCodeEnum.PARENT_ID_NOT_BE_ID.getCode(),AppHttpCodeEnum.PARENT_ID_NOT_BE_ID.getMsg());
        }
        else {
            updateById(menu);
            return ResponseResult.okResult();
        }
    }

    @Override
    public ResponseResult deleteMenu(Long id) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId,id);
        List<Menu> list = list(wrapper);
        if (list.isEmpty()){
            removeById(id);
            return ResponseResult.okResult();
        }
        else {
            int code = AppHttpCodeEnum.HAVE_CHILDREN.getCode();
            String msg = AppHttpCodeEnum.HAVE_CHILDREN.getMsg();
            return ResponseResult.errorResult(code,msg);
        }
    }

    @Override
    public ResponseResult treeselect() {
        List<Menu> menus = list();//先获得所有的Menu
        List<MenuTreeVo> menuTreeVo = BeanCopyUtils.copyBeanList(menus,MenuTreeVo.class);
        //构建tree
        //先找出第一层的菜单  然后去找他们的子菜单设置到children属性中
        List<MenuTreeVo> menuTree = builderMenuTree2(menuTreeVo,0L);
        return ResponseResult.okResult(menuTree);
    }

    @Override
    public ResponseResult rolMenuTreeSelect(Long id) {
        //先从role_menu表中找到关系
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper();
        wrapper.eq(RoleMenu::getRoleId,id);
        List<RoleMenu> MenuList = roleMenuService.list(wrapper);
        //根据MenuId找到menu信息
        List<Menu> Menu = MenuList.stream().map(m -> getById(m.getMenuId()))
                .collect(Collectors.toList());
        //转为要求响应实体
        List<MenuTreeVo> menuTreeVos = BeanCopyUtils.copyBeanList(Menu, MenuTreeVo.class);
        //构建tree
        //先找出第一层的菜单  然后去找他们的子菜单设置到children属性中
        List<MenuTreeVo> menuTree = builderMenuTree2(menuTreeVos,0L);
        return ResponseResult.okResult(menuTree);
    }

    private List<MenuTreeVo> builderMenuTree2(List<MenuTreeVo> menus, Long parentId) {
        List<MenuTreeVo> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren2(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    //递归：获取传入参数的子Meun
    private List<MenuTreeVo> getChildren2(MenuTreeVo menu, List<MenuTreeVo> menus) {
        List<MenuTreeVo> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m->m.setChildren(getChildren2(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }

}

