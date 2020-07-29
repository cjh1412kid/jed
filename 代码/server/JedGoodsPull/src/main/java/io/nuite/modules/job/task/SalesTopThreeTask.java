package io.nuite.modules.job.task;

import io.nuite.modules.job.service.SalesTopThreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: 销售TOP3定时任务
 * @author: jxj
 * @create: 2019-05-08 09:42
 */
@Component("salesTopThreeTask")
public class SalesTopThreeTask {
    @Autowired
    private SalesTopThreeService salesTopThreeService;

    public void selectSaleGoodsTopThree(){
        try {
            salesTopThreeService.selectSaleGoodsTopThree();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void selectSaleCategoryTopThree(){
        try {
            salesTopThreeService.selectSaleCategoryTopThree();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
