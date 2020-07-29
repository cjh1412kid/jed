package io.nuite.modules.sr_base.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.nuite.modules.sr_base.entity.GoodsPeriodEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GoodsPeriodDao extends BaseMapper<GoodsPeriodEntity> {
    Integer selectSeqByName(@Param("companySeq") Integer companySeq, @Param("year") String year, @Param("name") String name);
}
