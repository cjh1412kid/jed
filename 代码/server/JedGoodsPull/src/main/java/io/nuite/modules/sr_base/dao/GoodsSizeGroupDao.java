package io.nuite.modules.sr_base.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.nuite.modules.sr_base.entity.GoodsSizeGroupEntity;

import java.util.List;

/**
 * @description: 尺码分组Dao类
 * @author: jxj
 * @create: 2019-05-17 17:47
 */

public interface GoodsSizeGroupDao extends BaseMapper<GoodsSizeGroupEntity> {
    /**
     * 新增尺码分组
     * @param goodsSizeGroupEntities
     * @return
     * @throws Exception
     */
    Integer insertGoodsSizeGroup(List<GoodsSizeGroupEntity> goodsSizeGroupEntities) throws Exception;
}
