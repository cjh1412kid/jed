package io.nuite.modules.sr_base.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.nuite.modules.sr_base.entity.SysUserMenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserMenuDao extends BaseMapper<SysUserMenuEntity> {
    void insertList(@Param("userSeq") Long userSeq, @Param("menuIdList") List<Long> menuIdList);
}
