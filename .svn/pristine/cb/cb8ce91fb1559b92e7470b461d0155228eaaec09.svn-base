package io.nuite.modules.system.entity.order_platform;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import io.nuite.datasources.DatabaseNames;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName(DatabaseNames.SR_ORDER_PLATFORM + "YHSR_OP_AllocateTransfer_Preview")
public class AllocateTransferPreviewEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "Seq", type = IdType.AUTO)
	private Integer seq;

	@TableField("Shoes_Seq")
	private Integer shoesSeq;

	@TableField("Size_Seq")
	private Integer sizeSeq;

	@TableField("InShop_Seq")
	private Integer inShopSeq;

	@TableField("Num")
	private Integer num;

	@TableField("OutShop_Seq")
	private Integer outShopSeq;

	@TableField("CreateUser_Seq")
	private Integer createUserSeq;

	@TableField("InputTime")
	private Date inputTime;

	@TableField("Del")
	@TableLogic
	private Integer del;
	
	@TableField(exist = false)
	private String inShopName;
	
	@TableField(exist = false)
	private String outShopName;
	
}
