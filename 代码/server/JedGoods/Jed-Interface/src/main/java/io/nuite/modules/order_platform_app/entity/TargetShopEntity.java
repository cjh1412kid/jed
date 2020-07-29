package io.nuite.modules.order_platform_app.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.nuite.datasources.DatabaseNames;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @description: 门店指标实体类
 * @author: jxj
 * @create: 2019-03-21 10:32
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@TableName(DatabaseNames.SR_ORDER_PLATFORM + "YHSR_Target_Shop")
@ApiModel(value = "门店指标实体类")
public class TargetShopEntity implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 主键，序号
     */
    @TableId(value = "Seq")
    private Integer seq;

    /**
     * 金额
     */
    @TableField(value = "Money")
    @ApiModelProperty(value = "金额")
    private BigDecimal money;

    /**
     * 删除标记
     */
    @TableLogic
    @TableField(value = "Del")
    private Integer del;

    /**
     * 创建人
     */
    @TableField(value = "Creator")
    private Integer creator;

    /**
     * 外键，门店号
     */
    @TableField(value = "Shop_Seq")
    @ApiModelProperty(value = "门店号")
    private Integer shopSeq;

    /**
     * 指标年份
     */
    @TableField(value = "Target_Year")
    @ApiModelProperty(value = "指标年份")
    private Integer targetYear;

    /**
     * 指标月份
     */
    @TableField(value = "Target_Month")
    @ApiModelProperty(value = "指标月份")
    private Integer targetMonth;

    @TableField(value = "InputTime")
	private Date inputTime;
    
    /**
     *  指标（1.基准指标、2.中级指标、3.高级指标）
     */
    @TableField(value = "Tag")
   	private Integer tag;
    
    /**
     * 总指标金额
     */
    @TableField(exist = false)
    private BigDecimal totalMoney;

    /**
     * 月总指标列表
     */
    @TableField(exist = false)
    private List<TargetShopEntity> monthMoneyList;

    /**
     * 门店名称
     */
    @TableField(exist = false)
    private String shopName;

    /**
     * 单个月门店指标列表
     */
    @TableField(exist = false)
    private List<TargetShopEntity> monthShopMoneyList;

    /**
     * 实际完成金额
     */
    @TableField(exist = false)
    private BigDecimal completeMoney;

    /**
     * 总完成指标比例
     */
    @TableField(exist = false)
    private BigDecimal completePercent;

    /**
     * 门店完成指标列表
     */
    @TableField(exist = false)
    private List<TargetShopEntity> totalShopCompleteList;

    /**
     * 月总金额
     */
    @TableField(exist = false)
    private BigDecimal monthMoney;
    
    @TableField(exist = false)
    private BigDecimal standardMoney;
    
    @TableField(exist = false)
    private BigDecimal middleMoney;
    
    @TableField(exist = false)
    private BigDecimal advanceMoney;
    
    @TableField(exist = false)
    private BigDecimal middlePercent;
    
    @TableField(exist = false)
    private BigDecimal advancePercent;
    
    
    @TableField(exist = false)
    private Integer standardSeq;
    
    @TableField(exist = false)
    private Integer middleSeq;
    
    @TableField(exist = false)
    private Integer advanceSeq;
}
