package io.nuite.modules.order_platform_app.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.nuite.datasources.DatabaseNames;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 0
 *
 * @author admin
 * @email
 * @date 2018-04-11 11:29:58
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@TableName(DatabaseNames.SR_ORDER_PLATFORM + "YHSR_OP_ShoesDataCopy")
public class ShoesDataCopyEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 序号(主键)
     */
    @TableId
    @TableField(value = "Seq")
    private Integer seq;
    /**
     * 鞋子序号(外键:YHSR_Goods_Shoes表)
     */
    @TableField(value = "Shoes_Seq")
    private Integer shoesSeq;
    /**
     * 鞋子序号(外键:YHSR_Goods_Shoes表)
     */
    @TableField(value = "Shop_Seq")
    private Integer shopSeq;
    /**
     * 颜色序号(外键:YHSR_Goods_Color表)
     */
    @TableField(value = "Color_Seq")
    private Integer colorSeq;
    /**
     * 尺码
     */
    @TableField(value = "Size_Seq")
    private Integer sizeSeq;
    /**
     * 总数量
     */
    @TableField(value = "Num")
    private Integer num;
    /**
     * 库存修改时间
     */
    @TableField(value = "StockDate")
    private Date stockDate;
    /**
     * 库存量
     */
    @TableField(value = "Stock")
    private Integer stock;
    /**
     * 备注
     */
    @TableField(value = "Remark")
    private String remark;
    /**
     * 删除标识(0:未删除,1:已删除)
     */
    @TableLogic
    @TableField(value = "Del")
    private Integer del;
    /**
     * 公司序号
     */
    @TableField(value = "Company_Seq")
    private Integer companySeq;

    //自定义字段
    /**
     * 颜色名称
     */
    @TableField(exist = false)
    private String colorName;

    /**
     * 颜色代码
     */
    @TableField(exist = false)
    private String code;

    /**
     * 尺码名称
     */
    @TableField(exist = false)
    private String sizeName;
    /**
     * 尺码代码
     */
    @TableField(exist = false)
    private String sizeCode;

    /**
     * 门店名称
     */
    @TableField(exist = false)
    private String shopName;
    
    
    @TableField(value = "goods_type")
    private Integer goodsType;
    
}
