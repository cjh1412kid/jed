package io.nuite.modules.sr_base.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import io.nuite.datasources.DatabaseNames;

import java.io.Serializable;

/**
 * @Author: yangchuang
 * @Date: 2018/8/10 15:10
 * @Version: 1.0
 * @Description: 用于esmart接口查询用户key和secret
 */

@TableName(DatabaseNames.SR_BASE + "YHSR_Base_ESmart")
public class BaseESmartEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("Seq")
    private Integer seq;

    @TableField("FactoryUser_Seq")
    private Integer factoryUserSeq;

    @TableField("ESmartKey")
    private String smartKey;

    @TableField("ESmartSecret")
    private String smartSecret;

    @TableField("Company_Seq")
    private Integer companySeq;

    @TableField("User_Prefix")
    private String userPrefix;

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Integer getFactoryUserSeq() {
        return factoryUserSeq;
    }

    public void setFactoryUserSeq(Integer factoryUserSeq) {
        this.factoryUserSeq = factoryUserSeq;
    }

    public String getSmartKey() {
        return smartKey;
    }

    public void setSmartKey(String smartKey) {
        this.smartKey = smartKey;
    }

    public String getSmartSecret() {
        return smartSecret;
    }

    public void setSmartSecret(String smartSecret) {
        this.smartSecret = smartSecret;
    }

    public Integer getCompanySeq() {
        return companySeq;
    }

    public void setCompanySeq(Integer companySeq) {
        this.companySeq = companySeq;
    }

    public String getUserPrefix() {
        return userPrefix;
    }

    public void setUserPrefix(String userPrefix) {
        this.userPrefix = userPrefix;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BaseESmartEntity{");
        sb.append("seq=").append(seq);
        sb.append(", factoryUserSeq=").append(factoryUserSeq);
        sb.append(", smartKey='").append(smartKey).append('\'');
        sb.append(", smartSecret='").append(smartSecret).append('\'');
        sb.append(", companySeq=").append(companySeq);
        sb.append('}');
        return sb.toString();
    }
}
