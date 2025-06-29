package com.github.paicoding.module.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.paicoding.module.user.dto.UserSearchParam;
import com.github.paicoding.module.user.entity.User;
import com.github.paicoding.module.user.vo.UserRoleVO;
import io.lettuce.core.dynamic.annotation.Param;

/**
 * @author Zane Leo
 * @date 2025/3/27 00:03
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据角色ID返回用户列表和是否绑定角色的信息
     *
     * @param page   分页参数
     * @param roleId 角色ID
     * @return 用户列表
     */
    IPage<UserRoleVO> selectUsersWithRole(Page<UserRoleVO> page, Long roleId, UserSearchParam searchParams);
}
