package io.nuite.modules.erp.service;

public interface ErpShoesDataSyncService {
    /**
     * 同步伯俊Erp库存数据，同时计算总进货量
     */
    boolean shoesDataSync(Integer goodsType);

}

