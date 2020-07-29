package io.nuite.modules.system.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import io.nuite.datasources.DatabaseNames;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ${comments}
 * 
 * @author wanghao
 * @email barryhippo@163.com
 * @date 2019-05-16 13:13:28
 */
@Data
@TableName(DatabaseNames.SR_ORDER_PLATFORM + "YHSR_OP_ShoesArrived")
public class ShoesArrivedEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号(主键)
	 */
	@TableId(value = "Seq")
	private Integer seq;
	/**
	 * 补货序号
	 */
	@TableField(value = "Replenish_Seq")
	private Integer replenishSeq;
	/**
	 * 到货数量
	 */
	@TableField(value = "ArrivedNum")
	private Integer arrivedNum;
	/**
	 * 到货时间
	 */
	@TableField(value = "ArrivedTime")
	private Date arrivedTime;
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
	/**
	 * 设置：补货序号
	 */
	public void setReplenishSeq(Integer replenishSeq) {
		this.replenishSeq = replenishSeq;
	}
	/**
	 * 获取：补货序号
	 */
	public Integer getReplenishSeq() {
		return replenishSeq;
	}
	/**
	 * 设置：到货数量
	 */
	public void setArrivedNum(Integer arrivedNum) {
		this.arrivedNum = arrivedNum;
	}
	/**
	 * 获取：到货数量
	 */
	public Integer getArrivedNum() {
		return arrivedNum;
	}
	/**
	 * 设置：到货时间
	 */
	public void setArrivedTime(Date arrivedTime) {
		this.arrivedTime = arrivedTime;
	}
	/**
	 * 获取：到货时间
	 */
	public Date getArrivedTime() {
		return arrivedTime;
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
