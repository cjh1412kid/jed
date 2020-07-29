package io.nuite.modules.erp.service;

/**
 * @description: 拉取ERP销售Service类
 * @author: jxj
 * @create: 2019-05-21 14:04
 */

public interface ErpSellService {
    /**
     * 拉取ERP销售
     * @return
     * @throws Exception
     */
    boolean getErpSell(Integer goodsType) throws Exception;
}
