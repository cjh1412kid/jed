package io.nuite.modules.system.service;

import io.nuite.common.utils.PageUtils;
import io.nuite.modules.system.entity.SysFactoryEntity;

import java.util.Map;

public interface SystemFactoryService {
    PageUtils queryFactoryPage(Map<String, Object> params);

    SysFactoryEntity queryFactory(Long seq);

    void saveFactory(SysFactoryEntity factoryEntity);

    void deleteFactory(Long seq);
}
