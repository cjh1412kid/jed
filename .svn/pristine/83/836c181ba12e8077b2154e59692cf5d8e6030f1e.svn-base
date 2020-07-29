package io.nuite.modules.job.task;

import io.nuite.modules.erp.service.TransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: 更新调拨状态定时任务
 * @author: jxj
 * @create: 2019-11-27 10:45
 */
@Component("updateTransferStatusTask")
public class UpdateTransferStatusTask {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TransferService transferService;

    public void updateTransferStatus() {
        try {
            transferService.updateTransferStatus();
            logger.error("更新调拨状态成功");
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            logger.error("更新调拨状态失败");
        }
    }
}
