package io.nuite.modules.system.entity.order_platform;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import io.nuite.datasources.DatabaseNames;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 配码模版详情
 * </p>
 *
 * @author yangchuang
 * @since 2019-03-29
 */
@Data
@Accessors(chain = true)
@TableName(DatabaseNames.SR_ORDER_PLATFORM + "YHSR_OP_SizeAllotDetail")
public class SizeAllotDetailEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 模版
     */
    @TableField("TemplateSeq")
    private Integer templateSeq;
    /**
     * 尺码
     */
    @TableField("Size")
    private Integer size;
    /**
     * 模版中每个尺码的数量
     */
    @TableField("Per")
    private Integer per;
    
}
