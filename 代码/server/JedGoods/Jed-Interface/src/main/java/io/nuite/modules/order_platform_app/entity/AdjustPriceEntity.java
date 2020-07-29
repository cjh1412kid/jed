package io.nuite.modules.order_platform_app.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.nuite.datasources.DatabaseNames;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description: 调价实体类
 * @author: jxj
 * @create: 2019-03-30 12:58
 */

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName(DatabaseNames.SR_ORDER_PLATFORM + "YHSR_OP_AdjustPrice")
@ApiModel(value = "调价实体类")
public class AdjustPriceEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 序号(主键)
     */
    @TableId(value = "Seq")
    private Integer seq;

    /**
     * 鞋子序号
     */
    @TableField(value = "Shoes_Seq")
    @ApiModelProperty(value = "鞋子序号")
    private Integer shoesSeq;

    /**
     * 消息序号
     */
    @TableField(value = "Message_Seq")
    @ApiModelProperty(value = "消息序号")
    private Integer messageSeq;

    /**
     * 现价
     */
    @TableField(value = "Current_Price")
    private BigDecimal currentPrice;

    /**
     * 原价
     */
    @TableField(value = "Previous_Price")
    private BigDecimal previousPrice;

    /**
     * 鞋子序号
     */
    @TableField(exist = false)
    private String shoesId;

    /**
     * 鞋子货号
     */
    @TableField(exist = false)
    private String goodID;

    /**
     * 鞋子图片
     */
    @TableField(exist = false)
    private String image;

    /**
     * 调价时间
     */
    @TableField(exist = false)
    private String adjustTime;

}
