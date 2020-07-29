package io.nuite.modules.order_platform_app.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import io.nuite.common.utils.PageUtils;
import io.nuite.modules.order_platform_app.entity.SaleShoesDetailEntity;
import io.nuite.modules.order_platform_app.entity.TargetShopEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;

/**
 * @description: 门店指标Service类
 * @author: jxj
 * @create: 2019-03-21 10:42
 */

public interface TargetShopService extends IService<TargetShopEntity> {
    /**
     * 新增门店指标列表
     * @param list
     * @param baseUserEntity
     * @return
     * @throws Exception
     */
    boolean insertOrUpdateTarget(String list,BaseUserEntity baseUserEntity) throws Exception;

    /**
     * 获取总指标
     * @param targetYear
     * @param baseUserEntity
     * @return
     * @throws Exception
     */
    Map<String,Object> selectTotalTarget(Integer targetYear,BaseUserEntity baseUserEntity) throws Exception;

    /**
     * 获取单个月指标
     * @param targetYear
     * @param targetMonth
     * @param baseUserEntity
     * @return
     * @throws Exception
     */
    Map<String,Object> selectMonthTarget(Integer targetYear,Integer targetMonth,BaseUserEntity baseUserEntity) throws Exception;
    
    /**
     * 获取指标列表
     * @param targetYear
     * @param targetMonth
     * @param baseUserEntity
     * @return
     * @throws Exception
     */
    Map<String,Object> selectTargetList(Integer targetYear,Integer targetMonth,BaseUserEntity baseUserEntity,Integer type) throws Exception;

    /**
     * 获取总销售指标分析
     * @param targetYear
     * @param targetMonth
     * @param baseUserEntity
     * @return
     * @throws Exception
     */
    Map<String,Object> selectTotalTargetAnalyze(Integer targetYear,Integer targetMonth,BaseUserEntity baseUserEntity) throws Exception;

    /**
     * 获取门店销售指标分析
     * @param targetYear
     * @param targetMonth
     * @param shopSeq
     * @param baseUserEntity
     * @return
     * @throws Exception
     */
    Map<String, Object> selectSalesTargetAnalyze(Integer targetYear,Integer targetMonth,Integer shopSeq,BaseUserEntity baseUserEntity) throws Exception;

    PageUtils getTargetList(Page page,Integer seq,  Map<String, Object> params) throws Exception ;
    
    void parseJRDExcelToSave(MultipartFile file, BaseUserEntity userEntity,Integer year) throws Exception;

}
