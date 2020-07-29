package io.nuite.modules.sr_base.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.modules.sr_base.dao.GoodsPeriodDao;
import io.nuite.modules.sr_base.entity.GoodsPeriodEntity;
import io.nuite.modules.sr_base.service.GoodsPeriodService;
import org.springframework.stereotype.Service;

@Service
public class GoodsPeriodServiceImpl extends ServiceImpl<GoodsPeriodDao, GoodsPeriodEntity> implements GoodsPeriodService {
}
