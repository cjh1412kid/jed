package io.nuite.modules.erp.service;

/**
 * @description: 拉取ERP零售Service类
 * @author: jxj
 * @create: 2019-05-22 15:57
 */

public interface ErpSaleService {
    /**
     * 拉取ERP零售
     * @return
     * @throws Exception
     */
    boolean getErpSale(Integer goodsType) throws Exception;
}
