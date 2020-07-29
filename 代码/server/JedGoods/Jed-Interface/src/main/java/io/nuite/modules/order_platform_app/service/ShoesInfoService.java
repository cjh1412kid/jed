package io.nuite.modules.order_platform_app.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.order_platform_app.entity.ShoesInfoEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;


public interface ShoesInfoService extends IService<ShoesInfoEntity> {

	List<Map<String, Object>> getShoesCategory(Integer companySeq, Integer parentSeq);

	ShoesInfoEntity getShoesInfoByShoesSeq(Integer shoesSeq);

	
	
	
	List<Object> getHqNewestShoesSeqs(Integer companySeq);
	
	List<Object> getHqMainpushShoesSeqs(Integer companySeq);
	
	List<Object> getShopMainpushShoesSeqs(Integer shopSeq);

	List<Object> getAllShopMainpushShoesSeqs(Integer companySeq);
	
	
	

	Page<Map<String, Object>> getHqAndShopShoesListOnSaleTime(Integer requestFromType, List<Integer> satisfyShoesSeqs, Date saleTimeStart, Date saleTimeEnd,
			Integer stockMinNum, Integer stockMaxNum, Integer orderBy, Integer orderDir, Integer start, Integer num,List<Integer> shopSeqList);
	
	
	
	Page<Map<String, Object>> getShopShoesListOnSaleTime(Integer requestFromType, List<Object> shopSeqList,Integer shopSeq, List<Integer> satisfyShoesSeqs, Date saleTimeStart,
			Date saleTimeEnd, Integer stockMinNum, Integer stockMaxNum, Integer orderBy, Integer orderDir, Integer start, Integer num);
	

	void setHqMainpush(Integer shoesSeq, Integer isMainpush);

	Map<String, Object> getShopShoeOnSaleTime(Date saleTimeStart, Date saleTimeEnd, Integer shoesSeq);

	List<Map<String, Object>> getShoeOnSaleTimeOnShop(Date saleTimeStart, Date saleTimeEnd, Integer shoesSeq,Integer orderBy,Integer orderDir);

	List<Map<String, Object>> getHeadOnSaleTime(Date saleTimeStart, Date saleTimeEnd, Integer shoesSeq);
	
	
	List<Map<String, Object>> getAllSizeStock(Integer shoesSeq,Integer shopSeq);
	
	
	
	List<Map<String, Object>> getSizeStock(Integer shoesSeq);

	/**
	 * 获取主推鞋子列表
	 * @param userEntity
	 * @param shopSeqList
	 * @param categorySeqList
	 * @param orderBy
	 * @param orderDir
	 * @param saleTimeStart
	 * @param saleTimeEnd
	 * @param page
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectMainPushShoes(BaseUserEntity userEntity,List<Integer> shopSeqList,List<Integer> categorySeqList, Integer orderBy, Integer orderDir, Date saleTimeStart, Date saleTimeEnd, Page<Map<String,Object>> page) throws Exception;

	/**
	 * 获取畅销鞋子列表
	 * @param userEntity
	 * @param shopSeqList
	 * @param yearList
	 * @param seasonSeqList
	 * @param categorySeqList
	 * @param ranking
	 * @param orderBy
	 * @param orderDir
	 * @param saleTimeStart
	 * @param saleTimeEnd
	 * @param page
	 * @return
	 * @throws Exception
	 */
	List<Map<String,Object>> selectSalableShoes(BaseUserEntity userEntity, List<Integer> shopSeqList, List<Integer> yearList,List<Integer> seasonSeqList, List<Integer> categorySeqList, Integer ranking,Integer orderBy, Integer orderDir, Date saleTimeStart, Date saleTimeEnd,Page<Map<String,Object>> page) throws Exception;

	/**
	 * 获取滞销鞋子列表
	 * @param userEntity
	 * @param shopSeqList
	 * @param yearList
	 * @param seasonSeqList
	 * @param categorySeqList
	 * @param ranking
	 * @param orderBy
	 * @param orderDir
	 * @param saleTimeStart
	 * @param saleTimeEnd
	 * @param page
	 * @return
	 * @throws Exception
	 */
	List<Map<String,Object>> selectUnsalableShoes(BaseUserEntity userEntity, List<Integer> shopSeqList, List<Integer> yearList,List<Integer> seasonSeqList, List<Integer> categorySeqList, Integer ranking,Integer orderBy, Integer orderDir, Date saleTimeStart, Date saleTimeEnd,Page<Map<String,Object>> page) throws Exception;

