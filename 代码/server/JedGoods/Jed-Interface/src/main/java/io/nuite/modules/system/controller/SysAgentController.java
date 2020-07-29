package io.nuite.modules.system.controller;

import io.nuite.common.utils.PageUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.sr_base.entity.BaseAgentEntity;
import io.nuite.modules.sr_base.service.BaseAgentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 代理管理
 */
@RestController
@RequestMapping("/system/agent")
public class SysAgentController extends AbstractController {

    @Autowired
    private BaseAgentService baseAgentService;

    @GetMapping("/list")
    public R list(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit) {
        try {
            PageUtils agentPage = baseAgentService.getLsit(getUser().getBrandSeq(),page,limit);
            return R.ok().put("page", agentPage);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return R.error("服务器异常");
        }
    }

    @PostMapping("save")
    public R save(BaseAgentEntity baseAgentEntity) {
        try {
            baseAgentEntity.setBrandSeq(getUser().getBrandSeq());
            int i = baseAgentService.save(baseAgentEntity);
            return R.ok(i);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return R.error("服务器异常");
        }
    }

    @PostMapping("update")
    public R update(BaseAgentEntity baseAgentEntity) {
        try {
            baseAgentEntity.setBrandSeq(getUser().getBrandSeq());
            int i = baseAgentService.update(baseAgentEntity);
            return R.ok(i);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return R.error("服务器异常");
        }
    }

    @GetMapping("edit")
    public R edit(@RequestParam("seq") Integer seq) {
        try {
            BaseAgentEntity agentEntity = baseAgentService.edit(seq);
            return R.ok().put("agent", agentEntity);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return R.error("服务器异常");
        }
    }

    @GetMapping("delete")
    public R delete(@RequestParam("seq") Integer seq) {
        try {
            int i = baseAgentService.delete(seq);
            return R.ok(i);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return R.error("服务器异常");
        }
    }

}
