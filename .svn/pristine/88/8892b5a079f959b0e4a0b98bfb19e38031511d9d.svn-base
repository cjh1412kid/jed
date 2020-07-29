package io.nuite.modules.system.entity.order_platform;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.nuite.datasources.DatabaseNames;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 配码模版
 * </p>
 *
 * @author yangchuang
 * @since 2019-03-29
 */
@Data
@Accessors(chain = true)
@TableName(DatabaseNames.SR_ORDER_PLATFORM + "YHSR_OP_SizeAllotTemplate")
public class SizeAllotTemplateEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableId(value = "Seq", type = IdType.AUTO)
    private Integer seq;
    
    @TableField("Name")
    private String name;
    
    @TableField("CategorySeq")
    private Integer categorySeq;
    
    @TableField("InputTime")
    private Date inputTime;
    
    /**
     * 模版详细内容
     */
    @TableField(exist = false)
    private List<SizeAllotDetailEntity> details;
}
