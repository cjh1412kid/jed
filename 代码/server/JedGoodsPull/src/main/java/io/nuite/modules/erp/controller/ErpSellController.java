package io.nuite.modules.erp.controller;

import io.nuite.common.utils.R;
import io.nuite.modules.erp.service.ErpSellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 拉取ERP销售Controller类
 * @author: jxj
 * @create: 2019-05-22 17:37
 */
@RestController
@RequestMapping("/erpSell")
public class ErpSellController {
    @Autowired
    private ErpSellService erpSellService;

    @GetMapping("/getErpSell")
    private R getErpSell(Integer goodsType) {
        try {
            if(erpSellService.getErpSell(goodsType)) {
                return R.ok("数据解析成功并已插入数据库");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return R.error("数据解析失败");
    }
}
