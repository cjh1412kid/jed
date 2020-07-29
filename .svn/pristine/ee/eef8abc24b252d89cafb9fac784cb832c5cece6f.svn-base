package io.nuite.modules.sr_base.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.nuite.modules.sr_base.entity.BaseShopEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
public interface BaseShopDao extends BaseMapper<BaseShopEntity> {

    List<Map<String, Object>> getAreaListByBrandSeq(Integer brandSeq);

    List<Map<String, Object>> getOfficeListByAreaSeq(Integer areaSeq);

    List<Map<String, Object>> getShopListByOfficeSeq(Integer officeSeq);

    List<BaseShopEntity> getShopListByAreaSeq(@Param("areaSeqList") List<Integer> areaSeqList,
                                              @Param("start") Integer start, @Param("num") Integer num);

    /**
     * 根据该门店的Seq返回该门店的信息
     */
    BaseShopEntity getShopBySeq(@Param("seq") Integer seq);

    /**
     * 同等条件下没有分页的数据条数
     */
    int getTotalCount(@Param("areaSeqList") List<Integer> areaSeqList);

    List<BaseShopEntity> selectAllList(@Param("brandSeq") Integer brandSeq);
    
    /**
     * 门店管理分页查询
     * @param page
     * @param companySeq
     * @return
     */
    List<BaseShopEntity> listPageByCompanySeq(Page page, @Param("companySeq") Integer companySeq);

    /**
     * 新增门店
     * @param baseShopEntities
     * @return
     * @throws Exception
     */
    Integer insertShop(List<BaseShopEntity> baseShopEntities) throws Exception;
    List<Integer> selectShopSeqs(@Param("brandSeq") Integer brandSeq);

    /**
     * 更新门店
     * @param baseShopEntities
     * @return
     * @throws Exception
     */
    Integer updateShop(List<BaseShopEntity> baseShopEntities) throws Exception;

    /**
     * 获取所有门店包括已删除
     * @param map
     * @return
     * @throws Exception
     */
    List<BaseShopEntity> selectShop(Map<String, Object> map) throws Exception;
}
