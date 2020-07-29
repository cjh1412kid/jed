package io.nuite.modules.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import io.nuite.modules.system.entity.SystemUserTokenEntity;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 系统用户Token
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 15:22:07
 */
@Mapper
public interface SystemUserTokenDao extends BaseMapper<SystemUserTokenEntity> {

	SystemUserTokenEntity queryByToken(@Param("token") String token);
	
	SystemUserTokenEntity selectBySeq(@Param("userSeq") Integer userSeq);
	
	Integer updateBySeq(@Param("tokenEntity")SystemUserTokenEntity tokenEntity);
	
	
}
