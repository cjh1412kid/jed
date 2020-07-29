package io.nuite.modules.system.entity.order_platform;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import io.nuite.datasources.DatabaseNames;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 货品订单详细
 * </p>
 *
 * @author yangchuang
 * @since 2019-04-01
 */
@Data
@Accessors(chain = true)
@TableName(DatabaseNames.SR_ORDER_PLATFORM + "YHSR_OP_GoodsAllotOrderDetail")
public class GoodsAllotOrderDetailEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 订单号
     */
    @TableField("OrderSeq")
    private Integer orderSeq;
    /**
     * 尺码
     */
    @TableField("Size")
    private Integer size;
    /**
     * 尺码对应数量
     */
    @TableField("Count")
    private Integer count;
    
    /**
     * 门店配码后剩余数量
     */
    @TableField("Yu")
    private Integer yu;
    
}
