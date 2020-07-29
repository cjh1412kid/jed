package io.nuite.modules.system.entity.order_platform;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.nuite.datasources.DatabaseNames;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 门店配码单
 * </p>
 *
 * @author yangchuang
 * @since 2019-04-03
 */
@Data
@Accessors(chain = true)
@TableName(DatabaseNames.SR_ORDER_PLATFORM + "YHSR_OP_ShopAllotOrder")
public class ShopAllotOrderEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableId(value = "Seq", type = IdType.AUTO)
    private Integer seq;
    
    @TableField("SilevePlateDistributeSeq")
    private Integer silevePlateDistributeSeq;
    
    @TableField("Count")
    private Integer count;
    
    @TableField("InputTime")
    private Date inputTime;
    
    @TableField("Del")
    @TableLogic
    private Integer del;
    
    /**
     * 订单尺码数据
     */
    @TableField(exist = false)
    private List<ShopAllotOrderDetailEntity> details;
    
}
