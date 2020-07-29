package io.nuite.modules.system.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 调价表格数据表单
 *
 * @Author: yangchuang
 * @Date: 2019/4/12 14:14
 * @Version: 1.0
 */

@Data
public class AdjustPriceForm {
    /**
     * 门店名称
     */
    private String shopName;
    
    /**
     * 货号
     */
    private String goodID;
    /**
     * 货号序号
     */
    private Integer shoeSeq;
    
    /**
     * 当前价格
     */
    private BigDecimal currentPrice;
    
    /**
     * 原价
     */
    private BigDecimal previousPrice;
}
