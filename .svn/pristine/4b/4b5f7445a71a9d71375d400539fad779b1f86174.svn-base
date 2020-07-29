package io.nuite.modules.order_platform_app.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import io.nuite.datasources.DatabaseNames;
import lombok.Data;

import java.util.Date;

/**
 * 公告
 *
 * @Author: yangchuang
 * @Date: 2019/3/14 13:58
 * @Version: 1.0
 */

@Data
@TableName(DatabaseNames.SR_ORDER_PLATFORM + "YHSR_OP_Notice")
public class NoticeEntity {

    @TableId("Seq")
    private Integer seq;

    /**
     * 公告标题
     */
    @TableField("Name")
    private String name;

    @TableField("ImageName")
    private String imageName;

    @TableField("CompanySeq")
    private Integer companySeq;

    /**
     * 0:未使用 1:已使用
     */
    @TableField("isUsed")
    private Integer isUsed;
    
    @TableField("InputTime")
    private Date inputTime;

    @TableLogic
    @TableField("Del")
    private Integer del;

    /**
     * 公告详情页
     */
    @TableField(value = "DetailedPage", strategy = FieldStrategy.IGNORED)
    private String detailedPage;

    //---------------------------

    @TableField(exist = false)
    private String imgSrc;
}
