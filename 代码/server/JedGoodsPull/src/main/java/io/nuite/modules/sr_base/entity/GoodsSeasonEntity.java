package io.nuite.modules.sr_base.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import io.nuite.datasources.DatabaseNames;

import java.io.Serializable;
import java.util.Date;

/**
 * ${comments}
 * 
 * @author wanghao
 * @email barryhippo@163.com
 * @date 2019-03-07 16:24:44
 */
@TableName(DatabaseNames.SR_BASE + "YHSR_Goods_Season")
public class GoodsSeasonEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号(主键)
	 */
	@TableId(value = "Seq")
	private Integer seq;
	/**
	 * 品牌序号(外键:YHSR_Base_Brand表)
	 */
	@TableField(value = "Brand_Seq")
	private Integer brandSeq;
	/**
	 * 季节名字
	 */
	@TableField(value = "SeasonName")
	private String seasonName;
	/**
	 * 入库时间
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
	 * 设置：品牌序号(外键:YHSR_Base_Brand表)
	 */
	public void setBrandSeq(Integer brandSeq) {
		this.brandSeq = brandSeq;
	}
	/**
	 * 获取：品牌序号(外键:YHSR_Base_Brand表)
	 */
	public Integer getBrandSeq() {
		return brandSeq;
	}
	/**
	 * 设置：季节名字
	 */
	public void setSeasonName(String seasonName) {
		this.seasonName = seasonName;
	}
	/**
	 * 获取：季节名字
	 */
	public String getSeasonName() {
		return seasonName;
	}
	/**
	 * 设置：入库时间
	 */
	public void setInputTime(Date inputTime) {
		this.inputTime = inputTime;
	}
	/**
	 * 获取：入库时间
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
