package io.nuite.modules.system.dao;

import io.nuite.modules.system.entity.SysFactoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SystemFactoryDao {
    List<SysFactoryEntity> queryFactoryPage(@Param("limit") int limit, @Param("page") int page, @Param("keywords") Object keywords);

    int queryFactoryTotal(@Param("keywords") Object keywords);

    SysFactoryEntity queryFactoryOne(Long seq);

}
