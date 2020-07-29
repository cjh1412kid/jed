package io.nuite.modules.order_platform_app.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.nuite.modules.order_platform_app.entity.ShoesInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 0
 * 
 * @author admin
 * @email 
 * @date 2018-04-11 11:29:58
 */
@Mapper
public interface ShoesInfoDao extends BaseMapper<ShoesInfoEntity> {
	/**
	 * 新增鞋子信息
	 * @param shoesInfoEntities
	 * @return
	 * @throws Exception
	 */
	Integer insertShoesInfo(List<ShoesInfoEntity> shoesInfoEntities) throws Exception;

	/**
	 * 修改鞋子信息
	 * @param shoesInfoEntities
	 * @return
	 * @throws Exception
	 */
	Integer updateShoesInfo(List<ShoesInfoEntity> shoesInfoEntities) throws Exception;

    void deleteByShoesSeq(@Param("seq") Integer seq);

    void updateOnSale(@Param("goodsSeq") Integer goodsSeq, @Param("onSale") int onSale);





	List<Map<String, Object>> getHqAndShopShoesList(Page<Map<String, Object>> page,
                                                    @Param("shoesSeqs") String shoesSeqs,
                                                    @Param("orderBy") Integer orderBy,
                                                    @Param("orderDir") Integer orderDir);


	List<Map<String, Object>> getShopShoesList(Page<Map<String, Object>> page,
                                               @Param("shopSeqList") List<Object> shopSeqList,
                                               @Param("shopSeq") Integer shopSeq,
                                               @Param("shoesSeqs") String shoesSeqs,
                                               @Param("orderBy") Integer orderBy,
                                               @Param("orderDir") Integer orderDir);



	List<Map<String, Object>> getHqAndShopShoesListOnSaleTime(Page<Map<String, Object>> page,
                                                              @Param("shoesSeqs") String shoesSeqs,
                                                              @Param("saleTimeStart") Date saleTimeStart,
                                                              @Param("saleTimeEnd") Date saleTimeEnd,
                                                              @Param("stockMinNum") Integer stockMinNum,
                                                              @Param("stockMaxNum") Integer stockMaxNum,
                                                              @Param("orderBy") Integer orderBy,
                                                              @Param("orderDir") Integer orderDir,
                                                              @Param("shopSeqList") List<Integer> shopSeqList);



	List<Map<String, Object>> getShopShoesListOnSaleTime(Page<Map<String, Object>> page,
                                                         @Param("shopSeqList") List<Object> shopSeqList,
                                                         @Param("shopSeq") Integer shopSeq,
                                                         @Param("shoesSeqs") String shoesSeqs,
                                                         @Param("saleTimeStart") Date saleTimeStart,
                                                         @Param("saleTimeEnd") Date saleTimeEnd,
                                                         @Param("stockMinNum") Integer stockMinNum,
                                                         @Param("stockMaxNum") Integer stockMaxNum,
                                                         @Param("orderBy") Integer orderBy,
                                                         @Param("orderDir") Integer orderDir);

	Map<String, Object> getShopShoesOnSaleTime(@Param("saleTimeStart") Date saleTimeStart,
                                               @Param("saleTimeEnd") Date saleTimeEnd, @Param("shoesSeq") Integer shoesSeq);

	List<Map<String, Object>> getShoeOnSaleTimeOnShop(@Param("saleTimeStart") Date saleTimeStart,
                                                      @Param("saleTimeEnd") Date saleTimeEnd, @Param("shoesSeq") Integer shoesSeq,
                                                      @Param("orderBy") Integer orderBy, @Param("orderDir") Integer orderDir);
	List<Map<String, Object>> getHeadOnSaleTime(@Param("saleTimeStart") Date saleTimeStart,
                                                @Param("saleTimeEnd") Date saleTimeEnd, @Param("shoesSeq") Integer shoesSeq);

	List<Map<String, Object>>  getAllSizeStock(@Param("shoesSeq") Integer shoesSeq, @Param("shopSeq") Integer shopSeq);

	List<Map<String, Object>>  getSizeStock(@Param("shoesSeq") Integer shoesSeq);

	/**
	 * 主推款列表
	 * @param map
	 * @param page
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectMainPushShoes(Map<String, Object> map, Page<Map<String, Object>> page) throws Exception;

	/**
	 * 老款列表
	 * @param map
	 * @param page
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectOldShoes(Map<String, Object> map, Page<Map<String, Object>> page) throws Exception;

	/**
	 * 断缺码列表
	 * @param map
	 * @param page
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectBreakOrAbsenceShoes(Map<String, Object> map, Page<Map<String, Object>> page) throws Exception;

	/**
	 * 门店所有货品的库存总量
	 * @param shopSeq
	 * @return
	 */
	Integer getShopStock(@Param("shopSeq") Integer shopSeq);
}
