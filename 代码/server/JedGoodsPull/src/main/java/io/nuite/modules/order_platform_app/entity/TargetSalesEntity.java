package io.nuite.modules.order_platform_app.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
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
 * @description: 导购员指标实体类
 * @author: jxj
 * @create: 2019-03-21 10:32
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@TableName(DatabaseNames.SR_ORDER_PLATFORM + "YHSR_Target_Sales")
@ApiModel(value = "导购员指标实体类")
public class TargetSalesEntity implements Serializable {
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
     * 外键，导购员需要
     */
    @TableField(value = "Sales_Seq")
    @ApiModelProperty(value = "导购员需要")
    private Integer salesSeq;

    /**
     * 指标周
     */
    @TableField(value = "Target_Week")
    @ApiModelProperty(value = "指标周")
    private Integer targetWeek;

    /**
     * 指标日期
     */
    @TableField(value = "Target_Day")
    @ApiModelProperty(value = "指标日期")
    private Integer targetDay;

    /**
     * 门店指标序号
     */
    @TableField(value = "Target_Shop_Seq")
    @ApiModelProperty(value = "门店指标序号")
    private Integer targetShopSeq;

    /**
     *  插入时间
     */
    @TableField(value = "InputTime")
	private Date inputTime;
    
    /**
     *  指标（1.基准指标、2.中级指标、3.高级指标）
     */
    @TableField(value = "Tag")
   	private Integer tag;
    /**
     * 导购员名称
     */
    @TableField(exist = false)
    private String salesName;

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
     * 指标年份
     */
    @TableField(exist = false)
    private Integer targetYear;

    /**
     * 指标月份
     */
    @TableField(exist = false)
    private Integer targetMonth;

    /**
     * 每月指标列表
     */
    @TableField(exist = false)
    private List<TargetSalesEntity> monthMoneyList;

    /**
     * 每月指标列表
     */
    @TableField(exist = false)
    private List<TargetSalesEntity> monthShopMoneyList;

    /**
     * 总金额
     */
    @TableField(exist = false)
    private BigDecimal totalMoney;

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
    
    @TableField(exist = false)
    private Integer standardShopSeq;
    
    @TableField(exist = false)
    private Integer middleShopSeq;
    
    @TableField(exist = false)
    private Integer advanceShopSeq;
    
    
	
}
