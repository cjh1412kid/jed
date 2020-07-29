package io.nuite.modules.sr_base.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import io.nuite.datasources.DatabaseNames;
import lombok.Data;

/**
 * @Author: yangchuang
 * @Date: 2019/3/8 11:00
 * @Version: 1.0
 */

@Data
@TableName(DatabaseNames.SR_BASE + "YHSR_Base_User_Role")
public class BaseUserRoleEntity {

    @TableField("User_Seq")
    private Integer userSeq;

    @TableField("Role_Seq")
    private Integer roleSeq;

    @TableField(exist = false)
    private String roleCode;
}
