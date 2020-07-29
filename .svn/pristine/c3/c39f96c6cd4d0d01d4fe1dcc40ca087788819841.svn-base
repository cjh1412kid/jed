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
 * @date 2018-04-11 11:29:58
 */
@TableName(DatabaseNames.SR_ORDER_PLATFORM+"YHSR_OP_PushRecord")
public class PushRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	
	/**
	 * 序号(主键)
	 */
	@TableId
	@TableField(value = "Seq")
	private Integer seq;
	/**
	 * 推送方用户序号(外键:YHSR_Base_User表)
	 */
	@TableField(value = "PushUserSeq")
	private Integer pushUserSeq;
	/**
	 * 接收方用户序号(外键:YHSR_Base_User表)
	 */
	@TableField(value = "ReceiveUserSeq")
	private Integer receiveUserSeq;
    /**
     * 接收方账号
     */
    @TableField(value = "AccountName")
    private String accountName;
	/**
	 * 推送类型(1:调拨)
	 */
	@TableField(value = "Type")
	private Integer type;
	/**
	 * 订单序号(如果是订单类型的推送有值，否则为null)
	 */
	@TableField(value = "OrderSeq")
	private Integer orderSeq;
	/**
	 * 推送内容
	 */
	@TableField(value = "Content")
	private String content;
	/**
	 * 是否已读(0:未读，1:已读)
	 */
	@TableField(value = "IsRead")
	private Integer isRead;
	/**
	 * 推送时间
	 */
	@TableField(value = "InputTime")
	private Date inputTime;
	/**
	 * 删除标识(0:未删除,1:已删除)
	 */
	@TableLogic
	@TableField(value = "Del")
	private Integer del;
	
	
	
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	public Integer getPushUserSeq() {
		return pushUserSeq;
	}
	public void setPushUserSeq(Integer pushUserSeq) {
		this.pushUserSeq = pushUserSeq;
	}
	public Integer getReceiveUserSeq() {
		return receiveUserSeq;
	}
	public void setReceiveUserSeq(Integer receiveUserSeq) {
		this.receiveUserSeq = receiveUserSeq;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getOrderSeq() {
		return orderSeq;
	}
	public void setOrderSeq(Integer orderSeq) {
		this.orderSeq = orderSeq;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getIsRead() {
		return isRead;
	}
	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}
	public Date getInputTime() {
		return inputTime;
	}
	public void setInputTime(Date inputTime) {
		this.inputTime = inputTime;
	}
	public Integer getDel() {
		return del;
	}
	public void setDel(Integer del) {
		this.del = del;
	}

	
	
}
