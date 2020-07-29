package io.nuite.modules.sr_base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;

import io.nuite.datasources.DatabaseNames;

/**
 * 0
 * 
 * @author admin
 * @email 
 * @date 2018-04-11 11:38:09
 */
@TableName(DatabaseNames.SR_BASE+"YHSR_Base_Brand")
public class BaseBrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号(主键)
	 */
	@TableId
	@TableField(value = "Seq")
	private Integer seq;
	/**
	 * 品牌名
	 */
	@TableField(value = "Name")
	private String name;
	/**
	 * 所属公司序号(外键:YHSR_Base_Company表)
	 */
	@TableField(value = "Company_Seq")
	private Integer companySeq;
	/**
	 * 描述
	 */
	@TableField(value = "Remark")
	private String remark;
	
	@TableField(value = "Image")
	private String image;
	
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
	 * 设置：品牌名
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：品牌名
	 */
	public String getName() {
		return name;
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
	 * 设置：描述
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：描述
	 */
	public String getRemark() {
		return remark;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
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
