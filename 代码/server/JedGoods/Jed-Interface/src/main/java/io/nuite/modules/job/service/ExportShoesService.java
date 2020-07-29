package io.nuite.modules.job.service;

/**
 * @description: 滞销款调出Service
 * @author: jxj
 * @create: 2019-12-02 14:50
 */

public interface ExportShoesService {
    /**
     * 各门店滞销前10名自动加入虚拟商城
     * @throws Exception
     */
    void exportShoes() throws Exception;

    /**
     * 手动调出货品如果库存数量小于调出数量修改调出数量为库存的数量
     * @throws Exception
     */
    void updateManualShoesNum() throws Exception;

    /**
     * 删除手动调出货品中不是今年的货品
     * @throws Exception
     */
    void deleteManualShoesNotThisYear() throws Exception;
}
