package io.nuite.modules.sr_base.service;

import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.sr_base.entity.SysMenuEntity;

import java.util.List;

public interface SysMenuService extends IService<SysMenuEntity> {
    /**
     * 获取用户菜单列表
     */
    List<SysMenuEntity> getUserMenuList(Long userId);

    /**
     * 根据父菜单，查询子菜单
     *
     * @param parentId   父菜单ID
     * @param menuIdList 用户菜单ID
     * @param wipeAlone  去除alone类型的菜单
     */
    List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList, boolean wipeAlone);

    /**
     * 根据父菜单，查询子菜单
     *
     * @param parentId 父菜单ID
     */
    List<SysMenuEntity> queryListParentId(Long parentId);

    /**
     * 获取不包含按钮的菜单列表
     */
    List<SysMenuEntity> queryNotButtonList();

    /**
     * 删除
     */
    void delete(Long menuId);

    /**
     * 获取授权时使用的列表
     *
     * @param userSeq 用户序号
     */
    List<SysMenuEntity> getAuthorList(Long userSeq);
}
