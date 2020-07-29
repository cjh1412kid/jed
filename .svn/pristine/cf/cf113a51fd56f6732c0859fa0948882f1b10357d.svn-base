package io.nuite.modules.order_platform_app.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.nuite.datasources.DatabaseNames;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ${comments}
 * 
 * @author wanghao
 * @email barryhippo@163.com
 * @date 2018-11-30 11:05:11
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@TableName(DatabaseNames.SR_ORDER_PLATFORM+"YHSR_OP_SaleShoesDetail")
public class SaleShoesDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号(主键)
	 */
	@TableId(value = "Seq")
	private Integer seq;

	/**
	 * ERP订单号
	 */
	@TableField(value = "ErpOrderNum")
	private String erpOrderNum;
	/**
	 * 波次序号
	 */
	@TableField(value = "PeriodSeq")
	private Integer periodSeq;
	/**
	 * 大区序号
	 */
	@TableField(value = "AreaSeq")
	private Integer areaSeq;
	/**
	 * 分公司序号
	 */
	@TableField(value = "BranchOfficeSeq")
	private Integer branchOfficeSeq;
	/**
	 * 门店序号
	 */
	@TableField(value = "ShopSeq")
	private Integer shopSeq;
	/**
	 * 销售时间
	 */
	@TableField(value = "SaleDate")
	private Date saleDate;
	/**
	 * 销售类型 0：线下，1：线上
	 */
	@TableField(value = "SaleType")
	private Integer saleType;
	/**
	 * 鞋子序号
	 */
	@TableField(value = "ShoeSeq")
	private Integer shoeSeq;
	/**
	 * 货号
	 */
	@TableField(value = "ShoeID")
	private String shoeID;
	/**
	 * 订单数量
	 */
	@TableField(value = "OrderCount")
	private Integer orderCount;
	/**
	 * 销售双数
	 */
	@TableField(value = "SaleCount")
	private Integer saleCount;
	/**
	 * 成本
	 */
	@TableField(value = "Cost")
	private BigDecimal cost;
	/**
	 * 吊牌价
	 */
	@TableField(value = "TagPrice")
	private BigDecimal tagPrice;
	/**
	 * 实际销售价格
	 */
	@TableField(value = "RealPrice")
	private BigDecimal realPrice;
	/**
	 * 插入时间
	 */
	@TableField(value = "InputTime")
	private Date inputTime;
	/**
	 * 删除标识( 0 : 未删除、  1 : 删除 )
	 */
	@TableLogic
	@TableField(value = "Del")
	private Integer del;
	/**
	 * 公司序号
	 */
	@TableField(value = "Company_Seq")
	private Integer companySeq;
	/**
	 * 导购员序号
	 */
	@TableField(value = "Sale_Seq")
	private Integer saleSeq;

	/**
	 * 完成年份
	 */
	@TableField(exist = false)
	private Integer completeYear;

	/**
	 * 完成月份
	 */
	@TableField(exist = false)
	private Integer completeMonth;

	/**
	 * 完成周
	 */
	@TableField(exist = false)
	private Integer completeWeek;

	/**
	 * 完成日期
	 */
	@TableField(exist = false)
	private Integer completeDay;

	/**
	 * 完成金额
	 */
	@TableField(exist = false)
	private BigDecimal completeMoney;

	/**
	 * 累计天数
	 */
	@TableField(exist = false)
	private Integer days;

	/**
	 * 用户序号
	 */
	@TableField(exist = false)
	private Integer userSeq;

	/**
	 * 品类名称
	 */
	@TableField(exist = false)
	private String categoryName;
	
	@TableField(value = "goods_type")
	private Integer goodsType;
}
