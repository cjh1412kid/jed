package io.nuite.modules.system.entity.order_platform;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import io.nuite.datasources.DatabaseNames;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 门店配码单尺码数据
 * </p>
 *
 * @author yangchuang
 * @since 2019-04-03
 */
@Data
@Accessors(chain = true)
@TableName(DatabaseNames.SR_ORDER_PLATFORM + "YHSR_OP_ShopAllotOrderDetail")
public class ShopAllotOrderDetailEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableField("OrderSeq")
    private Integer orderSeq;
    
    @TableField("Size")
    private Integer size;
    
    @TableField("Count")
    private Integer count;
    
    @TableField(exist = false)
    private Integer inNum;
    
}
