package io.nuite.modules.order_platform_app.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import io.nuite.datasources.DatabaseNames;

import com.baomidou.mybatisplus.annotations.TableField;

import java.io.Serializable;
import java.util.Date;

/**
 * 每日库存根据详情计算库存总情况
 * 
 * @author wanghao
 * @email barryhippo@163.com
 * @date 2019-03-20 16:23:59
 */
@TableName(DatabaseNames.SR_ORDER_PLATFORM+"YHSR_OP_ShoesData_Daily")
public class ShoesDataDailyEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId(value = "Seq")
	private Long seq;
	/**
	 * $column.comments
	 */
	@TableField(value = "Shop_Seq")
	private Long shopSeq;
	/**
	 * $column.comments
	 */
	@TableField(value = "Shoes_Seq")
	private Long shoesSeq;
	/**
	 * $column.comments
	 */
	@TableField(value = "ShoesId")
	private String shoesId;
	/**
	 * $column.comments
	 */
	@TableField(value = "Color")
	private Long color;
	/**
	 * $column.comments
	 */
	@TableField(value = "Size")
	private Long size;
	/**
	 * $column.comments
	 */
	@TableField(value = "Count")
	private Integer count;
	/**
	 * $column.comments
	 */
	@TableField(value = "UpdateTime")
	private Date updateTime;

	/**
	 * 设置：${column.comments}
	 */
	public void setSeq(Long seq) {
		this.seq = seq;
	}
	/**
	 * 获取：${column.comments}
	 */
	public Long getSeq() {
		return seq;
	}
	/**
	 * 设置：${column.comments}
	 */
	public void setShopSeq(Long shopSeq) {
		this.shopSeq = shopSeq;
	}
	/**
	 * 获取：${column.comments}
	 */
	public Long getShopSeq() {
		return shopSeq;
	}
	/**
	 * 设置：${column.comments}
	 */
	public void setShoesSeq(Long shoesSeq) {
		this.shoesSeq = shoesSeq;
	}
	/**
	 * 获取：${column.comments}
	 */
	public Long getShoesSeq() {
		return shoesSeq;
	}
	/**
	 * 设置：${column.comments}
	 */
	public void setShoesId(String shoesId) {
		this.shoesId = shoesId;
	}
	/**
	 * 获取：${column.comments}
	 */
	public String getShoesId() {
		return shoesId;
	}
	/**
	 * 设置：${column.comments}
	 */
	public void setColor(Long color) {
		this.color = color;
	}
	/**
	 * 获取：${column.comments}
	 */
	public Long getColor() {
		return color;
	}
	/**
	 * 设置：${column.comments}
	 */
	public void setSize(Long size) {
		this.size = size;
	}
	/**
	 * 获取：${column.comments}
	 */
	public Long getSize() {
		return size;
	}
	/**
	 * 设置：${column.comments}
	 */
	public void setCount(Integer count) {
		this.count = count;
	}
	/**
	 * 获取：${column.comments}
	 */
	public Integer getCount() {
		return count;
	}
	/**
	 * 设置：${column.comments}
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：${column.comments}
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
}
