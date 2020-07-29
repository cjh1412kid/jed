package io.nuite.modules.sr_base.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.nuite.datasources.DatabaseNames;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;

/**
 * 0
 *
 * @author admin
 * @email
 * @date 2018-04-11 11:38:09
 */
@Data
@TableName(DatabaseNames.SR_BASE + "YHSR_Base_Shop")
public class BaseShopEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 分公司名称
     */
    @TableField(exist = false)
    private String areaName;
    /**
     * 区域名称
     */
    @TableField(exist = false)
    private String regionName;
    /**
     * 门店类型
     */
    @TableField(exist = false)
    private String typeOfStore;
    /**
     * 序号(主键)
     */
    @TableId
    @TableField(value = "Seq")
    private Integer seq;
    /**
     * 关联区域表序号(外键:YHSR_Base_Area表)
     */
    @TableField(value = "Company_Seq")
    private Integer companySeq;
    
    /**
     * 关联区域表序号(外键:YHSR_Base_Area表)
     */
    @TableField(value = "Area_seq")
    private Integer areaSeq;
    /**
     * 门店编号
     */
    @TableField(value = "Id")
    private String id;
    /**
     * 店名
     */
    @TableField(value = "Name")
    private String name;
    /**
     * 地址
     */
    @TableField(value = "Address")
    private String address;
    /**
     * 纬度
     */
    @TableField(value = "Lat")
    private String lat;
    /**
     * 经度
     */
    @TableField(value = "Lng")
    private String lng;

    @TableField(value = "ShopImage")
    private String shopImage;
    /**
     * 安装时间
     */
    @TableField(value = "InstallDate")
    private Date installDate;
    /**
     * 备注
     */
    @TableField(value = "Remark")
    private String remark;
    /**
     * 入库时间
     */
    @TableField(value = "InputTime")
    private Date inputTime;
    /**
     * 门店类型(0:直营,1:联营,2:加盟)
     */
    @TableField(value = "ShopTypeFlag")
    private Integer shopTypeFlag;
    /**
     * 删除标识(0:未删除,1:已删除)
     */
    @TableLogic
    @TableField(value = "Del")
    private Integer del;
    
    @TableField(value = "Region_Seq")
    private Integer regionSeq;

    @TableField(value = "Is_Active")
    private Integer isActive;

    @TableField(exist = false)
    @JsonIgnore
    private MultipartFile shopImageFile;

    /**
     * 分布数量
     */
    @TableField(exist = false)
    private Integer distributeNum;

    @Override
    public String toString() {
        return "BaseShopEntity [areaName=" + areaName + ", typeOfStore=" + typeOfStore + ", seq=" + seq + ", areaSeq="
                + areaSeq + ", id=" + id + ", name=" + name + ", address=" + address + ", lat=" + lat + ", lng=" + lng
                + ", installDate=" + installDate + ", remark=" + remark + ", inputTime=" + inputTime + ", shopTypeFlag="
                + shopTypeFlag + ", del=" + del + "]";
    }


}
