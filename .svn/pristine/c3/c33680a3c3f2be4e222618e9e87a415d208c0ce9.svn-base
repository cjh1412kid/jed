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
@TableName(DatabaseNames.SR_ORDER_PLATFORM+"YHSR_OP_MeetingRemind")
public class MeetingRemindEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 序号(主键)
	 */
	@TableId
	@TableField(value = "Seq")
	private Integer seq;
	/**
	 * 用户序号
	 */
	@TableField(value = "User_Seq")
	private Integer userSeq;
	/**
	 * 扫码提醒（0:不提醒,1:提醒）
	 */
	@TableField(value = "ScanRemind")
	private Integer scanRemind;
	/**
	 * 下单提醒（0:不提醒,1:提醒）
	 */
	@TableField(value = "OrderRemind")
	private Integer orderRemind;
	/**
	 * 新增时间（上传计划，设置提醒是新增）
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
	public Integer getUserSeq() {
		return userSeq;
	}
	public void setUserSeq(Integer userSeq) {
		this.userSeq = userSeq;
	}
	public Integer getScanRemind() {
		return scanRemind;
	}
	public void setScanRemind(Integer scanRemind) {
		this.scanRemind = scanRemind;
	}
	public Integer getOrderRemind() {
		return orderRemind;
	}
	public void setOrderRemind(Integer orderRemind) {
		this.orderRemind = orderRemind;
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
