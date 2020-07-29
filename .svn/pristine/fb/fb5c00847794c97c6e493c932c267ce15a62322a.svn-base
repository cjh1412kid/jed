package io.nuite.modules.sr_base.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.nuite.modules.sr_base.entity.GoodsShoesEntity;
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
 * @date 2018-04-11 11:38:09
 */
@Mapper
public interface GoodsShoesDao extends BaseMapper<GoodsShoesEntity> {

    List<String> selectGoodsIDsByCompanySeq(@Param("companySeq") Integer companySeq);

    Integer selectGoodsSeqByGoodsID(@Param("companySeq") Integer companySeq, @Param("goodsId") String goodsID);

    /**
     * 查找属性选项是否与商品有关联
     * @param companySeq
     * @param sxid
     * @param optCode
     * @return
     */
    int getCountBySXAndOption(@Param("companySeq") Integer companySeq, @Param("sxid") String sxid, @Param("optCode") String optCode);

    /**
     * 根据工厂号查询所有关联商品
     * @param companySeq
     * @return
     */
    List<GoodsShoesEntity> selectGoodsByCompanySeq(@Param("companySeq") Integer companySeq);

    
    /**
     * 根据属性条件获取货号
     * @param years
     * @param seasonSeqs
     * @param categorySeqs
     * @param colorSeqs
     * @param sXsql 
     * @return
     */
	List<Integer> getShoesSeqListOnSXParam(@Param("companySeq") Integer companySeq,
										   @Param("years")String years,
										   @Param("seasonSeqs")String seasonSeqs,
										   @Param("categorySeqs")String categorySeqs,
										   @Param("colorSeqs")String colorSeqs,
										   @Param("sXsql")String sXsql, 
										   @Param("oldOrNew")Integer oldOrNew,
										   @Param("saleTimeStart")Date saleTimeStart,
										   @Param("saleTimeEnd")Date saleTimeEnd,
										   @Param("shopSeqList")List<Integer> shopSeqList,
										   @Param("depositTime")Date depositTime);

	/**
	 * 根据模糊查询条件获取货号
	 * @param companySeq
	 * @param paramList
	 * @param categorySeqs
	 * @return
	 */
	List<Integer> getShoesSeqListOnFuzzySearchParam(@Param("companySeq") Integer companySeq, 
											@Param("paramList")List<String> paramList, 
											@Param("categorySeqs")String categorySeqs);
	
	/**
	 * 查询存在的年份列表
	 *
	 * @param companySeq
	 * @return
	 */
	List<Integer> selectExistYearList(Integer companySeq);
	
	/**
	 * 查询存在的季节列表
	 *
	 * @param companySeq
	 * @return
	 */
	List<Map<Integer, String>> selectExistSeasonList(Integer companySeq);

	
	List<GoodsShoesEntity> goodsList(@Param("goodId")String goodId);


	/**
	 * 获取所有有效鞋子序号
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Integer> getAllShoesSeq(Map<String,Object> map) throws Exception;

	/**
	 * 删除无效鞋子
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Integer deleteInvalidShoes(Map<String,Object> map) throws Exception;

	/**
	 * 还原对应公司下所有鞋子被删除状态
	 * @param companySeq
	 * @return
	 * @throws Exception
	 */
	Integer updateGoodDel(@Param("companySeq")Integer companySeq,@Param("goodsType")Integer goodsType) throws Exception;
	
	 List<Map<String, Object>> getSizeMap(@Param("shoesSeq")Integer shoesSeq);
}
