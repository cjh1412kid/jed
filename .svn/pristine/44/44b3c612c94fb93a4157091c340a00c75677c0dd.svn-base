package io.nuite.modules.system.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统用户Token
 */
@TableName("YHSR_Other_UserToken")
public class SystemUserTokenEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //用户ID
    @TableId(value = "userSeq", type = IdType.INPUT)
    private Long userSeq;
    //token
    @TableField(value = "token")
    private String token;
    //过期时间
    @TableField(value = "expireTime")
    private Date expireTime;
    //更新时间
    @TableField(value = "updateTime")
    private Date updateTime;

    /**
     * 设置：用户ID
     */
    public void setUserSeq(Long userSeq) {
        this.userSeq = userSeq;
    }

    /**
     * 获取：用户ID
     */
    public Long getUserSeq() {
        return userSeq;
    }

    /**
     * 设置：token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 获取：token
     */
    public String getToken() {
        return token;
    }

    /**
     * 设置：过期时间
     */
    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    /**
     * 获取：过期时间
     */
    public Date getExpireTime() {
        return expireTime;
    }

    /**
     * 设置：更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取：更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }
}
