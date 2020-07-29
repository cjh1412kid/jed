package io.nuite.modules.order_platform_app.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.nuite.modules.order_platform_app.entity.ShoesDataCopyEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 0
 *
 * @author admin
 * @email
 * @date 2018-04-11 11:29:58
 */
@Mapper
public interface ShoesDataCopyDao extends BaseMapper<ShoesDataCopyEntity> {
    /**
     * 新增库存
     * @param shoesDataEntities
     * @return
     * @throws Exception
     */
    Integer insertShoesData(List<ShoesDataCopyEntity> shoesDataEntities) throws Exception;

    /**
     * 删除对应公司下所有库存数据
     * @param companySeq
     * @return
     * @throws Exception
     */
    Integer deleteShoesData(@Param("companySeq") Integer companySeq,@Param("goodsType") Integer goodsType) throws Exception;
}
