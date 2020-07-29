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
@TableName(DatabaseNames.SR_BASE + "YHSR_Goods_Category")
public class GoodsCategoryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 序号(主键)
     */
    @TableId
    @TableField(value = "Seq")
    private Integer seq;
    /**
     * 分类父节点序号
     */
    @TableField(value = "ParentSeq")
    private Integer parentSeq;
    /**
     * 公司序号(外键:YHSR_Base_Company表)
     */
    @TableField(value = "Company_Seq")
    private Integer companySeq;

    @TableField(value = "Category_Code")
    private String catetoryCode;
    /**
     * 分类名称
     */
    @TableField(value = "Name")
    private String name;
    /**
     * 创建时间
     */
    @TableField(value = "InputTime")
    private Date inputTime;

    /**
     * 是否可见（0:可见 1:不可见）
     */
    @TableField(value = "Visible")
    private Integer visible;

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
     * 设置：分类父节点序号
     */
    public void setParentSeq(Integer parentSeq) {
        this.parentSeq = parentSeq;
    }

    /**
     * 获取：分类父节点序号
     */
    public Integer getParentSeq() {
        return parentSeq;
    }

    /**
     * 设置：公司序号(外键:YHSR_Base_Company表)
     */
    public void setCompanySeq(Integer companySeq) {
        this.companySeq = companySeq;
    }

    /**
     * 获取：公司序号(外键:YHSR_Base_Company表)
     */
    public Integer getCompanySeq() {
        return companySeq;
    }

    /**
     * 设置：分类名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取：分类名称
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

    public Integer getVisible() {
        return visible;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
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

    public String getCatetoryCode() {
        return catetoryCode;
    }

    public void setCatetoryCode(String catetoryCode) {
        this.catetoryCode = catetoryCode;
    }
}
