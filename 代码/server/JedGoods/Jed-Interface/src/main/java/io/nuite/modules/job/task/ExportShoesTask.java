package io.nuite.modules.job.task;

import io.nuite.modules.job.service.ExportShoesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: 滞销款调出定时任务
 * @author: jxj
 * @create: 2019-12-02 16:13
 */
@Component("exportShoesTask")
public class ExportShoesTask {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ExportShoesService exportShoesService;

    public void exportShoes() {
        try {
            exportShoesService.exportShoes();
            logger.error("调出成功");
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            logger.error("调出失败");
        }
    }

    public void updateManualShoesNum() {
        try {
            exportShoesService.updateManualShoesNum();
            logger.error("修改手动调出数量成功");
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            logger.error("修改手动调出数量失败");
        }
    }

    public void deleteManualShoesNotThisYear() {
        try {
            exportShoesService.deleteManualShoesNotThisYear();
            logger.error("删除手动调出不是今年货品成功");
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            logger.error("删除手动调出不是今年货品失败");
        }
    }
}
