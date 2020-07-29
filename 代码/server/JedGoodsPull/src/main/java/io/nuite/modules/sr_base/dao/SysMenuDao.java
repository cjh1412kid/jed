package io.nuite.modules.sr_base.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.nuite.modules.sr_base.entity.SysMenuEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysMenuDao extends BaseMapper<SysMenuEntity> {
    /**
     * 根据父菜单，查询子菜单
     *
     * @param  父菜单ID
     */
    List<SysMenuEntity> queryListParentId(Long parentId);

    /**
     * 获取不包含按钮的菜单列表
     */
    List<SysMenuEntity> queryNotButtonList();
}
