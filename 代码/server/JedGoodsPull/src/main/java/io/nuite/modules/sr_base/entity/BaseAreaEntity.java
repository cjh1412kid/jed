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
@TableName(DatabaseNames.SR_BASE + "YHSR_Base_Area")
public class BaseAreaEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 序号(主键)
     */
    @TableId
    @TableField(value = "Seq")
    private Integer seq;
    /**
     * 区域父节点序号
     */
    @TableField(value = "ParentSeq")
    private Integer parentSeq;
    /**
     * 品牌序号 品牌下的区域(外键:YHSR_Base_Brand表
     */
    @TableField(value = "Brand_Seq")
    private Integer brandSeq;
    /**
     * 区域名称
     */
    @TableField(value = "Name")
    private String name;
    /**
     * 创建时间
     */
    @TableField(value = "InputTime")
    private Date inputTime;
    /**
     * 区域的地理范围
     */
    @TableField(value = "Bound")
    private String bound;
    /**
     * 删除标识(0:未删除,1:已删除)
     */
    @TableLogic
    @TableField(value = "Del")
    private Integer del;

    /**
     * 父级名称
     */
    @TableField(exist = false)
    private String parentName;

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
     * 设置：区域父节点序号
     */
    public void setParentSeq(Integer parentSeq) {
        this.parentSeq = parentSeq;
    }

    /**
     * 获取：区域父节点序号
     */
    public Integer getParentSeq() {
        return parentSeq;
    }

    /**
     * 设置：品牌序号 品牌下的区域(外键:YHSR_Base_Brand表
     */
    public void setBrandSeq(Integer brandSeq) {
        this.brandSeq = brandSeq;
    }

    /**
     * 获取：品牌序号 品牌下的区域(外键:YHSR_Base_Brand表
     */
    public Integer getBrandSeq() {
        return brandSeq;
    }

    /**
     * 设置：区域名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取：区域名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置：创建时间
     */
    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    /**
     * 获取：创建时间
     */
    public Date getInputTime() {
        return inputTime;
    }

    /**
     * 设置：区域的地理范围
     */
    public void setBound(String bound) {
        this.bound = bound;
    }

    /**
     * 获取：区域的地理范围
     */
    public String getBound() {
        return bound;
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

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
