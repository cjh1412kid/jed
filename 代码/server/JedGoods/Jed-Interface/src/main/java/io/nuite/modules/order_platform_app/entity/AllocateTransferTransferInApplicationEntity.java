package io.nuite.modules.order_platform_app.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import io.nuite.datasources.DatabaseNames;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableLogic;

import java.io.Serializable;
import java.util.Date;

/**
 * 调入申请表
 * 
 * @author wanghao
 * @email barryhippo@163.com
 * @date 2019-09-12 08:52:00
 */
@TableName(DatabaseNames.SR_ORDER_PLATFORM + "YHSR_OP_AllocateTransfer_TransferInApplication")
public class AllocateTransferTransferInApplicationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号(主键)
	 */
	@TableId(value = "Seq")
	private Integer seq;
	/**
	 * 申请调入方的门店序号
	 */
	@TableField(value = "InShop_Seq")
	private Integer inShopSeq;
	/**
	 * 申请人序号
	 */
	@TableField(value = "InUser_Seq")
	private Integer inUserSeq;
	/**
	 * 鞋子序号(外键:YHSR_Goods_Shoes表)
	 */
	@TableField(value = "Shoes_Seq")
	private Integer shoesSeq;
	/**
	 * 调出方门店序号
	 */
	@TableField(value = "OutShop_Seq")
	private Integer outShopSeq;
	/**
	 * 状态(0: 待处理 1:已同意 2:已拒绝)
	 */
	@TableField(value = "State")
	private Integer state;
	/**
	 * 备注
	 */
	@TableField(value = "Remark")
	private String remark;
	/**
	 * 处理人序号
	 */
	@TableField(value = "OutUser_Seq")
	private Integer outUserSeq;
	/**
	 * 处理时间
	 */
	@TableField(value = "HandleTime")
	private Date handleTime;
	/**
	 * 时间
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
	 * 类型 0 app,1 后台
	 */
	@TableField(value = "Type")
	private Integer type;
	/**
	 * 类型 0 app,1 后台
	 */
	@TableField(value = "ErpOrderNum")
	private String erpOrderNum;
	/**
	 * 类型 0 app,1 后台
	 */
	@TableField(value = "ExpressCompany_Seq")
	private Integer expressCompanySeq;
	/**
	 * 类型 0 app,1 后台
	 */
	@TableField(value = "ExpressNum")
	private String expressNum;
	
	@TableField(exist = false)
	private String expressName;

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
	 * 设置：申请调入方的门店序号
	 */
	public void setInShopSeq(Integer inShopSeq) {
		this.inShopSeq = inShopSeq;
	}
	/**
	 * 获取：申请调入方的门店序号
	 */
	public Integer getInShopSeq() {
		return inShopSeq;
	}
	/**
	 * 设置：申请人序号
	 */
	public void setInUserSeq(Integer inUserSeq) {
		this.inUserSeq = inUserSeq;
	}
	/**
	 * 获取：申请人序号
	 */
	public Integer getInUserSeq() {
		return inUserSeq;
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
	 * 设置：调出方门店序号
	 */
	public void setOutShopSeq(Integer outShopSeq) {
		this.outShopSeq = outShopSeq;
	}
	/**
	 * 获取：调出方门店序号
	 */
	public Integer getOutShopSeq() {
		return outShopSeq;
	}
	/**
	 * 设置：状态(0: 待处理 1:已同意 2:已拒绝)
	 */
	public void setState(Integer state) {
		this.state = state;
	}
	/**
	 * 获取：状态(0: 待处理 1:已同意 2:已拒绝)
	 */
	public Integer getState() {
		return state;
	}
	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：备注
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * 设置：处理人序号
	 */
	public void setOutUserSeq(Integer outUserSeq) {
		this.outUserSeq = outUserSeq;
	}
	/**
	 * 获取：处理人序号
	 */
	public Integer getOutUserSeq() {
		return outUserSeq;
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
	public Date getHandleTime() {
		return handleTime;
	}
	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}

	public String getErpOrderNum() {
		return erpOrderNum;
	}

	public void setErpOrderNum(String erpOrderNum) {
		this.erpOrderNum = erpOrderNum;
	}

	public Integer getExpressCompanySeq() {
		return expressCompanySeq;
	}

	public void setExpressCompanySeq(Integer expressCompanySeq) {
		this.expressCompanySeq = expressCompanySeq;
	}

	public String getExpressNum() {
		return expressNum;
	}

	public void setExpressNum(String expressNum) {
		this.expressNum = expressNum;
	}
	public String getExpressName() {
		return expressName;
	}
	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}
	
	
}
