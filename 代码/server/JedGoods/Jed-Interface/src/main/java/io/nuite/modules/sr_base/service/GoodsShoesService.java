package io.nuite.modules.sr_base.service;

import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.sr_base.entity.GoodsShoesEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface GoodsShoesService extends IService<GoodsShoesEntity> {
    
    GoodsShoesEntity getGoodsByGoodId(Integer companySeq, String goodId);
    
    /**
     * 查找属性选项是否与商品有关联
     *
     * @param companySeq
     * @param sxid       属性列名
     * @param optCode    属性选项编码
     * @return 关系属性选项的商品数
     */
    int getCountBySXAndOption(Integer companySeq, String sxid, String optCode);
    
    List<Integer> getShoesSeqListOnSXParam(Integer companySeq, List<Integer> yearList, List<Integer> seasonSeqList, List<Integer> categorySeqList,
                                           List<Integer> colorSeqList, Map<String, String> sXMap, String fuzzySearchParam, Date saleTimeStart,Date saleTimeEnd,List<Integer> shopSeqList,Date depositTime);
    
    /**
     * 查询存在的年份列表
     *
     * @param companySeq
     * @return
     */
    List<Integer> queryExistYearList(Integer companySeq);
    
    /**
     * 查询存在的季节列表
     *
     * @param companySeq
     * @return
     */
    List<Map<Integer, String>> queryExistSeasonList(Integer companySeq);
    
    
    List<Integer> getShoesSeqListByParam(Integer companySeq, List<Integer> yearList, List<Integer> seasonSeqList, List<Integer> categorySeqList,
            List<Integer> colorSeqList, Map<String, String> sXMap, Integer oldOrNew);
    
    GoodsShoesEntity goodsList(String goodId);
    
    List<Map<String, Object>> getSizeMap(Integer shoesSeq);
    
}
