package io.nuite.modules.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.modules.sr_base.dao.BaseRoleDao;
import io.nuite.modules.sr_base.entity.BaseRoleEntity;
import io.nuite.modules.system.dao.SystemRoleMenuDao;
import io.nuite.modules.system.entity.SystemRoleMenuEntity;
import io.nuite.modules.system.service.SystemRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色与菜单对应关系
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:44:35
 */
@Service
public class SystemRoleMenuServiceImpl extends ServiceImpl<SystemRoleMenuDao, SystemRoleMenuEntity> implements SystemRoleMenuService {

    @Autowired
    private BaseRoleDao baseRoleDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long roleId, List<Long> menuIdList) {
        //先删除角色与菜单关系
        deleteBatch(new Long[]{roleId});

        if (menuIdList.size() == 0) {
            return;
        }

        //保存角色与菜单关系
        List<SystemRoleMenuEntity> list = new ArrayList<>(menuIdList.size());
        for (Long menuId : menuIdList) {
            SystemRoleMenuEntity sysRoleMenuEntity = new SystemRoleMenuEntity();
            sysRoleMenuEntity.setMenuId(menuId);
            sysRoleMenuEntity.setRoleId(roleId);

            list.add(sysRoleMenuEntity);
        }
        this.insertBatch(list);
    }

    @Override
    public List<Long> queryMenuIdList(Long roleId) {
        return baseMapper.queryMenuIdList(roleId);
    }

    @Override
    public int deleteBatch(Long[] roleIds) {
        return baseMapper.deleteBatch(roleIds);
    }

    @Override
    public List<BaseRoleEntity> selectRoles() {
        return baseRoleDao.selectList(new EntityWrapper<BaseRoleEntity>().notIn("Code", "FACTORY_ADMIN"));
    }

}
