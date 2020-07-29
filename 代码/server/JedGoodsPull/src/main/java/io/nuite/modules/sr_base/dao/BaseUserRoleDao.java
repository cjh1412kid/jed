package io.nuite.modules.sr_base.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.nuite.modules.sr_base.entity.BaseRoleEntity;
import io.nuite.modules.sr_base.entity.BaseUserRoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: yangchuang
 * @Date: 2019/3/8 11:06
 * @Version: 1.0
 */

@Mapper
public interface BaseUserRoleDao extends BaseMapper<BaseUserRoleEntity> {

    /**
     * 判断用户是否具有某个角色
     *
     * @param userSeq
     * @param roleCode
     * @return
     */
    List<BaseUserRoleEntity> selectByUserSeqAndRoleCode(@Param("userSeq") Integer userSeq, @Param("roleCode") String roleCode);

    /**
     * 查询用户的第一个角色
     *
     * @param userSeq
     * @return
     */
    BaseRoleEntity selectRoleByUserSeq(@Param("userSeq") Integer userSeq);
    
    
    /**
     * 查询用户所有的角色
     *
     * @param userSeq
     * @return
     */
    List<BaseUserRoleEntity> selectByUserSeq(@Param("userSeq") Integer userSeq);
    
}
