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
@TableName(DatabaseNames.SR_ORDER_PLATFORM+"YHSR_OP_OnlineMessage")
public class OnlineMessageEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	@TableField(value = "Seq")
	private Integer seq;
	/**
	 * 发送消息人
	 */
	@TableField(value = "SenderUser_Seq")
	private Integer senderUserSeq;
	/**
	 * 接收消息对象（根据接收对象类型关联用户表或者群组表）
	 */
	@TableField(value = "ReceiveObject_Seq")
	private Integer receiveObjectSeq;
	/**
	 * 接收对象类型 1用户 2群组
	 */
	@TableField(value = "ReceiveObjectType")
	private Integer receiveObjectType;
	/**
	 * 消息内容
	 */
	@TableField(value = "Content")
	private String content;
	/**
	 * 上传的图片路径
	 */
	@TableField(value = "ImgPath")
	private String imgPath;
	/**
	 * 
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
	 * 设置：发送消息人
	 */
	public void setSenderUserSeq(Integer senderUserSeq) {
		this.senderUserSeq = senderUserSeq;
	}
	/**
	 * 获取：发送消息人
	 */
	public Integer getSenderUserSeq() {
		return senderUserSeq;
	}
	/**
	 * 设置：接收消息对象（根据接收对象类型关联用户表或者群组表）
	 */
	public void setReceiveObjectSeq(Integer receiveObjectSeq) {
		this.receiveObjectSeq = receiveObjectSeq;
	}
	/**
	 * 获取：接收消息对象（根据接收对象类型关联用户表或者群组表）
	 */
	public Integer getReceiveObjectSeq() {
		return receiveObjectSeq;
	}
	/**
	 * 设置：接收对象类型 1用户 2群组
	 */
	public void setReceiveObjectType(Integer receiveObjectType) {
		this.receiveObjectType = receiveObjectType;
	}
	/**
	 * 获取：接收对象类型 1用户 2群组
	 */
	public Integer getReceiveObjectType() {
		return receiveObjectType;
	}
	/**
	 * 设置：消息内容
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 获取：消息内容
	 */
	public String getContent() {
		return content;
	}
	/**
	 * 设置：上传的图片路径
	 */
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	/**
	 * 获取：上传的图片路径
	 */
	public String getImgPath() {
		return imgPath;
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
