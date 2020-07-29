package io.nuite.modules.order_platform_app.service;

import java.util.List;
import java.util.Map;

import io.nuite.modules.order_platform_app.entity.ShoesValuateEntity;

public interface GoodsDataService {

	List<Map<String, Object>> getAllEvaluate(Integer seq,Integer start,Integer num);

	ShoesValuateEntity getUserShoesValuate(Integer userSeq, Integer shoesSeq);
	
	void updateShoesValuate(Integer userSeq, Integer shoesSeq, ShoesValuateEntity shoesValuateEntity);

	void addShoesSearchTimes(Integer seq);
	
	void addOrUpdateShoesBrowseRecord(Integer userSeq, Integer shoesSeq);

	Integer getStockQuantity(Integer shoesSeq);
}
