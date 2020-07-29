package io.nuite.modules.order_platform_app.service;

import java.util.List;

import io.nuite.modules.sr_base.entity.BaseUserEntity;

public interface BaseService {
	
	//根据 品牌编号 查询波次编号列表
	List<Integer> getPeriodListByBrandSeq(Integer brandSeq);

	//查询登录用户所属的工厂管理员
	BaseUserEntity getAdminUserByLoginUser(BaseUserEntity loginUser);

	//查询登录用户所属工厂拥有特定权限的工厂用户列表
	List<BaseUserEntity> getUserByPermission(BaseUserEntity loginUser, String[] perms);

}
