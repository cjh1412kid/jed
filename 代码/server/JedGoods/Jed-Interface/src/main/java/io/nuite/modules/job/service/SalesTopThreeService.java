package io.nuite.modules.job.service;

import java.util.Map;

/**
 * @description: 销售TOP3Service类
 * @author: jxj
 * @create: 2019-05-07 20:14
 */

public interface SalesTopThreeService {

    /**
     * 获取昨天销售前三货品信息
     * @return
     * @throws Exception
     */
    void selectSaleGoodsTopThree() throws Exception;

    /**
     * 获取昨天销售前三分类信息
     * @return
     * @throws Exception
     */
    void selectSaleCategoryTopThree() throws Exception;
}
