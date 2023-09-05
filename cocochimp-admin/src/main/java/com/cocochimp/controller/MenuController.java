package com.cocochimp.controller;

import com.cocochimp.domain.ResponseResult;
import com.cocochimp.domain.entity.Menu;
import com.cocochimp.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("list")
    public ResponseResult MenuList(String status,String menuName){
        return menuService.menuList(status,menuName);
    }

    @PostMapping
    public ResponseResult InsertList(@RequestBody Menu menu){
        return menuService.InsertList(menu);
    }

    @GetMapping("{id}")
    public ResponseResult selectById(@PathVariable Long id){
        return menuService.selectById(id);
    }

    @PutMapping
    public ResponseResult updateMenu(@RequestBody Menu menu){
        return menuService.updateMenu(menu);
    }

    @DeleteMapping("{id}")
    public ResponseResult deleteMenu(@PathVariable Long id){
        return menuService.deleteMenu(id);
    }

    @GetMapping("treeselect")
    public ResponseResult treeselect(){
        return menuService.treeselect();
    }

    @GetMapping("roleMenuTreeselect/{id}")
    public ResponseResult rolMenuTreeSelect(@PathVariable Long id){
        return menuService.rolMenuTreeSelect(id);
    }

}
