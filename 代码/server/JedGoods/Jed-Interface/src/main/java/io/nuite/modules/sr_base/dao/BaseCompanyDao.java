package io.nuite.modules.sr_base.dao;

import io.nuite.modules.sr_base.entity.BaseCompanyEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 0
 * 
 * @author admin
 * @email 
 * @date 2018-04-11 11:38:09
 */
@Mapper
public interface BaseCompanyDao extends BaseMapper<BaseCompanyEntity> {

    /**
     * 根据创建者seq 和 公司名 判断是否存在
     */
    BaseCompanyEntity selectOne(@Param("userSeq")long userSeq, @Param("companyName")String companyName);

   
	
}
