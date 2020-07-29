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
@TableName(DatabaseNames.SR_ORDER_PLATFORM + "YHSR_OP_AllocateTransfer_ShopTempStock")
public class AllocateTransferShopTempStockEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@TableId(value = "Seq", type = IdType.AUTO)
	private Integer seq;
	
	@TableField("Shop_Seq")
	private Integer shopSeq;
	
	@TableField("Temp_Stock")
	private Integer tempStock;
	
	@TableField("InputTime")
	private Date inputTime;
	
	@TableField("Del")
	@TableLogic
	private Integer del;
	
}
