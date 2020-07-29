package io.nuite.modules.order_platform_app.service;

import io.nuite.modules.sr_base.entity.BaseUserEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SaleShoesAnalysisService{
	
	List<Map<String, Object>> getHqShoesRankList(List<Integer> shoesSeqs, Date saleTimeStart, Date saleTimeEnd, Integer orderBy, Integer orderDir, Integer start,
                                                 Integer num);

	List<Map<String, Object>> getShopShoesRankList(Integer shopSeq, List<Integer> shoesSeqs, Date saleTimeStart, Date saleTimeEnd, Integer orderBy, Integer orderDir,
                                                   Integer start, Integer num);

	List<Map<String, Object>> getShopRankList(Integer companySeq, Date saleTimeStart, Date saleTimeEnd, Integer orderBy,
                                              Integer orderDir, Integer start, Integer num);

	
	
	
	
	Integer getHqTotalSaleNum(List<Integer> shoesSeqList, Date saleTimeStart, Date saleTimeEnd);

	BigDecimal getHqTotalAvgSalePrice(List<Integer> shoesSeqList, Date saleTimeStart, Date saleTimeEnd);

	Integer getHqTotalInNum(List<Integer> shoesSeqList, Date saleTimeStart, Date saleTimeEnd);

	Integer getHqTotalStockNum(List<Integer> shoesSeqList);

	Integer getShopTotalSaleNum(Integer shopSeq, List<Integer> shoesSeqList, Date saleTimeStart, Date saleTimeEnd);

	BigDecimal getShopTotalAvgSalePrice(Integer shopSeq, List<Integer> shoesSeqList, Date saleTimeStart, Date saleTimeEnd);

	Integer getShopTotalInNum(Integer shopSeq, List<Integer> shoesSeqList, Date saleTimeStart, Date saleTimeEnd);

	Integer getShopTotalStockNum(Integer shopSeq, List<Integer> shoesSeqList);
	
	Integer getTotalSaleNum(List<Integer> shopSeqList, List<Integer> shoesSeqList, Date saleTimeStart, Date saleTimeEnd, BigDecimal startPrice, BigDecimal endPrice);

	BigDecimal getTotalSalePrice(List<Integer> shopSeqList, List<Integer> shoesSeqList, Date saleTimeStart, Date saleTimeEnd, BigDecimal startPrice, BigDecimal endPrice);
	
	Integer getTotalSaleGood(List<Integer> shopSeqList, List<Integer> shoesSeqList, Date saleTimeStart, Date saleTimeEnd, BigDecimal startPrice, BigDecimal endPrice);
	
	List<Map<String, Object>> getHqShoesRankListByParam(List<Integer> shoesSeqs, List<Integer> shopSeqs, Date saleTimeStart, Date saleTimeEnd, Integer orderBy, Integer orderDir, Integer start,
                                                        Integer num, BigDecimal startPrice, BigDecimal endPrice, BaseUserEntity loginUser);

	List<Map<String, Object>> getHqShoesRankListByMoney(List<Integer> shoesSeqs, List<Integer> shopSeqs, Date saleTimeStart, Date saleTimeEnd, Integer orderBy, Integer orderDir, Integer start,
                                                        Integer num, BigDecimal startPrice, BigDecimal endPrice, BaseUserEntity loginUser);
}
