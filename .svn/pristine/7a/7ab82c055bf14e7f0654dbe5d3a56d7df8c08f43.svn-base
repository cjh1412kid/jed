package io.nuite.modules.order_platform_app.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.plugins.Page;

/**
 * 0
 * 
 * @author admin
 * @email 
 * @date 2018-04-11 11:29:58
 */
@Mapper
public interface SaleShoesAnalysisDao {

	
	List<Map<String, Object>> getHqShoesRankList(Page<Map<String, Object>> page,
													@Param("shoesSeqs")String shoesSeqs, 
													@Param("saleTimeStart")Date saleTimeStart,
													@Param("saleTimeEnd")Date saleTimeEnd, 
													@Param("orderBy")Integer orderBy, 
													@Param("orderDir")Integer orderDir);


	List<Map<String, Object>> getShopShoesRankList(Page<Map<String, Object>> page, 
													@Param("shopSeq")Integer shopSeq, 
													@Param("shoesSeqs")String shoesSeqs, 
													@Param("saleTimeStart")Date saleTimeStart, 
													@Param("saleTimeEnd")Date saleTimeEnd, 
													@Param("orderBy")Integer orderBy, 
													@Param("orderDir")Integer orderDir);


	List<Map<String, Object>> getShopRankList(Page<Map<String, Object>> page, 
													@Param("companySeq")Integer companySeq, 
													@Param("saleTimeStart")Date saleTimeStart, 
													@Param("saleTimeEnd")Date saleTimeEnd,
													@Param("orderBy")Integer orderBy, 
													@Param("orderDir")Integer orderDir);
	
	List<Map<String, Object>> getHqShoesAnalysisList(Page<Map<String, Object>> page, 
													@Param("shopSeqs")String shopSeqs, 
													@Param("shoesSeqs")String shoesSeqs, 
													@Param("startPrice")BigDecimal startPrice,
													@Param("endPrice")BigDecimal endPrice,
													@Param("saleTimeStart")Date saleTimeStart, 
													@Param("saleTimeEnd")Date saleTimeEnd, 
													@Param("orderBy")Integer orderBy, 
													@Param("orderDir")Integer orderDir,
													@Param("shopSeq")Integer shopSeq);

	List<Map<String, Object>> getHqShoesAnalysisListByMoney(Page<Map<String, Object>> page, 
													@Param("shopSeqs")String shopSeqs, 
													@Param("shoesSeqs")String shoesSeqs, 
													@Param("startPrice")BigDecimal startPrice,
													@Param("endPrice")BigDecimal endPrice,
													@Param("saleTimeStart")Date saleTimeStart, 
													@Param("saleTimeEnd")Date saleTimeEnd, 
													@Param("orderBy")Integer orderBy, 
													@Param("orderDir")Integer orderDir,
													@Param("shopSeq")Integer shopSeq);
}
