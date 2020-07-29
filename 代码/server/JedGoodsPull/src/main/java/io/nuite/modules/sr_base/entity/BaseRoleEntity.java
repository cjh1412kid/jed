package io.nuite.modules.sr_base.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import io.nuite.datasources.DatabaseNames;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(DatabaseNames.SR_BASE + "YHSR_Base_Role")
public class BaseRoleEntity implements Serializable {

    /**BaseRoleEntity
     * 序号(主键)
     */
    @TableId(value = "Seq")
    private Integer seq;

    @TableField(value = "Name")
    private String name;

    @TableField(value = "Code")
    private String code;

    @TableField(value = "InputTime")
    private Date inputTime;

    @TableLogic
    @TableField(value = "Del")
    private boolean del;

}
