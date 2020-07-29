package io.nuite.modules.sr_base.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import io.nuite.datasources.DatabaseNames;
import lombok.Data;

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
@TableName(DatabaseNames.SR_BASE+"YHSR_Goods_Size")
public class GoodsSizeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号(主键)
	 */
	@TableId
	@TableField(value = "Seq")
	private Integer seq;
	/**
	 * 公司序号(外键:YHSR_Base_Company表)
	 */
	@TableField(value = "Company_Seq")
	private Integer companySeq;
	/**
	 * 尺码编码
	 */
	@TableField(value = "SizeCode")
	private String sizeCode;
	/**
	 * 颜色名称
	 */
	@TableField(value = "SizeName")
	private String sizeName;
	/**
	 * 创建时间
	 */
	@TableField(value = "InputTime")
	private Date inputTime;
	/**
	 * 删除标识(0:未删除,1:已删除)
	 */
	@TableLogic
	@TableField(value = "Del")
	private Integer del;
}
