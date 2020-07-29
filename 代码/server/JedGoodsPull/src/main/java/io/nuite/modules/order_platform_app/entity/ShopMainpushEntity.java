package io.nuite.modules.order_platform_app.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.nuite.datasources.DatabaseNames;
import lombok.Data;

import java.io.Serializable;

/**
 * ${comments}
 * 
 * @author wanghao
 * @email barryhippo@163.com
 * @date 2019-03-13 17:43:43
 */
@Data
@TableName(DatabaseNames.SR_ORDER_PLATFORM+"YHSR_OP_ShopMainpush")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ShopMainpushEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**	
	 * 序号(主键)
	 */
	@TableId(value = "Seq")
	private Integer seq;
	/**
	 * 门店序号
	 */
	@TableField(value = "Shop_Seq")
	private Integer shopSeq;
	/**
	 * 鞋子序号(外键:YHSR_Goods_Shoes表)
	 */
	@TableField(value = "Shoes_Seq")
	private Integer shoesSeq;
    /**
     * 货号
     */
	@TableField(exist = false)
    private String goodID;
	/**
	 * 主推的门店个数
	 */
	@TableField(exist = false)
    private Integer number;

	/**
	 * 门店名称
	 */
	@TableField(exist = false)
	private String shopName;
}
