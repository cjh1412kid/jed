package io.nuite.modules.order_platform_app.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import io.nuite.datasources.DatabaseNames;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 0
 *
 * @author admin
 * @email
 * @date 2018-04-11 11:29:58
 */
@Data
@TableName(DatabaseNames.SR_ORDER_PLATFORM + "YHSR_OP_ShoesInfo")
public class ShoesInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 序号(主键)
     */
    @TableId(value = "Seq")
    private Integer seq;
    /**
     * 鞋子序号(外键:YHSR_Goods_Shoes表)
     */
    @TableField(value = "Shoes_Seq")
    private Integer shoesSeq;
    /**
     * 吊牌价格1
     */
    @TableField(value = "TagPrice1")
    private BigDecimal tagPrice1;
    /**
     * 吊牌价格2
     */
    @TableField(value = "TagPrice2")
    private BigDecimal tagPrice2;
    /**
     * 吊牌价格3
     */
    @TableField(value = "TagPrice3")
    private BigDecimal tagPrice3;
    /**
     * 成交价格1
     */
    @TableField(value = "DealPrice1")
    private BigDecimal dealPrice1;
    /**
     * 成交价格2
     */
    @TableField(value = "DealPrice2")
    private BigDecimal dealPrice2;
    /**
     * 成交价格3
     */
    @TableField(value = "DealPrice3")
    private BigDecimal dealPrice3;
    /**
     * 批发价格1
     */
    @TableField(value = "WholesalePrice1")
    private BigDecimal wholesalePrice1;
    /**
     * 批发价格2
     */
    @TableField(value = "WholesalePrice2")
    private BigDecimal wholesalePrice2;
    /**
     * 批发价格3
     */
    @TableField(value = "WholesalePrice3")
    private BigDecimal wholesalePrice3;
    /**
     * 是否主推(0:否, 1:是)
     */
    @TableField(value = "IsMainpush")
    private Integer isMainpush;
    /**
     * 是否新品(0:否, 1:是)
     */
    @TableField(value = "IsNewest")
    private Integer isNewest;
    /**
     * 鞋子搜索次数
     */
    @TableField(value = "SearchTimes")
    private Integer searchTimes;
    /**
     * 是否上架（0:下架 1:上架）
     */
    @TableField(value = "OnSale")
    private Integer onSale;
    /**
     * 上架时间
     */
    @TableField(value = "OnSaleTime")
    private Date onSaleTime;
    /**
     * 下架时间
     */
    @TableField(value = "OffSaleTime")
    private Date offSaleTime;
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
}
