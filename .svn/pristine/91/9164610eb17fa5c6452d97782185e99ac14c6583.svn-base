package io.nuite.modules.order_platform_app.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
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
@TableName(DatabaseNames.SR_ORDER_PLATFORM+"YHSR_OP_PublicityPic")
public class PublicityPicEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号(主键)
	 */
	@TableId
	@TableField(value = "Seq")
	private Integer seq;
	/**
	 * 所属公司序号(外键:YHSR_Base_Company表)
	 */
	@TableField(value = "Company_Seq")
	private Integer companySeq;
	/**
	 * 首页展示图片1
	 */
	@TableField(value = "ImgMain")
	private String imgMain;
	/**
	 * 新品推荐展示图
	 */
	@TableField(value = "ImgNewest")
	private String imgNewest;
	/**
	 * 插入时间
	 */
	@TableField(value = "InputTime")
	private Date inputTime;
	/**
	 * 删除标识(0:未删除,1:已删除)
	 */
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
	 * 设置：所属公司序号(外键:YHSR_Base_Company表)
	 */
	public void setCompanySeq(Integer companySeq) {
		this.companySeq = companySeq;
	}
	/**
	 * 获取：所属公司序号(外键:YHSR_Base_Company表)
	 */
	public Integer getCompanySeq() {
		return companySeq;
	}
	/**
	 * 设置：首页展示图片1
	 */
	public void setImgMain(String imgMain) {
		this.imgMain = imgMain;
	}
	/**
	 * 获取：首页展示图片1
	 */
	public String getImgMain() {
		return imgMain;
	}
	/**
	 * 设置：新品推荐展示图
	 */
	public void setImgNewest(String imgNewest) {
		this.imgNewest = imgNewest;
	}
	/**
	 * 获取：新品推荐展示图
	 */
	public String getImgNewest() {
		return imgNewest;
	}
	/**
	 * 设置：插入时间
	 */
	public void setInputTime(Date inputTime) {
		this.inputTime = inputTime;
	}
	/**
	 * 获取：插入时间
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
