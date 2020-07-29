package io.nuite.modules.order_platform_app.dao;

import com.baomidou.mybatisplus.plugins.Page;
import io.nuite.modules.order_platform_app.entity.ShoesValuateEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 0
 * 
 * @author admin
 * @email 
 * @date 2018-04-11 11:29:58
 */
@Mapper
public interface ShoesValuateDao extends BaseMapper<ShoesValuateEntity> {

	List<Map<String, Object>> getShoesBySeqs(@Param("shoesSeqs")String shoesSeqs);

	/**
	 * 获取浏览记录
	 * @param page
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getBrowseList(Page<ShoesValuateEntity> page, Map<String,Object> map);

	/**
	 * 获取收藏
	 * @param page
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getCollectedList(Page<ShoesValuateEntity> page, Map<String,Object> map);
}
