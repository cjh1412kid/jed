package io.nuite.modules.system.dao.order_platform;

import io.nuite.modules.system.entity.order_platform.SizeAllotTemplateEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author yangchuang
 * @since 2019-03-29
 */

@Mapper
public interface SizeAllotTemplateDao extends BaseMapper<SizeAllotTemplateEntity> {
    
    /**
     * 根据品类查询所有模版数据
     *
     * @param categorySeq
     * @return
     */
    List<SizeAllotTemplateEntity> selectByCategorySeq(Integer categorySeq);
}
