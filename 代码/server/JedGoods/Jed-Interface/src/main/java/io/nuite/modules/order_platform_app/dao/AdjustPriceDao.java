package io.nuite.modules.order_platform_app.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.nuite.modules.order_platform_app.entity.AdjustPriceEntity;
import io.nuite.modules.order_platform_app.entity.MessageEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @description: 调价Dao类
 * @author: jxj
 * @create: 2019-03-30 13:10
 */
@Mapper
public interface AdjustPriceDao extends BaseMapper<AdjustPriceEntity> {
    /**
     * 获取调价信息下调价列表
     * @param map
     * @return
     * @throws Exception
     */
    List<AdjustPriceEntity> selectAdjustPriceByMessageSeq(Map<String,Object> map) throws Exception;
    
    
    /**
     * 查询公司下的所有调价记录
     *
     * @param companySeq
     * @return
     */
    List<Map<String, Object>> selectAdjustPriceTree(@Param("companySeq") Integer companySeq);
    
    /**
     * 根据调价事件名称查询所有相关门店调价数据
     * @param flag
     * @return
     */
    List<MessageEntity> selectAdjustPriceDataByFlag(@Param("flag") String flag);
}


