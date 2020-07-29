package io.nuite.modules.sr_base.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author admin
 * @email
 * @date 2018-04-11 11:38:09
 */
@Mapper
public interface BaseUserDao extends BaseMapper<BaseUserEntity> {

    /**
     * 根据seqs获取多个用户的基础信息(名字、公司、品牌、头像)Map
     */
    List<Map<String, Object>> getBaseUserInfoBySeqs(@Param("userSeqs") String userSeqs);

    /**
     * 根据用户名，查询系统用户 只查询后台用户：工厂账号或者工厂子账号
     */
    BaseUserEntity queryByUserName(@Param("username") String username);

    /**
     * 查询用户的所有权限
     *
     * @param userId 用户ID
     */
    List<String> queryAllPerms(Long userId);

    /**
     * 查询用户的所有菜单ID
     */
    List<Long> queryAllMenuId(Long userSeq);


    BaseUserEntity selectUserByCompanyAndAccountName(@Param("accountName") String accountName, @Param("companySeq") Integer companySeq);

    void updateUserBySeq(BaseUserEntity baseUserEntity);

    List<BaseUserEntity> selectBySeqList(@Param("companySeq") Integer companySeq,
                                         @Param("start") Integer start,
                                         @Param("num") Integer num);

    Integer getTotalCount(@Param("companySeq") Integer companySeq);

    List<BaseUserEntity> selectUserPage(Page<BaseUserEntity> page, Map<String, Object> map);

    BaseUserEntity selectAdminUser(@Param("companySeq") Integer companySeq, @Param("brandSeq") Integer brandSeq);
    
    BaseUserEntity selectUserByAccountName(@Param("accountName") String accountName);

    /**
     * 获取门店用户
     * @param page
     * @param map
     * @return
     * @throws Exception
     */
    List<BaseUserEntity> selectShopUser(Page<BaseUserEntity> page,Map<String,Object> map) throws Exception;
}
