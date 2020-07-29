package io.nuite.modules.order_platform_app.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import io.nuite.datasources.DatabaseNames;

import java.io.Serializable;

@TableName(DatabaseNames.SR_ORDER_PLATFORM + "YHSR_OP_ShoesCompanyType")
public class ShoesCompanyTypeEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "Seq")
    private Integer seq;

    @TableField(value = "Shoes_Seq")
    private Integer shoesSeq;

    @TableField(value = "CompanyType_Seq")
    private Integer companyTypeSeq;
    @TableField(value = "Remark")
    private String remark;

    @TableLogic
    @TableField(value = "Del")
    private Integer del;

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Integer getShoesSeq() {
        return shoesSeq;
    }

    public void setShoesSeq(Integer shoesSeq) {
        this.shoesSeq = shoesSeq;
    }

    public Integer getCompanyTypeSeq() {
        return companyTypeSeq;
    }

    public void setCompanyTypeSeq(Integer companyTypeSeq) {
        this.companyTypeSeq = companyTypeSeq;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getDel() {
        return del;
    }

    public void setDel(Integer del) {
        this.del = del;
    }
}
