package io.nuite.modules.sr_base.service;

import com.baomidou.mybatisplus.service.IService;

import io.nuite.modules.sr_base.entity.BaseUserRoleEntity;

/**
 * @Author: yangchuang
 * @Date: 2019/3/11 10:27
 * @Version: 1.0
 */

public interface BaseUserRoleService extends IService<BaseUserRoleEntity> {

    /**
     * 根据用户序号查询用户的管理员角色对象
     *
     * @param userSeq
     * @return
     */
    BaseUserRoleEntity findAdminByUserSeq(Integer userSeq);

    /**
     * 判断是否是管理员角色用户
     *
     * @param userSeq
     * @return
     */
    boolean isAdminUser(Integer userSeq);

	String getRoleCodeByUserSeq(Integer userSeq);
}
