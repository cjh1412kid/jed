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
@TableName(DatabaseNames.SR_ORDER_PLATFORM+"YHSR_OP_OnlineGroup")
public class OnlineGroupEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	@TableField(value = "Seq")
	private Integer seq;
	/**
	 * 群组名称
	 */
	@TableField(value = "GroupName")
	private String groupName;
	/**
	 * 创建人
	 */
	@TableField(value = "CreateUser_Seq")
	private Integer createUserSeq;
	/**
	 * 
	 */
	@TableField(value = "InputTime")
	private Date inputTime;
	/**
	 * 状态1：正常0：删除2：停用
	 */
	@TableField(value = "State")
	private Integer state;
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
	 * 设置：群组名称
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	/**
	 * 获取：群组名称
	 */
	public String getGroupName() {
		return groupName;
	}
	/**
	 * 设置：创建人
	 */
	public void setCreateUserSeq(Integer createUserSeq) {
		this.createUserSeq = createUserSeq;
	}
	/**
	 * 获取：创建人
	 */
	public Integer getCreateUserSeq() {
		return createUserSeq;
	}
	/**
	 * 设置：
	 */
	public void setInputTime(Date inputTime) {
		this.inputTime = inputTime;
	}
	/**
	 * 获取：
	 */
	public Date getInputTime() {
		return inputTime;
	}
	/**
	 * 设置：状态
1：正常
0：删除
2：停用
	 */
	public void setState(Integer state) {
		this.state = state;
	}
	/**
	 * 获取：状态
1：正常
0：删除
2：停用
	 */
	public Integer getState() {
		return state;
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
