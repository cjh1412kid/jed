package io.nuite.modules.system.service;

import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.system.entity.SystemUserTokenEntity;

/**
 * 用户Token
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 15:22:07
 */
public interface SystemUserTokenService extends IService<SystemUserTokenEntity> {

	/**
	 * 生成token
	 * @param userId  用户ID
	 */
	//R createToken(long userId);

	/**
	 * 退出，修改token值
	 * @param userId  用户ID
	 */
	//void logout(long userId);

}
