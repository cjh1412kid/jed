package io.nuite.modules.job.controller;

import io.nuite.common.utils.R;
import io.nuite.modules.job.service.ErpService;
import io.nuite.modules.sr_base.service.BaseShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: ERP数据拉取Controller类
 * @author: jxj
 * @create: 2019-04-03 13:56
 */
@RestController
@RequestMapping("/erp")
public class ErpController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ErpService service;
    @Autowired
    private BaseShopService shopService;

    @Value("${jrd.companySeq}")
    private Integer companySeq;

    @GetMapping("/getErpData")
    public R getEprData(@RequestParam String startDate, @RequestParam String endDate, String lastupdate) {
        try {
            if (service.getErpData(startDate,endDate,lastupdate)) {
                return R.ok("数据解析成功并已插入数据库");
            }
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
            e.printStackTrace();
        }
        return R.error("数据解析失败");
    }

    @GetMapping("/getShopStock")
    public R getShopStock() {
        try {
            if(service.getShopStock()) {
                return R.ok("数据解析成功并已插入数据库");
            }
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
            e.printStackTrace();
        }
        return R.error("数据解析失败");
    }
}