	/**
	 * 库存结构按年份
	 * @param userEntity
	 * @param shopSeq
	 * @param yearList
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> selectYearStockStructure(BaseUserEntity userEntity,Integer shopSeq,List<Integer> yearList) throws Exception;

	/**
	 * 库存结构按季节
	 * @param userEntity
	 * @param shopSeq
	 * @param yearList
	 * @param seasonSeqList
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> selectSeasonStockStructure(BaseUserEntity userEntity,Integer shopSeq,List<Integer> yearList,List<Integer> seasonSeqList) throws Exception;

	/**
	 * 库存结构按类别
	 * @param userEntity
	 * @param shopSeq
	 * @param yearList
	 * @param seasonSeqList
	 * @param type
	 * @param categorySeqList
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> selectCategoryStockStructure(BaseUserEntity userEntity,Integer shopSeq,List<Integer> yearList,List<Integer> seasonSeqList,Integer type,List<Integer> categorySeqList) throws Exception;

	/**
	 * 鞋子库存列表
	 * @param userEntity
	 * @param shopSeq
	 * @param yearList
	 * @param seasonSeqList
	 * @param categorySeqList
	 * @param page
	 * @return
	 * @throws Exception
	 */
	List<Map<String,Object>> selectShoesStock(BaseUserEntity userEntity,Integer shopSeq,List<Integer> yearList,List<Integer> seasonSeqList,List<Integer> categorySeqList,Page<Map<String,Object>> page) throws Exception;

	/**
	 * 获取老款鞋子
	 * @param userEntity
	 * @param shopSeqList
	 * @param yearList
	 * @param categorySeqList
	 * @param seasonSeqList
	 * @param saleTimeStart
	 * @param saleTimeEnd
	 * @param page
	 * @return
	 * @throws Exception
	 */
	List<Map<String,Object>> selectOldShoes(BaseUserEntity userEntity,List<Integer> shopSeqList,List<Integer> yearList,List<Integer> categorySeqList,List<Integer> seasonSeqList,Date saleTimeStart,Date saleTimeEnd,Page<Map<String,Object>> page) throws Exception;

	/**
	 * 获取断缺码鞋子
	 * @param userEntity
	 * @param shopSeqList
	 * @param yearList
	 * @param seasonSeqList
	 * @param type
	 * @param page
	 * @return
	 * @throws Exception
	 */
	List<Map<String,Object>> selectBreakOrAbsenceShoes(BaseUserEntity userEntity,List<Integer> shopSeqList,List<Integer> yearList,List<Integer> seasonSeqList,Integer type,Page<Map<String,Object>> page) throws Exception;


	/**
	 * 门店所有货品的库存总量
	 * @param shopSeq
	 * @return
	 */
	Integer getShopStock(Integer shopSeq);
	
	Integer getTempStock(Integer shopSeq);
	
	void updateTempStock(Integer shopSeq,Integer tempStock);
	
	void updateChangeTempStock(Integer shopSeq,Integer changeTempStock);
	
	void restoreTempStock();


	/**
	 * 调出货品
	 * @param userEntity
	 * @param yearList
	 * @param categorySeqList
	 * @param seasonSeqList
	 * @param orderBy
	 * @param orderDir
	 * @param saleTimeStart
	 * @param saleTimeEnd
	 * @param page
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectExportOutShoes(BaseUserEntity userEntity,List<Integer> yearList,List<Integer> categorySeqList,List<Integer> seasonSeqList,Integer orderBy,Integer orderDir,Date saleTimeStart,Date saleTimeEnd,Integer isTransferOut, Page<Map<String,Object>> page) throws Exception;

	/**
	 * 获取最近两个季节
	 * @return
	 * @throws Exception
	 */
	List<Integer> selectLastSeason() throws Exception;
	
}
