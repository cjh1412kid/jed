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
@TableName(DatabaseNames.SR_ORDER_PLATFORM+"YHSR_OP_SplitOrderModelDetail")
public class SplitOrderModelDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 序号(主键)
	 */
	@TableId
	@TableField(value = "Seq")
	private Integer seq;
	/**
	 * 模板序号
	 */
	@TableField(value = "Model_Seq")
	private Integer modelSeq;
    /**
     * 鞋子品牌
     */
    @TableField(value = "ShoesBrand")
    private String shoesBrand;
    /**
     * 鞋子类别序号
     */
    @TableField(value = "Category_Seq")
    private String categorySeq;
    /**
     * 波次序号(外键:YHSR_Goods_Period表)
     */
    @TableField(value = "Period_Seq")
    private String periodSeq;
    /**
     * 属性1~20
     */
    @TableField(value = "SX1")
    private String SX1;
    @TableField(value = "SX2")
    private String SX2;
    @TableField(value = "SX3")
    private String SX3;
    @TableField(value = "SX4")
    private String SX4;
    @TableField(value = "SX5")
    private String SX5;
    @TableField(value = "SX6")
    private String SX6;
    @TableField(value = "SX7")
    private String SX7;
    @TableField(value = "SX8")
    private String SX8;
    @TableField(value = "SX9")
    private String SX9;
    @TableField(value = "SX10")
    private String SX10;
    @TableField(value = "SX11")
    private String SX11;
    @TableField(value = "SX12")
    private String SX12;
    @TableField(value = "SX13")
    private String SX13;
    @TableField(value = "SX14")
    private String SX14;
    @TableField(value = "SX15")
    private String SX15;
    @TableField(value = "SX16")
    private String SX16;
    @TableField(value = "SX17")
    private String SX17;
    @TableField(value = "SX18")
    private String SX18;
    @TableField(value = "SX19")
    private String SX19;
    @TableField(value = "SX20")
    private String SX20;
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
	public Integer getModelSeq() {
		return modelSeq;
	}
	public void setModelSeq(Integer modelSeq) {
		this.modelSeq = modelSeq;
	}
	public String getShoesBrand() {
		return shoesBrand;
	}
	public void setShoesBrand(String shoesBrand) {
		this.shoesBrand = shoesBrand;
	}
	public String getCategorySeq() {
		return categorySeq;
	}
	public void setCategorySeq(String categorySeq) {
		this.categorySeq = categorySeq;
	}
	public String getPeriodSeq() {
		return periodSeq;
	}
	public void setPeriodSeq(String periodSeq) {
		this.periodSeq = periodSeq;
	}
	public String getSX1() {
		return SX1;
	}
	public void setSX1(String sX1) {
		SX1 = sX1;
	}
	public String getSX2() {
		return SX2;
	}
	public void setSX2(String sX2) {
		SX2 = sX2;
	}
	public String getSX3() {
		return SX3;
	}
	public void setSX3(String sX3) {
		SX3 = sX3;
	}
	public String getSX4() {
		return SX4;
	}
	public void setSX4(String sX4) {
		SX4 = sX4;
	}
	public String getSX5() {
		return SX5;
	}
	public void setSX5(String sX5) {
		SX5 = sX5;
	}
	public String getSX6() {
		return SX6;
	}
	public void setSX6(String sX6) {
		SX6 = sX6;
	}
	public String getSX7() {
		return SX7;
	}
	public void setSX7(String sX7) {
		SX7 = sX7;
	}
	public String getSX8() {
		return SX8;
	}
	public void setSX8(String sX8) {
		SX8 = sX8;
	}
	public String getSX9() {
		return SX9;
	}
	public void setSX9(String sX9) {
		SX9 = sX9;
	}
	public String getSX10() {
		return SX10;
	}
	public void setSX10(String sX10) {
		SX10 = sX10;
	}
	public String getSX11() {
		return SX11;
	}
	public void setSX11(String sX11) {
		SX11 = sX11;
	}
	public String getSX12() {
		return SX12;
	}
	public void setSX12(String sX12) {
		SX12 = sX12;
	}
	public String getSX13() {
		return SX13;
	}
	public void setSX13(String sX13) {
		SX13 = sX13;
	}
	public String getSX14() {
		return SX14;
	}
	public void setSX14(String sX14) {
		SX14 = sX14;
	}
	public String getSX15() {
		return SX15;
	}
	public void setSX15(String sX15) {
		SX15 = sX15;
	}
	public String getSX16() {
		return SX16;
	}
	public void setSX16(String sX16) {
		SX16 = sX16;
	}
	public String getSX17() {
		return SX17;
	}
	public void setSX17(String sX17) {
		SX17 = sX17;
	}
	public String getSX18() {
		return SX18;
	}
	public void setSX18(String sX18) {
		SX18 = sX18;
	}
	public String getSX19() {
		return SX19;
	}
	public void setSX19(String sX19) {
		SX19 = sX19;
	}
	public String getSX20() {
		return SX20;
	}
	public void setSX20(String sX20) {
		SX20 = sX20;
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
