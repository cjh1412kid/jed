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
@TableName(DatabaseNames.SR_ORDER_PLATFORM+"YHSR_OP_OrderStatisticsModel")
public class OrderStatisticsModelEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 序号(主键)
	 */
	@TableId
	@TableField(value = "Seq")
	private Integer seq;
	/**
	 * 公司序号
	 */
	@TableField(value = "Company_Seq")
	private Integer companySeq;
	/**
	 * 模板名称
	 */
	@TableField(value = "ModelName")
	private String modelName;
	/**
	 * 行字段
	 */
	@TableField(value = "LineField")
	private String lineField;
	/**
	 * 汇总字段
	 */
	@TableField(value = "SummaryField")
	private String summaryField;
	/**
	 * 是否默认（0：否 1：是）
	 */
	@TableField(value = "IsDefault")
	private Integer IsDefault;
	/**
	 * 插入时间
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
	public Integer getCompanySeq() {
		return companySeq;
	}
	public void setCompanySeq(Integer companySeq) {
		this.companySeq = companySeq;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getLineField() {
		return lineField;
	}
	public void setLineField(String lineField) {
		this.lineField = lineField;
	}
	public String getSummaryField() {
		return summaryField;
	}
	public void setSummaryField(String summaryField) {
		this.summaryField = summaryField;
	}
	public Integer getIsDefault() {
		return IsDefault;
	}
	public void setIsDefault(Integer isDefault) {
		IsDefault = isDefault;
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
