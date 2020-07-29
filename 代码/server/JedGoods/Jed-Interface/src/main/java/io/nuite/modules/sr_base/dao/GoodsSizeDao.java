package io.nuite.modules.sr_base.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import io.nuite.modules.sr_base.entity.GoodsSizeEntity;
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
public interface GoodsSizeDao extends BaseMapper<GoodsSizeEntity> {

    Integer selectSizeSeqByCodeAndCompanySeq(@Param("companySeq") Integer companySeq, @Param("code") String sizeCode);

    /**
     * 新增尺码
     * @param goodsSizeEntities
     * @return
     * @throws Exception
     */
    Integer insertGoodsSize(List<GoodsSizeEntity> goodsSizeEntities) throws Exception;
}
