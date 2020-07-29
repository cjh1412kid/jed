package io.nuite.modules.sr_base.service;

import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.sr_base.entity.BaseRegionEntity;

import java.util.List;

public interface BaseRegionService extends IService<BaseRegionEntity> {
    List<Integer> nOcategoryList(Integer brand_Seq);
}
