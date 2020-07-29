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
@TableName(DatabaseNames.SR_ORDER_PLATFORM+"YHSR_OP_OnlineGroupMember")
public class OnlineGroupMemberEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	@TableField(value = "Seq")
	private Integer seq;
	/**
	 * 群组序号
	 */
	@TableField(value = "Group_Seq")
	private Integer groupSeq;
	/**
	 * 成员序号
	 */
	@TableField(value = "User_Seq")
	private Integer userSeq;
	/**
	 * 录入时间
	 */
	@TableField(value = "InputTime")
	private Date inputTime;
	/**
	 * 删除标识0未删除，1删除
	 */
	@TableLogic
	@TableField(value = "Del")
	private Integer del;

	
	
	/**
	 * 设置：
	 */
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	/**
	 * 获取：
	 */
	public Integer getSeq() {
		return seq;
	}
	/**
	 * 设置：群组序号
	 */
	public void setGroupSeq(Integer groupSeq) {
		this.groupSeq = groupSeq;
	}
	/**
	 * 获取：群组序号
	 */
	public Integer getGroupSeq() {
		return groupSeq;
	}
	/**
	 * 设置：成员序号
	 */
	public void setUserSeq(Integer userSeq) {
		this.userSeq = userSeq;
	}
	/**
	 * 获取：成员序号
	 */
	public Integer getUserSeq() {
		return userSeq;
	}
	/**
	 * 设置：录入时间
	 */
	public void setInputTime(Date inputTime) {
		this.inputTime = inputTime;
	}
	/**
	 * 获取：录入时间
	 */
	public Date getInputTime() {
		return inputTime;
	}
	/**
	 * 设置：删除标识0未删除，1删除
	 */
	public void setDel(Integer del) {
		this.del = del;
	}
	/**
	 * 获取：删除标识0未删除，1删除
	 */
	public Integer getDel() {
		return del;
	}
}
