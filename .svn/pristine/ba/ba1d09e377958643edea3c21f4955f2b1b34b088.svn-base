package io.nuite.modules.system.controller;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;

import io.nuite.common.utils.PageUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.sr_base.entity.GoodsPeriodEntity;
import io.nuite.modules.system.service.SysPeriodService;

/**
 * 后台 - 货品波次管理
 * 
 * @author fl
 * @date 2018-06-04 16:47
 */
@RestController
@RequestMapping("system/period")
public class SysPeriodController extends AbstractController {

    @Autowired
    private SysPeriodService sysPeriodService;

    /**
     * 波次列表
     */
    @GetMapping("list")
    public R list(@RequestParam("page") Integer pageNum, @RequestParam("limit") Integer pageSize) {
        try {
            Page<GoodsPeriodEntity> page = sysPeriodService.getPeriodPage(getUser().getBrandSeq(), pageNum, pageSize);
            PageUtils pageUtils = new PageUtils(page);
            return R.ok().put("page", pageUtils);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }

    @PostMapping("add")
    public R add(@RequestBody GoodsPeriodEntity goodsPeriodEntity) {
        try {
            if (goodsPeriodEntity == null) {
                return R.error("数据格式不正确");
            }
            if(goodsPeriodEntity.getMeetingEndTime() != null) {
            	Calendar cal = Calendar.getInstance();
            	cal.setTime(goodsPeriodEntity.getMeetingEndTime());
            	cal.set(Calendar.HOUR_OF_DAY, 23);
            	cal.set(Calendar.MINUTE, 59);
            	cal.set(Calendar.SECOND, 59);
            	goodsPeriodEntity.setMeetingEndTime(cal.getTime());
            }
            sysPeriodService.addGoodsPeriod(getUser().getBrandSeq(), goodsPeriodEntity);
            return R.ok("添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }

    @PostMapping("update")
    public R update(@RequestBody GoodsPeriodEntity goodsPeriodEntity) {
        try {
            if (goodsPeriodEntity == null || goodsPeriodEntity.getSeq() == null) {
                return R.error("数据格式不正确");
            }
            if(goodsPeriodEntity.getMeetingEndTime() != null) {
            	Calendar cal = Calendar.getInstance();
            	cal.setTime(goodsPeriodEntity.getMeetingEndTime());
            	cal.set(Calendar.HOUR_OF_DAY, 23);
            	cal.set(Calendar.MINUTE, 59);
            	cal.set(Calendar.SECOND, 59);
            	goodsPeriodEntity.setMeetingEndTime(cal.getTime());
            }
            sysPeriodService.updateGoodsPeriod(goodsPeriodEntity);
            return R.ok("修改成功！");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }

    /**
     * 删除
     */
    @GetMapping("delete")
    public R delete(@RequestParam("seq") Integer seq) {
        try {
            // 判断该波次下是否有鞋子
            Boolean flag = sysPeriodService.hasShoesInPeriod(seq);
            if (flag) {
                return R.error("该波次已被使用，不可删除");
            }
            sysPeriodService.deleteGoodsPeriod(seq);
            return R.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }

    @GetMapping("edit")
    public R edit(@RequestParam("seq") Integer seq) {
        try {
            GoodsPeriodEntity goodsPeriodEntity = sysPeriodService.edit(seq);
            return R.ok().put("goodsPeriod", goodsPeriodEntity);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }
}
