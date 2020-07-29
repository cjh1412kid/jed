package io.nuite.modules.system.entity.order_platform;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.nuite.datasources.DatabaseNames;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 货品配码订单
 * </p>
 *
 * @author yangchuang
 * @since 2019-04-01
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain = true)
@TableName(DatabaseNames.SR_ORDER_PLATFORM + "YHSR_OP_GoodsAllotOrder")
public class GoodsAllotOrderEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableId(value = "Seq", type = IdType.AUTO)
    private Integer seq;
    
    @TableField("GoodID")
    private String goodID;
    /**
     * 订货总量
     */
    @TableField("Total")
    private Integer total;
    /**
     * 原计划订货数量
     */
    @TableField("PlanCount")
    private Integer planCount;
    
    @TableField("TemplateSeq")
    private Integer templateSeq;
    
    @TableField("CompanySeq")
    private Integer companySeq;
    
    @TableField("Year")
    private Integer year;
    
    @TableField("SeasonSeq")
    private Integer seasonSeq;
    
    @TableField("CategorySeq")
    private Integer categorySeq;
    
    @TableField("InputTime")
    private Date inputTime;
    
    @TableField("Del")
    @TableLogic
    private Integer del;
    
    /**
     * 货品订单详情
     */
    @TableField(exist = false)
    private List<GoodsAllotOrderDetailEntity> details;
    
    @TableField(exist = false)
    private String seasonName;
    
    @TableField(exist = false)
    private String categoryName;
    
}
