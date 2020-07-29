package io.nuite.modules.order_platform_app.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.nuite.modules.order_platform_app.entity.ShoesDataEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
public interface ShoesDataDao extends BaseMapper<ShoesDataEntity> {
    /**
     * 新增库存
     * @param shoesDataEntities
     * @return
     * @throws Exception
     */
    Integer insertShoesData(List<ShoesDataEntity> shoesDataEntities) throws Exception;

    /**
     * 更新库存
     * @param shoesDataEntities
     * @return
     * @throws Exception
     */
    Integer updateShoesData(List<ShoesDataEntity> shoesDataEntities) throws Exception;

    void changeShoesDataStock(@Param("shoesDataSeq") Integer shoesDataSeq, @Param("changNum") Integer changNum);

    List<ShoesDataEntity> queryDataAndColorName(@Param("goodsSeq") Integer goodsSeq);

    ShoesDataEntity selectByGoodsseqAndColorseqAndSizeseq(@Param("goodsSeq") Integer goodsSeq, @Param("colorSeq") Integer colorSeq, @Param("sizeSeq") Integer sizeSeq);

    List<Integer> selectByShoeSeq(@Param("shoeSeq") Integer shoeSeq);

    List<Integer> getShoesSeqsByShoesDataSeqs(@Param("list") List<Integer> shoesDataSeqs);

    void deleteByShoesSeq(@Param("seq")Integer seq);

    /**
     * 获取鞋子在各门店的库存
     * @param map
     * @return
     * @throws Exception
     */
    List<ShoesDataEntity> selectShopStockByShoesSeq(Map<String,Object> map) throws Exception;

    /**
     * 删除对应公司下所有库存数据
     * @param companySeq
     * @return
     * @throws Exception
     */
    Integer deleteShoesData(@Param("companySeq") Integer companySeq,@Param("goodsType") Integer goodsType) throws Exception;

    /**
     * 更新总进货量
     * @param shoesDataEntities
     * @return
     * @throws Exception
     */
    Integer updateShoesDataNum(List<ShoesDataEntity> shoesDataEntities) throws Exception;

    /**
     * 副表数据导入到主表
     * @return
     * @throws Exception
     */
    Integer selectShoesDataCopyIntoShoesData(Integer goodsType) throws Exception;

    /**
     * 库存结构按年份
     * @param map
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> selectYearStockStructure(Map<String,Object> map) throws Exception;

    /**
     * 库存结构按季节
     * @param map
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> selectSeasonStockStructure(Map<String,Object> map) throws Exception;

    /**
     * 库存结构按类别
     * @param map
     * @return
     * @throws Exception
     */
    Map<String,Object> selectCategoryStockStructure(Map<String,Object> map) throws Exception;

    /**
     * 鞋子库存列表
     * @param map
     * @param page
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> selectShoesStock(Map<String,Object> map,Page<Map<String,Object>> page) throws Exception;
}
