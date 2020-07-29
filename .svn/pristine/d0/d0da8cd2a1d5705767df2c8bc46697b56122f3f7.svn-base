package io.nuite.modules.order_platform_app.dao;

import com.baomidou.mybatisplus.plugins.Page;
import io.nuite.modules.order_platform_app.entity.ShoesDataEntity;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import io.nuite.modules.order_platform_app.entity.AllocateTransferFictitiousMallEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * ${comments}
 * 
 * @author wanghao
 * @email barryhippo@163.com
 * @date 2019-09-10 15:46:51
 */
@Mapper
public interface AllocateTransferFictitiousMallDao extends BaseMapper<AllocateTransferFictitiousMallEntity> {
    /**
     * 调出货品
     * @param map
     * @param page
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> selectExportOutShoes(Map<String,Object> map, Page<Map<String,Object>> page) throws Exception;

    /**
     * 获取货品本店尺码和调出仓尺码
     * @param shopSeq
     * @param shoesSeq
     * @return
     * @throws Exception
     */
    List<Integer> selectShopAndExportSize(@Param("shopSeq")Integer shopSeq, @Param("shoesSeq")Integer shoesSeq) throws Exception;

    /**
     * 获取调出货品的门店及尺码数量
     * @param map
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> selectExportShop(Map<String,Object> map) throws Exception;

    /**
     * 获取调出货品的门店数量
     * @param map
     * @return
     * @throws Exception
     */
    Integer selectExportShopNum(Map<String,Object> map) throws Exception;

    /**
     * 获取调出货品的数量
     * @param map
     * @return
     * @throws Exception
     */
    Integer selectExportShoesNum(Map<String,Object> map) throws Exception;
    
    Integer selectExportShoesNumByShopSeq(@Param("shopSeq")Integer shopSeq,@Param("shoesSeq")Integer shoesSeq);

    /**
     * 删除数量为0的数据(物理删除)
     * @param map
     * @throws Exception
     */
    void deleteShoesByNumIsZero(Map<String,Object> map) throws Exception;

    /**
     * 删除对应门店的对应鞋子(物理删除)
     * @param map
     * @throws Exception
     */
    void deleteShoesByShopSeqAndShoesSeq(Map<String,Object> map) throws Exception;

    /**
     * 根据序号物理删除
     * @param seq
     * @throws Exception
     */
    void deleteShoesBySeq(Integer seq) throws Exception;
}
