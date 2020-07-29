package io.nuite.modules.system.entity.home_page_management;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.nuite.datasources.DatabaseNames;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;

@TableName(DatabaseNames.SR_BASE + "YHSR_Home_Carousel")
public class SystemSowingMapEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @TableField(exist = false)
    private MultipartFile imageFile;

    /**
     * 序号(主键)
     */
    @TableId(value = "Seq")
    private Integer seq;

    /**
     * Image 图片名
     */
    @TableField(value = "Image")
    private String image;

    /**
     * Type 1: 鞋子列表 / 2：分类列表
     */
    @TableField(value = "Type")
    private Integer type;

    /**
     * Link_Seq 关联的Seq （鞋子的Seq或是分类的Seq）
     */
    @TableField(value = "Link_Seq")
    private String linkSeq;

    /**
     * Del 删除标签
     */
    @TableLogic
    @TableField(value = "Del")
    private Integer del;

    /**
     * InputTime 插入时间
     */
    @TableField(value = "InputTime")
    private Date inputTime;

    /**
     * Brand_Seq 品牌Seq
     */
    @TableField(value = "Brand_Seq")
    private Integer brandSeq;

    public Integer getSeq() {
        return seq;
    }

    public String getImage() {
        return image;
    }

    public Integer getType() {
        return type;
    }

    public String getLinkSeq() {
        return linkSeq;
    }

    public Integer getDel() {
        return del;
    }

    public Date getInputTime() {
        return inputTime;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setLinkSeq(String linkSeq) {
        this.linkSeq = linkSeq;
    }

    public void setDel(Integer del) {
        this.del = del;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }

    public Integer getBrandSeq() {
        return brandSeq;
    }

    public void setBrandSeq(Integer brandSeq) {
        this.brandSeq = brandSeq;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    @Override
    public String toString() {
        return "SystemSowingMapEntity [imageFile=" + ", seq=" + seq + ", image=" + image + ", type=" + type
                + ", linkSeq=" + linkSeq + ", del=" + del + ", inputTime=" + inputTime + ", brandSeq=" + brandSeq + "]";
    }
}
