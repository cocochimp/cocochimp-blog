package com.cocochimp.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MenuTreeVo {
    private Long id;
    private Long parentId;
    private String menuName;
    private List<MenuTreeVo> children;
}
