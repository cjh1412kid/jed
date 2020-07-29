package io.nuite.modules.order_platform_app.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.nuite.modules.order_platform_app.entity.SalesEntity;
import io.nuite.modules.system.model.ShopSalesForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author yangchuang
 * @since 2019-03-19
 */

@Mapper
public interface SalesDao extends BaseMapper<SalesEntity> {
    
    /**
     * 查询门店下的所有导购
     *
     * @param shopSeq
     * @return
     */
    List<SalesEntity> listSalesByShopSeq(@Param("shopSeq") Integer shopSeq);

    /**
     * 查询指定公司的所有门店导购信息
     * @param companySeq
     * @param shopSeq
     * @return
     */
    List<ShopSalesForm> listSalesByCompanySeq(@Param("companySeq") Integer companySeq, @Param("shopSeq") Integer shopSeq);

    /**
     * 新增店员
     * @param salesEntities
     * @return
     * @throws Exception
     */
    Integer insertSales(List<SalesEntity> salesEntities) throws Exception;
}
