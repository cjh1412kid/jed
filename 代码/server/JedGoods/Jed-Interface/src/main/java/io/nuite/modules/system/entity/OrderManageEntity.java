package io.nuite.modules.system.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.nuite.datasources.DatabaseNames;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订货会管理
 * </p>
 *
 * @author yangchuang
 * @since 2019-03-22
 */
@Data
@TableName(DatabaseNames.SR_BASE + "YHSR_Order_Manage")
public class OrderManageEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableId(value = "Seq", type = IdType.AUTO)
    private Integer seq;
    @TableField("Company_Seq")
    private Integer companySeq;
    
    @TableField("GoodID")
    private String goodID;
    @TableField("Year")
    private Integer year;
    @TableField("Season_Seq")
    private Integer seasonSeq;
    
    @TableField("Color_Seq")
    private Integer colorSeq;
    /**
     * 品类，风格
     */
    @TableField("Category_Seq")
    private Integer categorySeq;
    /**
     * 大类
     */
    @TableField("DaLei_Seq")
    private Integer daLeiSeq;
    /**
     * 中类
     */
    @TableField("ZhongLei_Seq")
    private Integer zhLeiSeq;
    
    @TableField("InputTime")
    private Date inputTime;
    @TableField("Del")
    @TableLogic
    private Integer del;
    
    /*************自定义属性*****************/
    @TableField(exist = false)
    private String categoryName;
    /**
     * 大类
     */
    @TableField(exist = false)
    private String daLeiName;
    /**
     * 中类
     */
    @TableField(exist = false)
    private String zhLeiName;
    
    @TableField(exist = false)
    private String seasonName;
    
    @TableField(exist = false)
    private String colorName;
    
}
