package io.nuite.modules.system.entity.order_platform;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import io.nuite.datasources.DatabaseNames;

import java.util.Date;

/**
 * @Author: yangchuang
 * @Date: 2018/8/29 14:11
 * @Version: 1.0
 * @Description:
 */

@TableName(DatabaseNames.SR_BASE+"YHSR_System_Log")
public class SystemLogEntity {

    @TableId("Seq")
    private Integer seq;

    @TableField("Content")
    private String content;

    @TableField("InputTime")
    private Date inputTime;

    @TableField("UpdateTime")
    private Date updateTime;

    @TableLogic
    @TableField("Del")
    private Integer del;

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getInputTime() {
        return inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDel() {
        return del;
    }

    public void setDel(Integer del) {
        this.del = del;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SystemLogEntity{");
        sb.append("seq=").append(seq);
        sb.append(", content='").append(content).append('\'');
        sb.append(", inputTime=").append(inputTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", del=").append(del);
        sb.append('}');
        return sb.toString();
    }
}
