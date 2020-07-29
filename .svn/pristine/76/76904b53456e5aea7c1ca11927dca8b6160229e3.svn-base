package io.nuite.modules.sr_base.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import io.nuite.datasources.DatabaseNames;

import java.io.Serializable;
import java.util.Date;

@TableName(DatabaseNames.SR_BASE + "YHSR_Goods_Brand")
public class GoodsBrandEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 序号(主键)
     */
    @TableId(value = "Seq")
    private Integer seq;

    /**
     * 公司序号
     */
    @TableField(value = "Company_Seq")
    private Integer companySeq;

    /**
     * 品牌代码
     */
    @TableField(value = "BrandCode")
    private String brandCode;

    /**
     * 品牌代码
     */
    @TableField(value = "BrandName")
    private String brandName;

    /**
     * 时间
     */
    @TableField(value = "InputTime")
    private Date inputTime;

    /**
     * 时间
     */
    @TableLogic
    @TableField(value = "Del")
    private int del;

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Integer getCompanySeq() {
        return companySeq;
    }

    public void setCompanySeq(Integer companySeq) {
        this.companySeq = companySeq;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Date getInputTime() {
        return inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    public int getDel() {
        return del;
    }

    public void setDel(int del) {
        this.del = del;
    }
}
