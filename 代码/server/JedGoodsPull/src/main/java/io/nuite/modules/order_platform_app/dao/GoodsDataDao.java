package io.nuite.modules.order_platform_app.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface GoodsDataDao {
	/**
	 * 返回平均分
	 */
	Float getAvgScore(@Param("Seq") Integer seq);

	/**
	 * 	鞋子总订货量
	 */
	Integer getOrderQuantity(@Param("Seq") Integer seq);

	/**
	 * 添加鞋子搜索次数
	 * @param seq
	 */
	void addShoesSearchTimes(@Param("seq") Integer seq);


	/**
	 * 返回该商品的所有评价
	 */
	List<Map<String, Object>> getAllEvaluate(@Param("Seq") Integer seq, @Param("start") Integer start, @Param("num") Integer num);

}
