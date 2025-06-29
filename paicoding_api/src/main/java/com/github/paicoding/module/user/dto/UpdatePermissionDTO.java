package com.github.paicoding.module.user.dto;

import lombok.Data;

/**
 * @author Zane Leo
 * @date 2025/6/7 09:19
 */
@Data
public class UpdatePermissionDTO {

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 权限ID
     */
    private Long permissionId;


}
