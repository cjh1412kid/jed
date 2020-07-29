package io.nuite.modules.order_platform_app.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import io.nuite.datasources.DatabaseNames;

import com.baomidou.mybatisplus.annotations.TableField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 每日库存存储详情
 * 
 * @author wanghao
 * @email barryhippo@163.com
 * @date 2019-03-20 16:23:59
 */
@Data
@TableName(DatabaseNames.SR_ORDER_PLATFORM+"YHSR_OP_ShoesData_Daily_Detail")
public class ShoesDataDailyDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId(value = "Seq")
	private Integer seq;
	/**
	 * $column.comments
	 */
	@TableField(value = "ErpOrderNum")
	private String erpOrderNum;
	/**
	 * $column.comments
	 */
	@TableField(value = "Shop_Seq")
	private Integer shopSeq;
	/**
	 * $column.comments
	 */
	@TableField(value = "ShopName")
	private String shopName;
	/**
	 * $column.comments
	 */
	@TableField(value = "Shoes_Seq")
	private Integer shoesSeq;
	/**
	 * $column.comments
	 */
	@TableField(value = "ShoesId")
	private String shoesId;
	/**
	 * 对应的erp数据类型 0：总仓进货 1：仓出店 2：店退仓  3：销售 4:总仓退货 5：门店调拨
	 */
	@TableField(value = "Type")
	private Integer type;
	/**
	 * $column.comments
	 */
	@TableField(value = "Color")
	private Integer color;
	/**
	 * $column.comments
	 */
	@TableField(value = "Size")
	private Integer size;
	/**
	 * $column.comments
	 */
	@TableField(value = "Count")
	private Integer count;
	/**
	 * $column.comments
	 */
	@TableField(value = "Price")
	private BigDecimal price;
	/**
	 * $column.comments
	 */
	@TableField(value = "UpdateTime")
	private Date updateTime;
	/**
	 * $column.comments
	 */
	@TableField(value = "Join")
	private Integer join;

	/**
	 * 吊牌价
	 */
	@TableField(value = "Tag_Price")
	private BigDecimal tagPrice;

	/**
	 * 录入ERP时间
	 */
	@TableField(value = "Input_ERP_Time")
	private Date inputERPTime;
	/**
	 * 公司序号
	 */
	@TableField(value = "Company_Seq")
	private Integer companySeq;

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
	 * 实际价格
	 */
	@TableField(exist = false)
	private BigDecimal realPrice;

	/**
	 * 总数量
	 */
	@TableField(exist = false)
	private Integer num;
	
	@TableField(value = "goods_type")
	private Integer goodsType;
}
