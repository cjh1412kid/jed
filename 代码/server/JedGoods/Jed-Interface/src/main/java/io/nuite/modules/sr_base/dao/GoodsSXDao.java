package io.nuite.modules.sr_base.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.nuite.modules.sr_base.entity.GoodsSXEntity;
import io.nuite.modules.sr_base.entity.GoodsSXOptionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 0
 *
 * @author admin
 * @email
 * @date 2018-04-11 11:38:09
 */
@Mapper
public interface GoodsSXDao extends BaseMapper<GoodsSXEntity> {
    List<GoodsSXEntity> selectListContainOption(@Param("companySeq") Integer companySeq);

    List<GoodsSXOptionEntity> getGoodsSXOptions(@Param("SXSeq") Integer sxSeq);
}
