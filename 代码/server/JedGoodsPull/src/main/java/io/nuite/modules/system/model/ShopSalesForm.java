package io.nuite.modules.system.model;

import io.nuite.modules.order_platform_app.entity.SalesEntity;
import lombok.Data;

import java.util.List;

/**
 * 门店销售员封装类
 *
 * @Author: yangchuang
 * @Date: 2019/4/11 10:47
 * @Version: 1.0
 */

@Data
public class ShopSalesForm {
    
    Integer shopSeq;
    
    String shopName;
    
    List<SalesEntity> sales;
}
