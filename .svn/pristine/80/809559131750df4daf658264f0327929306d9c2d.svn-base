package io.nuite.modules.order_platform_app.entity;

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
 * @date 2018-04-11 11:29:58
 */
@Data
@TableName(DatabaseNames.SR_ORDER_PLATFORM + "YHSR_OP_MeetingPlan")
public class MeetingPlanEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 序号(主键)
     */
    @TableId
    @TableField(value = "Seq")
    private Integer seq;
    /**
     * 公司序号
     */
    @TableField(value = "Company_Seq")
    private Integer companySeq;
    /**
     * 用户序号
     */
    @TableField(value = "User_Seq")
    private Integer userSeq;
    /**
     * 分类序号(风格)
     */
    @TableField(value = "Category_Seq")
    private Integer categorySeq;
    /**
     * 分类序号(中类)
     */
    @TableField(value = "ZhLeiSeq")
    private Integer zhLeiSeq;
    /**
     * 分类序号(大类)
     */
    @TableField(value = "daLeiSeq")
    private Integer daLeiSeq;
    /**
     * 计划订货数量
     */
    @TableField(value = "PlanNum")
    private Integer planNum;
    
    /**
     * 计划订货款数(货号数)
     */
    @TableField(value = "PlanGoodsIDs")
    private Integer planGoodsIDs;
    /**
     * 计划上传时间
     */
    @TableField(value = "InputTime")
    private Date inputTime;
    /**
     * 删除标识(0:未删除,1:已删除)
     */
    @TableLogic
    @TableField(value = "Del")
    private Integer del;
    
    /**
     * 门店序号
     */
    @TableField(value = "Shop_Seq")
    private Integer shopSeq;
    /**
     * 年份
     */
    @TableField(value = "Year")
    private Integer year;
    /**
     * 季节
     */
    @TableField(value = "Season_Seq")
    private Integer seasonSeq;
    
    /***************自定义属性********************/
    /**
     * 季节
     */
    @TableField(exist = false)
    private String seasonName;
    /**
     * 风格，品类
     */
    @TableField(exist = false)
    private String categoryName;
    /**
     * 大类
     */
    @TableField(exist = false)
    private String daLeiName;
    /**
     * 中类
     */
    @TableField(exist = false)
    private String zhLeiName;
}
