package io.nuite.modules.sr_base.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.common.utils.MapUtils;
import io.nuite.modules.sr_base.dao.SysUserMenuDao;
import io.nuite.modules.sr_base.entity.SysUserMenuEntity;
import io.nuite.modules.sr_base.service.SysUserMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserMenuServiceImpl extends ServiceImpl<SysUserMenuDao, SysUserMenuEntity> implements SysUserMenuService {

    @Autowired
    private SysUserMenuDao sysUserMenuDao;

    @Override
    public void updateUserMenu(Long userSeq, List<Long> menuIdList) {
        this.deleteByMap(new MapUtils().put("User_Seq", userSeq));

        if (menuIdList == null || menuIdList.size() == 0) {
            return;
        }

        //保存用户与角色关系
        sysUserMenuDao.insertList(userSeq, menuIdList);
    }
}
