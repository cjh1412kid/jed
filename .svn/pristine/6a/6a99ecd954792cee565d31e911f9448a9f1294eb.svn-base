package io.nuite.modules.sr_base.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import io.nuite.datasources.DatabaseNames;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: yangchuang
 * @Date: 2018-7-17
 * @Version: 1.0
 * @Description:
 */

@Data
@TableName(DatabaseNames.SR_ORDER_PLATFORM + "YHSR_Ueditor_Record")
public class UEditorContentEntity implements Serializable {
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
     * 分类名称
     */
    @TableField(value = "Name")
    private String name;
    /**
     * 创建时间
     */
    @TableField(value = "InputTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inputTime;

    /**
     * 是否正在被用（0:未使用 1:正在被用）
     */
    @TableField(value = "Used")
    private Integer used;

    /**
     * 删除标识(0:未删除,1:已删除)
     */
    @TableLogic
    @TableField("Del")
    private Integer del;

    @TableField(value = "Content")
    private String content;

    /**
     * 模版类型： 0首页模版；1 未登录模版
     */
    @TableField("UType")
    private String uType;

}
