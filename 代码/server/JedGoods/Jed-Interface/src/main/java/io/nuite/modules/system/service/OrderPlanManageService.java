package io.nuite.modules.system.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import io.nuite.common.utils.PageUtils;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.system.entity.OrderManageEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author yangchuang
 * @since 2019-03-22
 */
public interface OrderPlanManageService extends IService<OrderManageEntity> {
    
    /**
     * 解析Excel文件并存储数据
     *
     * @param file
     * @param userEntity
     * @throws Exception
     */
    void parseExcelToSave(MultipartFile file, BaseUserEntity userEntity) throws Exception;
    
    /**
     * 解析吉尔达Excel文件并存储数据
     *
     * @param file
     * @param userEntity
     * @throws Exception
     */
    void parseJRDExcelToSave(MultipartFile file, BaseUserEntity userEntity) throws Exception;
    
    PageUtils queryPage(Page<OrderManageEntity> page, Map<String, Object> params, Integer companySeq);
    
    OrderManageEntity getOrderManageEntityByGoodId(String goodId);
    
    
    List<Map<String,Object>> getAllExistSeasons(Integer companySeq);
    
    List<Integer> getAllExistYears(Integer companySeq);
}
