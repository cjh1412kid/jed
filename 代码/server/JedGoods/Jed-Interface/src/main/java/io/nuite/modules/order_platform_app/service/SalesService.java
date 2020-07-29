package io.nuite.modules.order_platform_app.service;

import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.order_platform_app.entity.SalesEntity;
import io.nuite.modules.system.model.ShopSalesForm;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author yangchuang
 * @since 2019-03-19
 */
public interface SalesService extends IService<SalesEntity> {
    
    List<Map<String, Object>> querySalesByShopSeq(Integer shopSeq);

    /**
     * 查询指定公司的所有门店导购信息
     * @param companySeq
     * @param shopSeq
     * @return
     */
    List<ShopSalesForm> querySalesByCompanySeq(Integer companySeq,Integer shopSeq);

    /**
     * 查询指定公司的所有门店导购信息
     * @param salesEntity
     * @return
     * @throws Exception
     */
    boolean insertSales(SalesEntity salesEntity) throws Exception;
}
