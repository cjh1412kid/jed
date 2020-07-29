package io.nuite.modules.sr_base.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import io.nuite.datasources.DatabaseNames;

import java.io.Serializable;

/**
 * 0
 *
 * @author admin
 * @email
 * @date 2018-04-11 11:38:09
 */
@TableName(DatabaseNames.SR_BASE+"YHSR_Base_Company")
public class BaseCompanyEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号(主键)
	 */
	@TableId
	@TableField(value = "Seq")
	private Integer seq;
	/**
	 * 公司集团名字
	 */
	@TableField(value = "Name")
	private String name;
	/**
	 * 公司地址
	 */
	@TableField(value = "Address")
	private String address;
	/**
	 * 总公司（父序号）
	 */
	@TableField(value = "ParentSeq")
	private Integer parentSeq;
	/**
	 * 纬度
	 */
	@TableField(value = "Lat")
	private Float lat;
	/**
	 * 经度
	 */
	@TableField(value = "Lng")
	private Float lng;
	/**
	 *公司类型序号
	 */
	@TableField(value = "CompanyType_Seq")
	private Integer companyTypeSeq;
	/**
	 * 描述
	 */
	@TableField(value = "Remark")
	private String remark;
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
	 * 设置：公司集团名字
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：公司集团名字
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：公司地址
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * 获取：公司地址
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * 设置：总公司（父序号）
	 */
	public void setParentSeq(Integer parentSeq) {
		this.parentSeq = parentSeq;
	}
	/**
	 * 获取：总公司（父序号）
	 */
	public Integer getParentSeq() {
		return parentSeq;
	}
	/**
	 * 设置：纬度
	 */
	public void setLat(Float lat) {
		this.lat = lat;
	}
	/**
	 * 获取：纬度
	 */
	public Float getLat() {
		return lat;
	}
	/**
	 * 设置：经度
	 */
	public void setLng(Float lng) {
		this.lng = lng;
	}
	/**
	 * 获取：经度
	 */
	public Float getLng() {
		return lng;
	}

	public Integer getCompanyTypeSeq() {
		return companyTypeSeq;
	}
	public void setCompanyTypeSeq(Integer companyTypeSeq) {
		this.companyTypeSeq = companyTypeSeq;
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
