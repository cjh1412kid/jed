package io.nuite.modules.order_platform_app.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;

import io.nuite.modules.order_platform_app.entity.SaleShoesDetailEntity;


public interface SaleShoesDetailService extends IService<SaleShoesDetailEntity> {
	
	//获取多个门店时间段内某一货号的销量
	Integer getShopsSaleNumOneShoesSeq(List<Object> shopSeqs, Integer shoesSeq, Date startDate, Date endDate);

	//获取多个门店时间段内某一货号的销量
	Integer getShopsTotalSaleNumOneShoesSeq(List<Object> shopSeqs, Integer shoesSeq, Date startDate, Date endDate);
	
	//获取多个门店时间段内某一货号的平均售价
	BigDecimal getShopsAvgSalePriceOneShoesSeq(List<Object> shopSeqs, Integer shoesSeq, Date startDate, Date endDate);

	//获取总部和所有门店时间段内的进货量
	Integer getHqAndShopInNumOneShoesSeq(Integer shoesSeq, Date startDate, Date endDate);
	//获取某一门店时间段内的进货量
	Integer getShopInNumOneShoesSeq(Integer shopSeq, Integer shoesSeq, Date startDate, Date endDate);

	//获取总部和所有门店某鞋子的库存量
	Integer getHqAndShopStockNumOneShoesSeq(Integer shoesSeq);
	//获取门店某鞋子的库存量
	Integer getShopStockNumOneShoesSeq(Integer shopSeq, Integer shoesSeq);
	

	//某一门店在多个门店序号中时间段内鞋子的销量排名
	Integer getShopSaleNumRankOneShoesSeq(List<Object> shopSeqs, Integer shopSeq, Integer shoesSeq, Date startDate, Date endDate);
	//某一门店在多个门店序号中时间段内鞋子的库存排名
	Integer getShopStockRankOneShoesSeq(List<Object> shopSeqs, Integer shopSeq, Integer shoesSeq);


	//获取总部和门店某货号鞋子的详细的库存列表
	List<Map<String, Object>> getHqAndShopShoesStockDetail(Integer shoesSeq);
	//获取某一门店某货号鞋子的详细的库存列表
	List<Map<String, Object>> getShopShoesStockDetail(Integer shopSeq, Integer shoesSeq);
	//获取多个门店某货号的详细的库存列表
	List<Map<String, Object>> getAllShopsShoesStockDetail(List<Object> shopSeqs, Integer shoesSeq);


	/**
	 * 本货号在当年当季销量排名
	 * @param shopSeqs
	 * @param shoesSeq
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	Integer getGoodsIdSaleNumRank(List<Object> shopSeqs, Integer shoesSeq, Date startDate, Date endDate) throws Exception;

	/**
	 * 本货号在当年当季当前分类中的销量排名
	 * @param shopSeqs
	 * @param shoesSeq
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	Integer getGoodsIdSaleNumRankByCategory(List<Object> shopSeqs, Integer shoesSeq, Date startDate, Date endDate) throws Exception;

	//本货号在所有货号中的库存排名
	Integer getGoodsIdStockRank(List<Object> shopSeqs, Integer shoesSeq);

	
	
	//查询近7天总销量
	Integer getAllShopsSaleNum(Date sevenDayBefore, Integer shoesSeq);
	//查询近7天门店销量
	Integer getShopSaleNum(Integer shopSeq, Date sevenDayBefore, Integer shoesSeq);
	
	
	//查询近7天销量最好的店铺序号
	Map<String, Object> getMostSaleShopSeq(Date sevenDayBefore, Integer shoesSeq);

	Map<String, Object> getShopSaleNumAndSKUsMap(Integer companySeq, List<Integer> shopSeqList, Date saleTimeStart, Date saleTimeEnd, List<Integer> shoesSeqList);

}
