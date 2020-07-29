package io.nuite.modules.system.controller;

import io.nuite.common.utils.Constant;
import io.nuite.common.utils.PageUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.system.entity.SysFactoryEntity;
import io.nuite.modules.system.service.SystemFactoryService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 系统工厂账号管理
 */
@RestController
@RequestMapping("/system/factory")
public class SysFactoryController extends AbstractController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SystemFactoryService systemFactoryService;

    /**
     * 所有工厂用户列表
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:factory:list")
    public R list(@RequestParam Map<String, Object> params) {
        //只有超级管理员，才能查看所有管理员列表
        if (getUserSeq() != Constant.SUPER_ADMIN) {
            return R.error("您没有权限哦！");
        }
        PageUtils page = systemFactoryService.queryFactoryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 用户信息
     */
    @GetMapping("/info")
    @RequiresPermissions("sys:factory:info")
    public R info(Long seq) {
        if (getUserSeq() != Constant.SUPER_ADMIN) {
            return R.error("您没有权限哦！");
        }

        if (seq == null) {
            return R.error("参数错误！");
        }
        SysFactoryEntity sysFactoryEntity = systemFactoryService.queryFactory(seq);
        return R.ok().put("factory", sysFactoryEntity);
    }

    /**
     * 保存用户
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:factory:save")
    public R save(@RequestBody SysFactoryEntity factoryEntity) {
        if (getUserSeq() != Constant.SUPER_ADMIN) {
            return R.error("您没有权限哦！");
        }

        if (factoryEntity.getSeq() != null) {
            if (factoryEntity.getCompanySeq() == null || factoryEntity.getBrandSeq() == null) {
                return R.error("修改的数据错误");
            }
        } else {
            if (StringUtils.isBlank(factoryEntity.getCompanyName())
                    || StringUtils.isBlank(factoryEntity.getBrandName())
                    || StringUtils.isBlank(factoryEntity.getAccountName())) {
                return R.error("内容输入不完整");
            }
        }

        try {
            systemFactoryService.saveFactory(factoryEntity);
            return R.ok();
        } catch (Exception e) {
            logger.error("", e);
            return R.error(e.getMessage());
        }

    }

    /**
     * 删除用户
     */
    @PostMapping("/delete")
    @RequiresPermissions("sys:factory:delete")
    public R delete(@RequestBody Long userSeq) {
        if (getUserSeq() != Constant.SUPER_ADMIN) {
            return R.error("您没有权限哦！");
        }

        if (userSeq != Constant.SUPER_ADMIN) {
            return R.error("系统管理员不能删除");
        }

        if (getUserSeq().equals(userSeq)) {
            return R.error("当前用户不能删除");
        }
        try {
            systemFactoryService.deleteFactory(userSeq);
            return R.ok();
        } catch (Exception e) {
            logger.error("", e);
            return R.error(e.getMessage());
        }
    }
}
