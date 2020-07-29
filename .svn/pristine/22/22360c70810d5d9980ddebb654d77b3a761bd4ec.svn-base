package io.nuite.modules.order_platform_app.service;

import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.order_platform_app.entity.ShoesDataEntity;

import java.util.List;
import java.util.Map;

public interface ShoesDataService extends IService<ShoesDataEntity> {

    ShoesDataEntity selectShoesDataByColorAndSize(Integer colorSeq, Integer sizeSeq, Integer shoesSeq);



	
	List<Object> getHqAndShopPickShoesSeqs(Integer companySeq);
	
	List<Integer> getHqAndShopBreakSizeShoesSeqs(List<Integer> shoesSeqList, List<Integer> shopSeqList);

	List<Integer> getHqAndShopMissSizeShoesSeqs(List<Integer> shoesSeqList, List<Integer> shopSeqList);

	List<Integer> getHqAndShopFullSizeInSomeRangeShoesSeqs(List<Integer> shoesSeqList, Integer sizeStart, Integer sizeEnd, List<Integer> shopSeqList);
	
	List<Integer> getHqAndShopFullSizeShoesSeqs(List<Integer> shoesSeqList, List<Integer> shopSeqList);
	
	
	List<Object> getShopPickShoeSeqs(Integer companySeq, List<Integer> shopSeqList);
	
	List<Object> getAllShopPickShoeSeqs(Integer companySeq);

	List<Integer> getShopBreakSizeShoesSeqs(Integer shopSeq, List<Integer> shoesSeqList);

	List<Integer> getShopMissSizeShoesSeqs(Integer shopSeq, List<Integer> shoesSeqList);

	List<Integer> getShopFullSizeInSomeRangeShoesSeqs(Integer shopSeq, List<Integer> shoesSeqList, Integer sizeStart, Integer sizeEnd);
	
	List<Integer> getShopFullSizeShoesSeqs(Integer shopSeq, List<Integer> shoesSeqList);



	List<ShoesDataEntity> getShoesDateListByShoesSeq(Integer shopSeq, Integer shoesSeq);

	/**
	 * 获取鞋子在各门店的库存
	 * @param goodId
	 * @param sizeSeq
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> selectShopStockByShoesSeq(String goodId, Integer sizeSeq) throws Exception;

	/**
	 * 获取总库存
	 * @param satisfyShoesSeqs
	 * @param shopSeqs
	 * @return
	 * @throws Exception
	 */
	Integer totalNum(List<Integer> satisfyShoesSeqs, List<Integer> shopSeqs) throws Exception;

	/**
	 * 获取总部库存
	 * @param satisfyShoesSeqs
	 * @return
	 * @throws Exception
	 */
	Integer totalStockNum(List<Integer> satisfyShoesSeqs) throws Exception;

	/**
	 * 获取门店总库存
	 * @param satisfyShoesSeqs
	 * @param shopSeqs
	 * @return
	 * @throws Exception
	 */
	Integer totalShopNum(List<Integer> satisfyShoesSeqs, List<Integer> shopSeqs) throws Exception;
	
	List<Object> getAllSize(Integer shoesSeq);
	
	List<Object> getNumCount(Integer shoesSeq, Integer sizeSeq);

	/**
	 * 获取断码鞋子序号
	 * @param companySeq
	 * @param shoesSeqList
	 * @param shopSeqList
	 * @return
	 */
	List<Integer> getBreakShoesSeqList(Integer companySeq, List<Integer> shoesSeqList, List<Integer> shopSeqList);

	/**
	 * 获取缺码鞋子序号
	 * @param shoesSeqList
	 * @param shopSeqList
	 * @return
	 */
	List<Integer> getAbsenceShoesSeqList(Integer companySeq, List<Integer> shoesSeqList, List<Integer> shopSeqList);
}
