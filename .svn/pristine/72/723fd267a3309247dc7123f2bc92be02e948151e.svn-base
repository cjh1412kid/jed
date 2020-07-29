package io.nuite.modules.order_platform_app.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.nuite.datasources.DatabaseNames;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @description: 消息实体类
 * @author: jxj
 * @create: 2019-03-30 11:29
 */

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName(DatabaseNames.SR_ORDER_PLATFORM + "YHSR_OP_Message")
@ApiModel(value = "消息实体类")
public class MessageEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 序号(主键)
     */
    @TableId(value = "Seq")
    private Integer seq;
    
    /**
     * 标题
     */
    @TableField(value = "Title")
    @ApiModelProperty(value = "标题")
    private String title;
    
    /**
     * 内容
     */
    @TableField(value = "Content")
    @ApiModelProperty(value = "内容")
    private String content;
    
    /**
     * 用户序号
     */
    @TableField(value = "User_Seq")
    @ApiModelProperty(value = "用户序号")
    private Integer userSeq;
    
    /**
     * 创建时间
     */
    @TableField(value = "Input_Time")
    private Date inputTime;
    
    /**
     * 删除标记
     */
    @TableLogic
    @TableField(value = "Del", fill = FieldFill.INSERT)
    private Integer del;
    
    /**
     * 消息类型 1，客服消息 2，调价消息 3，系统消息 4，调货消息
     */
    @TableField(value = "Type")
    @ApiModelProperty(value = "消息类型 1，客服消息 2，调价消息 3，系统消息 4，调货消息")
    private Integer type;
    
    /**
     * 创建人
     */
    @TableField(value = "Creator")
    private Integer creator;
    
    /**
     * 是否已读
     */
    @TableField(value = "Is_Read")
    private Integer isRead;
    
    /**
     * 调价事件标记名称，作为查询列表名称
     */
    @TableField(value = "Flag")
    private String flag;
    
    /**
     * 调入申请表seq（调货消息才有）
     */
    @TableField(value = "Application_Seq")
    private Integer applicationSeq;
    
    /**
     * 调货申请类型 1 调入申请 2 调出申请（调货消息才有）
     */
    @TableField(value = "Application_Type")
    private Integer applicationType;
    
    
    
    
    
    
    
    
    /**
     * 调价事件标记名称，作为查询列表名称
     */
    @TableField(exist = false)
    private List<AdjustPriceEntity> details;
    
    /**
     * 门店序号
     */
    @TableField(exist = false)
    private Integer shopSeq;
    
    /**
     * 门店名称
     */
    @TableField(exist = false)
    private String shopName;

    /**
     * 门店名称
     */
    @TableField(exist = false)
    private String name;

    /**
     * 门店名称
     */
    @TableField(exist = false)
    private String imageName;

    /**
     * 门店名称
     */
    @TableField(exist = false)
    private String detailedPage;

    /**
     * 门店名称
     */
    @TableField(exist = false)
    private String imgSrc;
}
