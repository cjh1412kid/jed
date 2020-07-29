package io.nuite.modules.sr_base.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.modules.system.dao.SystemLogDao;
import io.nuite.modules.system.entity.order_platform.SystemLogEntity;
import io.nuite.modules.system.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: yangchuang
 * @Date: 2018/8/29 14:29
 * @Version: 1.0
 * @Description:
 */
@Service
public class SystemLogServiceImpl extends ServiceImpl<SystemLogDao, SystemLogEntity> implements SystemLogService {

    @Autowired
    SystemLogDao systemLogDao;

    @Override
    public List<SystemLogEntity> getLogList(Integer start, Integer num) {
        return systemLogDao.getLogListByStartAndNum(start,num);
    }
}
