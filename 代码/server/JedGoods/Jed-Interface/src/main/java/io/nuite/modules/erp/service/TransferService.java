package io.nuite.modules.erp.service;

import io.nuite.modules.order_platform_app.entity.ShoesDataDailyDetailCopyEntity;

import java.util.List;

/**
 * @description: 更新调拨状态Service
 * @author: jxj
 * @create: 2019-11-26 13:24
 */

public interface TransferService {
    /**
     * 更新调拨状态
     * @throws Exception
     */
    void updateTransferStatus() throws Exception;
}
