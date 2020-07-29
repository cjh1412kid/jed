package io.nuite.modules.order_platform_app.service;

import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.order_platform_app.entity.AdjustPriceEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @description: 调价Service类
 * @author: jxj
 * @create: 2019-03-30 13:15
 */

public interface AdjustPriceService extends IService<AdjustPriceEntity> {
    /**
     * 获取调价信息下调价列表
     *
     * @param messageSeq
     * @return
     * @throws Exception
     */
    List<AdjustPriceEntity> selectAdjustPriceByMessageSeq(Integer messageSeq) throws Exception;
    
    List queryAdjustPriceTable();
    
    /**
     * 查询公司下的所有调价记录
     *
     * @param companySeq
     * @return
     */
    List<Map<String, Object>> queryAdjustPriceTree(Integer companySeq);
    
    /**
     * 解析调价Excel
     *
     * @param excelFile
     * @param user
     * @param flag
     * @return 返回查询不到用户的门店列表
     */
    List<String> parseExcelToSave(MultipartFile excelFile, BaseUserEntity user, String flag) throws Exception;
    
    /**
     * 根据调价事件标题查询调价列表
     *
     * @param flag
     * @return
     */
    List<Map<String, Object>> queryExcelDataByFlag(String flag);
}
