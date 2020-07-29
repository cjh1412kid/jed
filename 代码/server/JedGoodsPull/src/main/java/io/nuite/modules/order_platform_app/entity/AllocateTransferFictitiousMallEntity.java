package io.nuite.modules.order_platform_app.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.nuite.datasources.DatabaseNames;

import java.io.Serializable;
import java.util.Date;

/**
 * ${comments}
 * 
 * @author wanghao
 * @email barryhippo@163.com
 * @date 2019-09-10 15:46:51
 */
@TableName(DatabaseNames.SR_ORDER_PLATFORM + "YHSR_OP_AllocateTransfer_FictitiousMall")
public class AllocateTransferFictitiousMallEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号(主键)
	 */
	@TableId(value = "Seq")
	private Integer seq;
	/**
	 * 门店序号
	 */
	@TableField(value = "Shop_Seq")
	private Integer shopSeq;
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
	 * 调出数量
	 */
	@TableField(value = "TransferOutNum")
	private Integer transferOutNum;
	/**
	 * 时间
	 */
	@TableField(value = "InputTime")
	private Date inputTime;
	/**
	 * 删除标识(0:未删除,1:已删除)
	 */
	//@TableLogic
	@TableField(value = "Del")
	private Integer del;
	
	/**
	 * 调出类型（0：滞销自动调出 1：用户手动调出）
	 */
	@TableField(value = "TransferOutType")
	private Integer transferOutType;
	
	
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
	 * 设置：调出数量
	 */
	public void setTransferOutNum(Integer transferOutNum) {
		this.transferOutNum = transferOutNum;
	}
	/**
	 * 获取：调出数量
	 */
	public Integer getTransferOutNum() {
		return transferOutNum;
	}
	/**
	 * 设置：时间
	 */
	public void setInputTime(Date inputTime) {
		this.inputTime = inputTime;
	}
	/**
	 * 获取：时间
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
	public Integer getTransferOutType() {
		return transferOutType;
	}
	public void setTransferOutType(Integer transferOutType) {
		this.transferOutType = transferOutType;
	}
}
