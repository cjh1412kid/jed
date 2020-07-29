package io.nuite.modules.system.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.nuite.datasources.DatabaseNames;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ${comments}
 *
 * @author yangchuang
 * @date 2019-04-29 10:02:01
 */

@Data
@TableName(DatabaseNames.SR_ORDER_PLATFORM + "YHSR_OP_ShoesReplenishDetail")
public class ShoesReplenishDetailEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 序号(主键)
     */
    @TableId(value = "Seq")
    private Integer seq;
    /**
     *补单序号
     */
    @TableField(value = "ReplenishSeq")
    private Integer replenishSeq;
    /**
     * 尺码
     */
    @TableField(value = "Size_Seq")
    private Integer sizeSeq;
    /**
     * 补单数量
     */
    @TableField(value = "ReplenishNum")
    private Integer replenishNum; 
    
    
    /**
     * 插入时间
     */
    @TableField(value = "InputTime")
    private Date inputTime;
    /**
     * 删除标识(0:未删除,1:已删除)
     */
    @TableLogic
    @TableField(value = "Del")
    private Integer del;

    @TableField(exist = false)
    private String sizeName;
	
    
    
    
    

}
