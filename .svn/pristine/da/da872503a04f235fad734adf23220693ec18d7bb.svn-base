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
@TableName(DatabaseNames.SR_ORDER_PLATFORM+"YHSR_OP_CommunityRECORD")
public class CommunityRECORDEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号(主键)
	 */
	@TableId
	@TableField(value = "Seq")
	private Integer seq;
	/**
	 * 互动方式(1:浏览, 2:点赞)

	 */
	@TableField(value = "TypeName")
	private Integer typeName;
	/**
	 * 关联用户Seq
	 */
	@TableField(value = "User_Seq")
	private Integer userSeq;
	/**
	 * 关联内容Seq
	 */
	@TableField(value = "Content_Seq")
	private Integer contentSeq;
	/**
	 * 录入时间
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
	 * 设置：互动方式(1:浏览, 2:点赞)

	 */
	public void setTypeName(Integer typeName) {
		this.typeName = typeName;
	}
	/**
	 * 获取：互动方式(1:浏览, 2:点赞)

	 */
	public Integer getTypeName() {
		return typeName;
	}
	/**
	 * 设置：关联用户Seq
	 */
	public void setUserSeq(Integer userSeq) {
		this.userSeq = userSeq;
	}
	/**
	 * 获取：关联用户Seq
	 */
	public Integer getUserSeq() {
		return userSeq;
	}
	/**
	 * 设置：关联内容Seq
	 */
	public void setContentSeq(Integer contentSeq) {
		this.contentSeq = contentSeq;
	}
	/**
	 * 获取：关联内容Seq
	 */
	public Integer getContentSeq() {
		return contentSeq;
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
