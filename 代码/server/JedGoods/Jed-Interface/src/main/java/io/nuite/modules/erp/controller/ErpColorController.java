package io.nuite.modules.erp.controller;

import io.nuite.common.utils.R;
import io.nuite.modules.erp.service.ErpColorService;
import io.nuite.modules.sr_base.service.GoodsColorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 拉取ERP颜色Controller层
 * @author: jxj
 * @create: 2019-05-17 09:33
 */
@RestController
@RequestMapping("/erpColor")
public class ErpColorController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ErpColorService erpColorService;

    @GetMapping("/getErpColor")
    public R getErpColor() {
        try {
            if(erpColorService.getErpColor()) {
                return R.ok("数据解析成功并已插入数据库");
            }
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
            e.printStackTrace();
        }
        return R.error("数据解析失败");
    }
}
