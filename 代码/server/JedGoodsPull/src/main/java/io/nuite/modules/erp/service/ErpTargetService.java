package io.nuite.modules.erp.service;

/**
 * @description: ERP拉取指标Service
 * @author: jxj
 * @create: 2019-11-18 14:16
 */

public interface ErpTargetService {
    /**
     * 拉取门店指标
     * @return
     * @throws Exception
     */
    boolean getErpShopTarget() throws Exception;

    /**
     * 拉取店员指标
     * @return
     * @throws Exception
     */
    boolean getErpSalesTarget() throws Exception;
}
