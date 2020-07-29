package io.nuite.modules.order_platform_app.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.nuite.modules.order_platform_app.entity.SaleShoesDetailCopyEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @description: 零售中间表Dao
 * @author: jxj
 * @create: 2019-11-14 8:40
 */
@Mapper
public interface SaleShoesDetailCopyDao extends BaseMapper<SaleShoesDetailCopyEntity> {
    /**
     * 新增销售
     * @param saleShoesDetailCopyEntities
     * @return
     * @throws Exception
     */
    Integer insertSaleShoesDetailCopy(List<SaleShoesDetailCopyEntity> saleShoesDetailCopyEntities) throws Exception;

    /**
     * 删除中间库数据
     * @return
     * @throws Exception
     */
    Integer deleteSaleShoesDetailCopy() throws Exception;
}
