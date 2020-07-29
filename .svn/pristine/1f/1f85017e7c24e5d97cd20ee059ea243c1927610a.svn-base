package io.nuite.modules.sr_base.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.nuite.datasources.DatabaseNames;

import java.io.Serializable;

@TableName(DatabaseNames.SR_BASE + "YHSR_System_User_Menu")
public class SysUserMenuEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "Seq")
    private Long seq;

    /**
     * 用户ID
     */
    @TableField(value = "User_Seq")
    private Long userSeq;

    /**
     * 菜单ID
     */
    @TableField(value = "Menu_Seq")
    private Long menuSeq;

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public Long getUserSeq() {
        return userSeq;
    }

    public void setUserSeq(Long userSeq) {
        this.userSeq = userSeq;
    }

    public Long getMenuSeq() {
        return menuSeq;
    }

    public void setMenuSeq(Long menuSeq) {
        this.menuSeq = menuSeq;
    }
}
