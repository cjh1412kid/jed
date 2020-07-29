package io.nuite.modules.order_platform_app.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;

import io.nuite.datasources.DatabaseNames;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 0
 * 
 * @author admin
 * @email 
 * @date 2018-04-11 11:29:57
 */
@TableName(DatabaseNames.SR_ORDER_PLATFORM+"YHSR_OP_OrderProducts")
public class OrderProductsEntity implements Serializable {
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
	 * 鞋子数据序号(外键:YHSR_OP_ShoesData表)
	 */
	@TableField(value = "ShoesData_Seq")
	private Integer shoesDataSeq;
    /**
     * 订单产品价格
     */
    @TableField(value = "ProductPrice")
    private BigDecimal productPrice;
	/**
	 * 数量
	 */
	@TableField(value = "BuyCount")
	private Integer buyCount;
	/**
	 * 已发货数量
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
	public void setOrderSeq(Integer orderSeq) {
		this.orderSeq = orderSeq;
	}
	/**
	 * 获取：订单序号(外键:YHSR_OP_Order表)
	 */
	public Integer getOrderSeq() {
		return orderSeq;
	}
	/**
	 * 设置：鞋子数据序号(外键:YHSR_OP_ShoesData表)
	 */
	public void setShoesDataSeq(Integer shoesDataSeq) {
		this.shoesDataSeq = shoesDataSeq;
	}
	/**
	 * 获取：鞋子数据序号(外键:YHSR_OP_ShoesData表)
	 */
	public Integer getShoesDataSeq() {
		return shoesDataSeq;
	}
	public BigDecimal getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
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
	public Integer getDeliverNum() {
		return deliverNum;
	}
	public void setDeliverNum(Integer deliverNum) {
		this.deliverNum = deliverNum;
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
