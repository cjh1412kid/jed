package io.nuite.modules.order_platform_app.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import io.nuite.datasources.DatabaseNames;

import java.io.Serializable;
import java.util.Date;

/**
 * 0
 * 
 * @author admin
 * @email 
 * @date 2018-04-11 11:29:57
 */
@TableName(DatabaseNames.SR_ORDER_PLATFORM+"YHSR_OP_OrderExpress")
public class OrderExpressEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号(主键)
	 */
	@TableId
	@TableField(value = "Seq")
	private Integer seq;
	/**
	 * 订单序号(外键:YHSR_OP_Order表)
	 */
	@TableField(value = "Order_Seq")
	private Integer orderSeq;
	/**
	 * 物流公司序号
	 */
	@TableField(value = "ExpressCompany_Seq")
	private Integer expressCompanySeq;
	/**
	 * 物流单号
	 */
	@TableField(value = "ExpressNo")
	private String expressNo;
	/**
	 * 大货发货单图片
	 */
	@TableField(value = "ExpressImg")
	private String expressImg;
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
	 * 设置：订单序号(外键:YHSR_OP_Order表)
	 */
	public void setOrderSeq(Integer orderSeq) {
		this.orderSeq = orderSeq;
	}
	/**
	 * 获取：订单序号(外键:YHSR_OP_Order表)
	 */
	public Integer getOrderSeq() {
		return orderSeq;
	}
	public Integer getExpressCompanySeq() {
		return expressCompanySeq;
	}
	public void setExpressCompanySeq(Integer expressCompanySeq) {
		this.expressCompanySeq = expressCompanySeq;
	}
	public String getExpressNo() {
		return expressNo;
	}
	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}
	public String getExpressImg() {
		return expressImg;
	}
	public void setExpressImg(String expressImg) {
		this.expressImg = expressImg;
	}
	/**
	 * 设置：插入时间
	 */
	public void setInputTime(Date inputTime) {
		this.inputTime = inputTime;
	}
	/**
	 * 获取：插入时间
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
}
