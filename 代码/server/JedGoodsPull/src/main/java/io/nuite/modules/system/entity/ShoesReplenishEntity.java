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
@TableName(DatabaseNames.SR_ORDER_PLATFORM + "YHSR_OP_ShoesReplenish")
public class ShoesReplenishEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 序号(主键)
     */
    @TableId(value = "Seq")
    private Integer seq;
    /**
     * 鞋子序号
     */
    @TableField(value = "Shoes_Seq")
    private Integer shoesSeq;
    /**
     * 补货量
     */
    @TableField(value = "ReplenishNum")
    private Integer replenishNum;
    /**
     * 预计到货时间
     */
    @TableField(value = "ReplenishTime")
    @JsonFormat(pattern = "yyyy/MM/dd",timezone = "GTM+8")
    private Date replenishTime;
    /**
     * 备注
     */
    @TableField(value = "Remark")
    private String remark;
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
    /**
     * 货号
     */
    @TableField(exist = false)
    private String goodID;
    /**
     * 公司序号
     */
    @TableField(value = "Company_Seq")
    private Integer companySeq;
	
    
    
    
    

}
