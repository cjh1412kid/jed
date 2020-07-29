package io.nuite.modules.order_platform_app.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import io.nuite.modules.order_platform_app.entity.ShoesDataDailyDetailEntity;

import java.util.Date;
import java.util.List;

/**
 * 每日库存存储详情
 * 
 * @author wanghao
 * @email barryhippo@163.com
 * @date 2019-03-20 16:23:59
 */
@Mapper
public interface ShoesDataDailyDetailDao extends BaseMapper<ShoesDataDailyDetailEntity> {
    /**
     * 新增来往明细
     * @param shoesDataDailyDetailEntities
     * @return
     * @throws Exception
     */
    Integer insertShoesDataDailyDetail(List<ShoesDataDailyDetailEntity> shoesDataDailyDetailEntities) throws Exception;

    ShoesDataDailyDetailEntity getNearOne(@Param("shoesSeq")Integer shoesSeq,@Param("shopSeq") Integer shopSeq);
    
    ShoesDataDailyDetailEntity getOldOneByDepositTime(@Param("shoesSeq")Integer shoesSeq,@Param("inTime")Date date);

    ShoesDataDailyDetailEntity getOldOne(@Param("shoesSeq")Integer shoesSeq);

    /**
     * 把中间表数据插入主表
     * @return
     * @throws Exception
     */
    Integer selectSaleShoesDetailCopyIntoSaleShoesDetail() throws Exception;

}
