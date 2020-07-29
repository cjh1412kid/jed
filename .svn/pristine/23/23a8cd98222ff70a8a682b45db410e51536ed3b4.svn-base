package io.nuite.modules.erp.controller;

import io.nuite.common.utils.R;
import io.nuite.modules.erp.service.ErpSalesService;
import  io.nuite.modules.erp.service.ErpShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 拉取ERP门店Controller类
 * @author: jxj
 * @create: 2019-05-22 16:58
 */
@RestController
@RequestMapping("/erpShop")
public class ErpShopController {
    @Autowired
    private ErpShopService erpShopService;

    @Autowired
    private ErpSalesService erpSalesService;

    @GetMapping("/getErpShop")
    public R getErpShop() {
        try {
            if(erpShopService.getErpShop()) {
                return R.ok("数据解析成功并已插入数据库");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return R.error("数据解析失败");
    }

    @GetMapping("/getErpSales")
    public R getErpSales() {
        try {
            if(erpSalesService.getErpSales()) {
                return R.ok("数据解析成功并已插入数据库");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return R.error("数据解析失败");
    }
}
