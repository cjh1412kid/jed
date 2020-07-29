package io.nuite.modules.sr_base.entity;

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
 * @date 2018-04-11 11:38:09
 */
@TableName(DatabaseNames.SR_BASE + "YHSR_Goods_Period")
public class GoodsPeriodEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 序号(主键)
     */
    @TableId
    @TableField(value = "Seq")
    private Integer seq;
    /**
     * 品牌序号(外键:YHSR_Base_Brand表)
     */
    @TableField(value = "Brand_Seq")
    private Integer brandSeq;
    /**
     * 波次名字
     */
    @TableField(value = "Name")
    private String name;
    /**
     * 年份
     */
    @TableField(value = "Year")
    private Integer year;
    /**
     * 上架销售时间
     */
    @TableField(value = "SaleDate")
    private Date saleDate;
    /**
     * 入库时间
     */
    @TableField(value = "InputTime")
    private Date inputTime;
    /**
     * 订货会开始时间
     */
    @TableField(value = "MeetingStartTime")
    private Date meetingStartTime;
    /**
     * 订货会结束时间
     */
    @TableField(value = "MeetingEndTime")
    private Date meetingEndTime;
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
     * 设置：波次名字
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取：波次名字
     */
    public String getName() {
        return name;
    }

    /**
     * 设置：年份
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    /**
     * 获取：年份
     */
    public Integer getYear() {
        return year;
    }

    /**
     * 设置：上架销售时间
     */
    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    /**
     * 获取：上架销售时间
     */
    public Date getSaleDate() {
        return saleDate;
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

    public Date getMeetingStartTime() {
		return meetingStartTime;
	}

	public void setMeetingStartTime(Date meetingStartTime) {
		this.meetingStartTime = meetingStartTime;
	}

	public Date getMeetingEndTime() {
		return meetingEndTime;
	}

	public void setMeetingEndTime(Date meetingEndTime) {
		this.meetingEndTime = meetingEndTime;
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

    @Override
    public String toString() {
        return "GoodsPeriodEntity [seq=" + seq + ", brandSeq=" + brandSeq + ", name=" + name + ", year=" + year
                + ", saleDate=" + saleDate + ", inputTime=" + inputTime + ", del=" + del + "]";
    }

}
