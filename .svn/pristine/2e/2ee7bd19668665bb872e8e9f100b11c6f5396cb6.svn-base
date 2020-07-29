package io.nuite.modules.system.controller;

import io.nuite.modules.sr_base.dao.BaseUserDao;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Controller公共组件
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月9日 下午9:42:26
 */
public abstract class AbstractController {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BaseUserDao baseUserDao;

    /**
     * 登录用户
     */
    protected BaseUserEntity getUser() {
        return (BaseUserEntity) SecurityUtils.getSubject().getPrincipal();
    }

    /**
     * 登录用户seq
     */
    protected Long getUserSeq() {
        return (long) getUser().getSeq();
    }

    /**
     * 登录用户所属的工厂管理员
     */
    protected BaseUserEntity getAdminUser() {
        return baseUserDao.selectAdminUser(getUser().getCompanySeq(), getUser().getBrandSeq());
    }

    /**
     * 登录用户所属的工厂管理员seq
     */
    protected Long getAdminUserSeq() {
        return (long) getAdminUser().getSeq();
    }

}
