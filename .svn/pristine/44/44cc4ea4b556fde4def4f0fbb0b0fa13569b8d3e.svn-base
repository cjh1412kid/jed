package io.nuite.modules.system.controller;

import io.nuite.common.utils.PageUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.system.from.SubAccountForm;
import io.nuite.modules.system.service.SysFactorySubService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/system/factory/sub")
public class SysFactorySubController extends AbstractController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SysFactorySubService sysFactorySubService;

    /**
     * 工厂的子账号列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils pageUtils = sysFactorySubService.subAccountList(getAdminUser(), params);
        return R.ok().put("page", pageUtils);
    }

    /**
     * 新增子账号
     */
    @PostMapping("/save")
    @RequiresPermissions("subAccount:add")
    public R save(@RequestBody SubAccountForm subAccountForm) {
        if (StringUtils.isBlank(subAccountForm.getAccount().getAccountName())) {
            return R.error("登录名不能为空");
        }
        sysFactorySubService.newSubAccount(subAccountForm.getAccount(), subAccountForm.getMenu(), getUser());
        return R.ok();
    }

    /**
     * 用户信息
     */
    @GetMapping("/info")
    public R info(Long seq) {
        return sysFactorySubService.querySubFactory(seq);
    }

    /**
     * 更新子账号
     */
    @PostMapping("/update")
    @RequiresPermissions("subAccount:update")
    public R update(@RequestBody SubAccountForm subAccountForm) {
        if (subAccountForm.getAccount().getSeq() == null) {
            return R.error("更新参数错误");
        }
        sysFactorySubService.updateSubAccount(subAccountForm.getAccount(), subAccountForm.getMenu(), getAdminUser());
        return R.ok();
    }

    /**
     * 删除子账号
     */
    @PostMapping("/delete")
    @RequiresPermissions("subAccount:delete")
    public R delete(Long seq) {
        if (seq == null) {
            return R.error("用户不存在！");
        }
        if (getUserSeq().equals(seq)) {
            return R.error("不能删除自己！");
        }
        sysFactorySubService.delSubAccount(seq, getAdminUser());
        return R.ok();
    }
}
