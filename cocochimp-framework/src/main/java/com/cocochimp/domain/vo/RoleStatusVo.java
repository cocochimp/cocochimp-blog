package com.cocochimp.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleStatusVo {
    //主键@TableId
    private Long roleId;

    //账号状态（0正常 1停用）
    private String status;
}
