package io.nuite.modules.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.nuite.modules.system.entity.ShoesReplenishEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 * 
 * @author yangchuang
 * @email ychuang92@163.com
 * @date 2019-04-29 10:02:01
 */
@Mapper
public interface ShoesReplenishDao extends BaseMapper<ShoesReplenishEntity> {
	/**
	 * 获取补单到货时间
	 * @param page
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getReplenishList(@Param("shoesSeq") Integer shoesSeq);

	
	List<Map<String, Object>> getReplenishListByTime(@Param("inputTime") String replenishTime, @Param("companySeq") Integer companySeq);

	/**
	 * 获取补单列表
	 * @return
	 */
	List<ShoesReplenishEntity> getSupplementList(@Param("companySeq") Integer companySeq);
	
	List<ShoesReplenishEntity> getHaveSupplementList(@Param("companySeq") Integer companySeq);
	
	List<ShoesReplenishEntity> getStoresSupplementList(@Param("shopSeq") Integer shopSeq);
	
	List<ShoesReplenishEntity> getStoresHaveSupplementList(@Param("shopSeq") Integer shopSeq);

	List<String> getDaysList(@Param("companySeq") Integer companySeq);
	
	List<Map<String, Object>> getReplenishListByTime(Page<Map<String, Object>> page, @Param("inputTime") String replenishTime, @Param("companySeq") Integer companySeq);

	List<Map<String, Object>> getAllReplenishList(Page<Map<String, Object>> page, @Param("companySeq") Integer companySeq);
	
	List<Map<String, Object>> getAllReplenishList();

	/**
	 * 获取所有补过单的货品序号
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getReplenishSeqList(Map<String, Object> map) throws Exception;
	
	List<Map<String, Object>> getGoodsSeqs(@Param("years") String years, @Param("seasonSeqs") String seasonSeqs, @Param("categorySeqs") String categorySeqs, @Param("companySeq") Integer companySeq);

	Map<String, Object> getTotalNum(@Param("shoesSeq") Integer shoesSeq);
}
