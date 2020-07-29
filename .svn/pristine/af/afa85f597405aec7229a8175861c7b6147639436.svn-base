package io.nuite.modules.sr_base.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.common.utils.Constant;
import io.nuite.common.utils.MapUtils;
import io.nuite.modules.sr_base.dao.SysMenuDao;
import io.nuite.modules.sr_base.entity.SysMenuEntity;
import io.nuite.modules.sr_base.service.BaseUserService;
import io.nuite.modules.sr_base.service.SysMenuService;
import io.nuite.modules.sr_base.service.SysUserMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuDao, SysMenuEntity> implements SysMenuService {
    @Autowired
    private BaseUserService baseUserService;

    @Autowired
    private SysUserMenuService sysUserMenuService;

    @Override
    public List<SysMenuEntity> getUserMenuList(Long userSeq) {
        //用户菜单列表
        List<Long> menuIdList = baseUserService.queryAllMenuId(userSeq);
        return getAllMenuList(menuIdList, false, false);
    }

    @Override
    public List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList, boolean wipeAlone) {
        List<SysMenuEntity> menuList = queryListParentId(parentId);
        if (wipeAlone) {
            menuList.removeIf(SysMenuEntity::getAlone);
        }

        if (menuIdList == null) {
            return menuList;
        }

        List<SysMenuEntity> userMenuList = new ArrayList<>();
        for (SysMenuEntity menu : menuList) {
            if (menuIdList.contains(menu.getSeq())) {
                userMenuList.add(menu);
            }
        }
        return userMenuList;
    }

    /**
     * 获取所有菜单列表
     */
    private List<SysMenuEntity> getAllMenuList(List<Long> menuIdList, boolean wipeAlone, boolean containButton) {
        //查询根菜单列表
        List<SysMenuEntity> menuList = queryListParentId(0L, menuIdList, wipeAlone);
        //递归获取子菜单
        getMenuTreeList(menuList, menuIdList, wipeAlone, containButton);

        return menuList;
    }

    /**
     * 递归
     */
    private List<SysMenuEntity> getMenuTreeList(List<SysMenuEntity> menuList, List<Long> menuIdList, boolean wipeAlone, boolean containButton) {
        List<SysMenuEntity> subMenuList = new ArrayList<SysMenuEntity>();

        for (SysMenuEntity entity : menuList) {
            //目录
            boolean deepQuery = !containButton && entity.getType() == Constant.MenuType.CATALOG.getValue();
            deepQuery = deepQuery || (containButton && entity.getType() != Constant.MenuType.BUTTON.getValue());
            if (deepQuery) {
                List<SysMenuEntity> parentList = queryListParentId(entity.getSeq(), menuIdList, wipeAlone);
                List<SysMenuEntity> treeList = getMenuTreeList(parentList, menuIdList, wipeAlone, containButton);
                entity.setList(treeList);
            }
            subMenuList.add(entity);
        }

        return subMenuList;
    }

    @Override
    public List<SysMenuEntity> queryListParentId(Long parentId) {
        return baseMapper.queryListParentId(parentId);
    }

    @Override
    public List<SysMenuEntity> queryNotButtonList() {
        return baseMapper.queryNotButtonList();
    }

    @Override
    public void delete(Long menuId) {
        //删除菜单
        this.deleteById(menuId);
        //todo 删除菜单与用户关联
        sysUserMenuService.deleteByMap(new MapUtils().put("Menu_Seq", menuId));
    }

    @Override
    public List<SysMenuEntity> getAuthorList(Long userSeq) {
        List<SysMenuEntity> menuList;
        if (Constant.SUPER_ADMIN == userSeq) {
            menuList = this.selectList(null);
            for (SysMenuEntity sysMenuEntity : menuList) {
                SysMenuEntity parentMenuEntity = this.selectById(sysMenuEntity.getParentSeq());
                if (parentMenuEntity != null) {
                    sysMenuEntity.setParentName(parentMenuEntity.getName());
                }
            }
        } else {
            List<Long> menuIdList = baseUserService.queryAllMenuId(userSeq);
            menuList = getAllMenuList(menuIdList, true, true);
            changeMenuListTop(menuList);
        }
        return menuList;
    }

    private void changeMenuListTop(List<SysMenuEntity> menuList) {
        List<SysMenuEntity> childList = new ArrayList<SysMenuEntity>();
        for (SysMenuEntity sysMenuEntity : menuList) {
            List<SysMenuEntity> entities = sysMenuEntity.getList();
            if (entities != null && entities.size() > 0) {
                changeMenuListTop(entities);
                childList.addAll(entities);
            }
            sysMenuEntity.setList(null);
        }
        if (childList.size() > 0) {
            menuList.addAll(childList);
        }
    }
}
