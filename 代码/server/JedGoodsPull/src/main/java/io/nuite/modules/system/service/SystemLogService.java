package io.nuite.modules.system.service;

import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.system.entity.order_platform.SystemLogEntity;

import java.util.List;

/**
 * @Author: yangchuang
 * @Date: 2018/8/29 14:28
 * @Version: 1.0
 * @Description:
 */
public interface SystemLogService extends IService<SystemLogEntity> {

    /**
     * @param start 起始条数
     * @param num   总条数
     * @return
     */
    List<SystemLogEntity> getLogList(Integer start, Integer num);
}
