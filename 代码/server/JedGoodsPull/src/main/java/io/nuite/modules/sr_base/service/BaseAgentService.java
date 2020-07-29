package io.nuite.modules.sr_base.service;

import com.baomidou.mybatisplus.service.IService;
import io.nuite.common.utils.PageUtils;
import io.nuite.modules.sr_base.entity.BaseAgentEntity;

public interface BaseAgentService extends IService<BaseAgentEntity> {

    PageUtils getLsit(Integer brandSeq, Integer page, Integer limit);

    BaseAgentEntity edit(Integer seq);

    Integer delete(Integer seq);

    int save(BaseAgentEntity baseAgentEntity);

    int update(BaseAgentEntity baseAgentEntity);
}
