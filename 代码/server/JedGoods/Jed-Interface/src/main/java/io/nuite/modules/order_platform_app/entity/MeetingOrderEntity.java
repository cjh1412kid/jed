package io.nuite.modules.order_platform_app.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import io.nuite.datasources.DatabaseNames;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableLogic;

import java.io.Serializable;
import java.util.Date;

/**
 * ${comments}
 * 
 * @author wanghao
 * @email barryhippo@163.com
 * @date 2019-03-27 14:26:30
 */
@TableName(DatabaseNames.SR_ORDER_PLATFORM + "YHSR_OP_MeetingOrder")
public class MeetingOrderEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号(主键)
	 */
	@TableId(value = "Seq")
	private Integer seq;
	/**
	 * 公司序号
	 */
	@TableField(value = "Company_Seq")
	private Integer companySeq;
	/**
	 * 年份
	 */
	@TableField(value = "Year")
	private Integer year;
	/**
	 * 季节序号
	 */
	@TableField(value = "Season_Seq")
	private Integer seasonSeq;
	/**
	 * 门店序号
	 */
	@TableField(value = "Shop_Seq")
	private Integer shopSeq;
	/**
	 * 分类序号
	 */
	@TableField(value = "Category_Seq")
	private Integer categorySeq;
	/**
	 * 订单编号
	 */
	@TableField(value = "OrderNum")
	private String orderNum;
    /**
     * 订单状态 (0：（未发货）、1：部分发货、 2：已发货、3：已完成、4：已取消)
     */
    @TableField(value = "OrderStatus")
    private Integer orderStatus;
	/**
	 * 入库时间
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
	 * 设置：序号(主键)
	 */
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	/**
	 * 获取：序号(主键)
	 */
	public Integer getSeq() {
		return seq;
	}
	/**
	 * 设置：公司序号
	 */
	public void setCompanySeq(Integer companySeq) {
		this.companySeq = companySeq;
	}
	/**
	 * 获取：公司序号
	 */
	public Integer getCompanySeq() {
		return companySeq;
	}
	/**
	 * 设置：年份
	 */
	public void setYear(Integer year) {
		this.year = year;
	}
	/**
	 * 获取：年份
	 */
	public Integer getYear() {
		return year;
	}
	/**
	 * 设置：季节序号
	 */
	public void setSeasonSeq(Integer seasonSeq) {
		this.seasonSeq = seasonSeq;
	}
	/**
	 * 获取：季节序号
	 */
	public Integer getSeasonSeq() {
		return seasonSeq;
	}
	/**
	 * 设置：门店序号
	 */
	public void setShopSeq(Integer shopSeq) {
		this.shopSeq = shopSeq;
	}
	/**
	 * 获取：门店序号
	 */
	public Integer getShopSeq() {
		return shopSeq;
	}
	/**
	 * 设置：分类序号
	 */
	public void setCategorySeq(Integer categorySeq) {
		this.categorySeq = categorySeq;
	}
	/**
	 * 获取：分类序号
	 */
	public Integer getCategorySeq() {
		return categorySeq;
	}
	/**
	 * 设置：订单编号
	 */
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	/**
	 * 获取：订单编号
	 */
	public String getOrderNum() {
		return orderNum;
	}
	/**
	 * 设置：入库时间
	 */
	public void setInputTime(Date inputTime) {
		this.inputTime = inputTime;
	}
	/**
	 * 获取：入库时间
	 */
	public Date getInputTime() {
		return inputTime;
	}
	/**
	 * 设置：删除标识(0:未删除,1:已删除)
	 */
	public void setDel(Integer del) {
		this.del = del;
	}
	/**
	 * 获取：删除标识(0:未删除,1:已删除)
	 */
	public Integer getDel() {
		return del;
	}
	public Integer getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
}
