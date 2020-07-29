package io.nuite.modules.system.service;

import com.baomidou.mybatisplus.service.IService;
import io.nuite.common.utils.PageUtils;
import io.nuite.modules.system.entity.ShoesArrivedEntity;

import java.util.List;
import java.util.Map;

public interface ShoesArrivedService extends IService<ShoesArrivedEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    List<ShoesArrivedEntity> getListByReplenishSeq(Integer replenishSeq);
}

