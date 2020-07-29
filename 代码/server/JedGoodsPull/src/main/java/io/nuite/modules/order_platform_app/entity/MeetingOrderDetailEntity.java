package io.nuite.modules.order_platform_app.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import io.nuite.datasources.DatabaseNames;

import java.io.Serializable;
import java.util.Date;

/**
 * ${comments}
 * 
 * @author wanghao
 * @email barryhippo@163.com
 * @date 2019-03-27 15:20:17
 */
@TableName(DatabaseNames.SR_ORDER_PLATFORM+"YHSR_OP_MeetingOrderDetail")
public class MeetingOrderDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号(主键)
	 */
	@TableId(value = "Seq")
	private Integer seq;
	/**
	 * 订单序号(外键:YHSR_OP_Order表)
	 */
	@TableField(value = "MeetingOrder_Seq")
	private Integer meetingOrderSeq;
	/**
	 * 订货会鞋子序号
	 */
	@TableField(value = "OrderShoes_Seq")
	private Integer orderShoesSeq;
	/**
	 * 货号
	 */
	@TableField(value = "GoodID")
	private String goodID;
	/**
	 * 尺码
	 */
	@TableField(value = "Size_Seq")
	private Integer sizeSeq;
	/**
	 * 数量
	 */
	@TableField(value = "BuyCount")
	private Integer buyCount;
	/**
	 * 已发货量
	 */
	@TableField(value = "DeliverNum")
	private Integer deliverNum;
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
	public void setMeetingOrderSeq(Integer meetingOrderSeq) {
		this.meetingOrderSeq = meetingOrderSeq;
	}
	/**
	 * 获取：订单序号(外键:YHSR_OP_Order表)
	 */
	public Integer getMeetingOrderSeq() {
		return meetingOrderSeq;
	}
	/**
	 * 设置：订货会鞋子序号
	 */
	public void setOrderShoesSeq(Integer orderShoesSeq) {
		this.orderShoesSeq = orderShoesSeq;
	}
	/**
	 * 获取：订货会鞋子序号
	 */
	public Integer getOrderShoesSeq() {
		return orderShoesSeq;
	}
	/**
	 * 设置：货号
	 */
	public void setGoodID(String goodID) {
		this.goodID = goodID;
	}
	/**
	 * 获取：货号
	 */
	public String getGoodID() {
		return goodID;
	}
	/**
	 * 设置：尺码
	 */
	public void setSizeSeq(Integer sizeSeq) {
		this.sizeSeq = sizeSeq;
	}
	/**
	 * 获取：尺码
	 */
	public Integer getSizeSeq() {
		return sizeSeq;
	}
	/**
	 * 设置：数量
	 */
	public void setBuyCount(Integer buyCount) {
		this.buyCount = buyCount;
	}
	/**
	 * 获取：数量
	 */
	public Integer getBuyCount() {
		return buyCount;
	}
	/**
	 * 设置：已发货量
	 */
	public void setDeliverNum(Integer deliverNum) {
		this.deliverNum = deliverNum;
	}
	/**
	 * 获取：已发货量
	 */
	public Integer getDeliverNum() {
		return deliverNum;
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
