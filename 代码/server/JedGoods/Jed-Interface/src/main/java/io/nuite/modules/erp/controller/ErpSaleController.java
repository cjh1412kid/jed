package io.nuite.modules.erp.controller;

import io.nuite.common.utils.R;
import io.nuite.modules.erp.service.ErpSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 拉取ERP零售Controller类
 * @author: jxj
 * @create: 2019-05-23 14:16
 */
@RestController
@RequestMapping("/erpSale")
public class ErpSaleController {
    @Autowired
    private ErpSaleService erpSaleService;

    @GetMapping("/getErpSale")
    public R getErpSale(Integer goodsType) {
        try {
            if(erpSaleService.getErpSale(goodsType)) {
                return R.ok("数据解析成功并已插入数据库");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return R.error("数据解析失败");
    }
}
