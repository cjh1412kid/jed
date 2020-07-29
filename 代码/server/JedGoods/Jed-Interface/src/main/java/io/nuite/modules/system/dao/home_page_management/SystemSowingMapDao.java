package io.nuite.modules.system.dao.home_page_management;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;

import io.nuite.modules.system.entity.home_page_management.SystemSowingMapEntity;

@Mapper
public interface SystemSowingMapDao extends BaseMapper<SystemSowingMapEntity> {

	List<Map<String, Object>> selectGoodsList(Page<Map<String, Object>> page, Map<String, Object> condition);

}
