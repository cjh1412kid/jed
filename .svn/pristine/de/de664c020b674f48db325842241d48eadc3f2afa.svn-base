package io.nuite.modules.order_platform_app.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import io.nuite.datasources.DatabaseNames;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ${comments}
 *
 * @author wanghao
 * @email barryhippo@163.com
 * @date 2019-03-26 10:00:56
 */

@Data
@TableName(DatabaseNames.SR_ORDER_PLATFORM + "YHSR_OP_SievePlateDistribute")
public class SievePlateDistributeEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 序号(主键)
     */
    @TableId(value = "Seq")
    private Integer seq;
    /**
     * 公司序号
     */
    @TableField(value = "Company_Seq")
    private Integer companySeq;
    /**
     * 年份
     */
    @TableField(value = "Year")
    private Integer year;
    /**
     * 季节序号
     */
    @TableField(value = "Season_Seq")
    private Integer seasonSeq;
    /**
     * 货号
     */
    @TableField(value = "GoodID")
    private String goodID;
    /**
     * 门店序号
     */
    @TableField(value = "Shop_Seq")
    private Integer shopSeq;
    /**
     * 分配数量
     */
    @TableField(value = "distributeNum")
    private Integer distributeNum;
    /**
     * 订货会鞋子序号
     */
    @TableField(value = "OrderShoes_Seq")
    private Integer orderShoesSeq;
    /**
     * 分类序号
     */
    @TableField(value = "Category_Seq")
    private Integer categorySeq;
    /**
     * 入库时间
     */
    @TableField(value = "InputTime")
    private Date inputTime;
    /**
     * 删除标识(0:未删除,1:已删除)
     */
    @TableLogic
    @TableField(value = "Del")
    private Integer del;
    /**
     * 门店配码单序号，可判断是否门店已配码
     */
    @TableField(exist = false)
    private Integer shopAllotSeq;
    /**
     * 是否存在货号订单
     */
    @TableField(exist = false)
    private Integer goodsAllotSeq;
    
    @TableField(exist = false)
    private String shopName;
    
    @TableField(exist = false)
    private String seasonName;
    
    @TableField(exist = false)
    private String categoryName;
}
