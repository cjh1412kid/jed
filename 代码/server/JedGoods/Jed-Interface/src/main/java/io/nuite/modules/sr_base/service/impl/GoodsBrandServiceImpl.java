package io.nuite.modules.sr_base.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.modules.sr_base.dao.GoodsBrandDao;
import io.nuite.modules.sr_base.entity.GoodsBrandEntity;
import io.nuite.modules.sr_base.service.GoodsBrandService;
import org.springframework.stereotype.Service;

@Service
public class GoodsBrandServiceImpl extends ServiceImpl<GoodsBrandDao, GoodsBrandEntity> implements GoodsBrandService {
}
