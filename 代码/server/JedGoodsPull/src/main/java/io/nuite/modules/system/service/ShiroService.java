package io.nuite.modules.system.service;

import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.system.entity.SystemUserTokenEntity;

import java.util.Set;

/**
 * shiro相关接口
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-06 8:49
 */
public interface ShiroService {
    /**
     * 获取用户权限列表
     */
    Set<String> getUserPermissions(long userId);

    SystemUserTokenEntity queryByToken(String token);

    /**
     * 根据用户ID，查询用户
     * @param userId
     */
    BaseUserEntity queryUser(Long userId);
}
