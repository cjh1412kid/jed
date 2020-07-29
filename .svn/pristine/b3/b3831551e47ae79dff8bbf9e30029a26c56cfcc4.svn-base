package io.nuite.modules.order_platform_app.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.nuite.datasources.DatabaseNames;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 门店导购
 * </p>
 *
 * @author yangchuang
 * @since 2019-03-19
 */

@ApiModel(value = "门店导购")
@Data
@TableName(DatabaseNames.SR_BASE + "YHSR_Base_Sales")
public class SalesEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(hidden = true)
    @TableId(value = "Seq", type = IdType.AUTO)
    private Integer seq;
    
    @ApiModelProperty(value = "导购名称", required = true)
    @TableField("Name")
    private String name;
    
    @ApiModelProperty(value = "年龄")
    @TableField("Age")
    private Integer age;
    
    @ApiModelProperty(value = "手机号")
    @TableField("Tel")
    private String tel;
    
    @ApiModelProperty(value = "标记")
    @TableField("Flag")
    private String flag;
    
    @ApiModelProperty(hidden = true)
    @TableField("ShopSeq")
    private Integer shopSeq;
    
    @ApiModelProperty(hidden = true)
    @TableField("Creator")
    private Integer creator;
    
    @ApiModelProperty(hidden = true)
    @TableField("InputTime")
    private Date inputTime;
    
    @ApiModelProperty(hidden = true)
    @TableField("Del")
    @TableLogic
    private Integer del;

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String shopName;

    /**
     * 籍贯
     */
    @TableField(value = "Native_Place")
    private String nativePlace;

    /**
     * 身份证号
     */
    @TableField(value = "ID_Card_No")
    private String idCardNo;

    /**
     * 入职时间
     */
    @TableField(value = "Hire_Date")
    private String hireDate;

    /**
     * 性别
     */
    @TableField(value = "Sex")
    private String sex;

    /**
     * 职务
     */
    @TableField(value = "Duty")
    private String duty;

    /**
     * 是否离职 0未离职 1已离职
     */
    @TableField(value = "Is_Dimission")
    private Integer isDimission;
    
    
    
    
    @TableField(value = "Number")
    private String number;
    
    
    
    
}
