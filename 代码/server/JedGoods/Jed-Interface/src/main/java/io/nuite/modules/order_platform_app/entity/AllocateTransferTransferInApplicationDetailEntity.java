package io.nuite.modules.order_platform_app.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import io.nuite.datasources.DatabaseNames;

import com.baomidou.mybatisplus.annotations.TableField;

import java.io.Serializable;

/**
 * 调入申请表详情
 * 
 * @author wanghao
 * @email barryhippo@163.com
 * @date 2019-09-12 08:52:00
 */
@TableName(DatabaseNames.SR_ORDER_PLATFORM + "YHSR_OP_AllocateTransfer_TransferInApplicationDetail")
public class AllocateTransferTransferInApplicationDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号(主键)
	 */
	@TableId(value = "Seq")
	private Integer seq;
	/**
	 * 鞋子序号(外键:YHSR_Goods_Shoes表)
	 */
	@TableField(value = "Application_Seq")
	private Integer applicationSeq;
	/**
	 * 鞋子序号(外键:YHSR_Goods_Shoes表)
	 */
	@TableField(value = "Shoes_Seq")
	private Integer shoesSeq;
	/**
	 * 颜色序号(外键:YHSR_Goods_Color表)
	 */
	@TableField(value = "Color_Seq")
	private Integer colorSeq;
	/**
	 * 尺码
	 */
	@TableField(value = "Size_Seq")
	private Integer sizeSeq;
	/**
	 * 申请调入数量
	 */
	@TableField(value = "Num")
	private Integer num;
	
	
    /**
     * 调出门店序号
     */
    @TableField(exist = false)
    private Integer outShopSeq;
    
	
	

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
	 * 设置：鞋子序号(外键:YHSR_Goods_Shoes表)
	 */
	public void setApplicationSeq(Integer applicationSeq) {
		this.applicationSeq = applicationSeq;
	}
	/**
	 * 获取：鞋子序号(外键:YHSR_Goods_Shoes表)
	 */
	public Integer getApplicationSeq() {
		return applicationSeq;
	}
	/**
	 * 设置：鞋子序号(外键:YHSR_Goods_Shoes表)
	 */
	public void setShoesSeq(Integer shoesSeq) {
		this.shoesSeq = shoesSeq;
	}
	/**
	 * 获取：鞋子序号(外键:YHSR_Goods_Shoes表)
	 */
	public Integer getShoesSeq() {
		return shoesSeq;
	}
	/**
	 * 设置：颜色序号(外键:YHSR_Goods_Color表)
	 */
	public void setColorSeq(Integer colorSeq) {
		this.colorSeq = colorSeq;
	}
	/**
	 * 获取：颜色序号(外键:YHSR_Goods_Color表)
	 */
	public Integer getColorSeq() {
		return colorSeq;
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
	 * 设置：申请调入数量
	 */
	public void setNum(Integer num) {
		this.num = num;
	}
	/**
	 * 获取：申请调入数量
	 */
	public Integer getNum() {
		return num;
	}
	public Integer getOutShopSeq() {
		return outShopSeq;
	}
	public void setOutShopSeq(Integer outShopSeq) {
		this.outShopSeq = outShopSeq;
	}
	
}
