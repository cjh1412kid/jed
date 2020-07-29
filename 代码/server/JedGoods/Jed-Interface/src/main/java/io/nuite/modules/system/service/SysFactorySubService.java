package io.nuite.modules.system.service;

import io.nuite.common.utils.PageUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.sr_base.entity.BaseUserEntity;

import java.util.List;
import java.util.Map;

public interface SysFactorySubService {
    PageUtils subAccountList(BaseUserEntity baseUserEntity, Map<String, Object> params);

    R querySubFactory(Long seq);

    void newSubAccount(BaseUserEntity baseUserEntity, List<Long> menus, BaseUserEntity createUser);

    void updateSubAccount(BaseUserEntity baseUserEntity, List<Long> menus, BaseUserEntity createUser);

    void delSubAccount(Long seq, BaseUserEntity createUser);
}
