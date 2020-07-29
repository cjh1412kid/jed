package io.nuite.modules.sr_base.service;

import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.sr_base.entity.SysUserMenuEntity;

import java.util.List;

public interface SysUserMenuService extends IService<SysUserMenuEntity> {
    void updateUserMenu(Long userSeq, List<Long> menuIdList);
}
