package io.nuite.modules.system.service.order_platform;

import com.baomidou.mybatisplus.service.IService;
import io.nuite.modules.system.entity.order_platform.SizeAllotTemplateEntity;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yangchuang
 * @since 2019-03-29
 */
public interface SizeAllotTemplateService extends IService<SizeAllotTemplateEntity> {
    /**
     * 获取品类相关联的所有配码模版
     *
     * @param categorySeq
     * @return
     */
    List<SizeAllotTemplateEntity> listTemplateByCategorySeq(Integer categorySeq);
    
    /**
     * 保存配码模版
     *
     * @param sizeAllotTemplateEntity
     * @return
     */
    SizeAllotTemplateEntity saveSizeAllotTemplate(SizeAllotTemplateEntity sizeAllotTemplateEntity);
    /**
     * 更新配码模版及细节
     *
     * @param sizeAllotTemplateEntity
     * @return
     */
    SizeAllotTemplateEntity updateSizeAllotTemplate(SizeAllotTemplateEntity sizeAllotTemplateEntity);
    /**
     * 删除配码模版及细节
     *
     * @param templateSeq
     * @return
     */
    void deleteSizeAllotTemplate(Integer templateSeq);
}
