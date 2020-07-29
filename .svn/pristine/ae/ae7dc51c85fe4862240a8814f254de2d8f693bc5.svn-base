package io.nuite.modules.sr_base.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import io.nuite.datasources.DatabaseNames;

import java.io.Serializable;
import java.util.Date;

@TableName(DatabaseNames.SR_BASE + "YHSR_Base_Agent")
public class BaseAgentEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 序号(主键)
     */
    @TableId(value = "Seq")
    private Integer seq;

    @TableField(value = "Brand_Seq")
    private Integer brandSeq;

    @TableField(value = "AgentName")
    private String agentName;

    @TableField(value = "InputTime")
    private Date inputTime;

    @TableLogic
    @TableField(value = "Del")
    private int del;

    @TableField(value = "Remark")
    private String remark;

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getBrandSeq() {
        return brandSeq;
    }

    public void setBrandSeq(Integer brandSeq) {
        this.brandSeq = brandSeq;
    }

    @Override
    public String toString() {
        return "BaseAgentEntity [seq=" + seq + ", brandSeq=" + brandSeq + ", agentName=" + agentName + ", inputTime="
                + inputTime + ", del=" + del + ", remark=" + remark + "]";
    }
    
}
