package io.nuite.modules.order_platform_app.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;

import io.nuite.datasources.DatabaseNames;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 0
 * 
 * @author admin
 * @email 
 * @date 2018-04-11 11:29:57
 */
@TableName(DatabaseNames.SR_ORDER_PLATFORM+"YHSR_OP_CommunityCOMMENT")
public class CommunityCOMMENTEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号(主键)
	 */
	@TableId
	@TableField(value = "Seq")
	private Integer seq;
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
	 * 关联父类Seq
	 */
	@TableField(value = "Parent_Seq")
	private Integer parentSeq;
	/**
	 * 评论内容
	 */
	@TableField(value = "Content")
	private String content;
	/**
	 * 录入时间
	 */
	@TableField(value = "InputTime")
	private Date inputTime;
	/**
	 * 状态(1:正常, 0:删除, 2:停用)

	 */
	@TableField(value = "State")
	private Integer state;
	/**
	 * 删除标识(0:未删除,1:已删除)
	 */
	@TableLogic
	@TableField(value = "Del")
	private Integer del;

	
	
	//自定义字段
	/**
	 * 用户姓名
	 */
    @TableField(exist = false)
    private String userName;
    
    /**
     * 二级评论列表
     */
    @TableField(exist = false)
    private List<CommunityCOMMENTEntity> secondCommentList;
    
    
    
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
	 * 设置：关联父类Seq
	 */
	public void setParentSeq(Integer parentSeq) {
		this.parentSeq = parentSeq;
	}
	/**
	 * 获取：关联父类Seq
	 */
	public Integer getParentSeq() {
		return parentSeq;
	}
	/**
	 * 设置：评论内容
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 获取：评论内容
	 */
	public String getContent() {
		return content;
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
	 * 设置：状态(1:正常, 0:删除, 2:停用)

	 */
	public void setState(Integer state) {
		this.state = state;
	}
	/**
	 * 获取：状态(1:正常, 0:删除, 2:停用)

	 */
	public Integer getState() {
		return state;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<CommunityCOMMENTEntity> getSecondCommentList() {
		return secondCommentList;
	}
	public void setSecondCommentList(List<CommunityCOMMENTEntity> secondCommentList) {
		this.secondCommentList = secondCommentList;
	}
	
}
