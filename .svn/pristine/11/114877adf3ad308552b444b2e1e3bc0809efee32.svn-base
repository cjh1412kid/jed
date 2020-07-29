package io.nuite.modules.system.controller.home_page_management;

import io.nuite.common.utils.MapUtils;
import io.nuite.common.utils.PageInfo;
import io.nuite.common.utils.PageUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.system.controller.AbstractController;
import io.nuite.modules.system.entity.home_page_management.SystemSowingMapEntity;
import io.nuite.modules.system.service.home_page_management.SystemSowingMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 订货app轮播管理
 */
@RestController
@RequestMapping("sowingMap")
public class SystemSowingMapController extends AbstractController {
    @Autowired
    private SystemSowingMapService systemSowingMapService;

    @RequestMapping("/list")
    public R list() {
        try {
            PageUtils companyBrandPage = systemSowingMapService.sowingMapList(getUser().getBrandSeq());
            return R.ok().put("page", companyBrandPage);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }

    @RequestMapping("/goodList")
    public R goodList(@RequestParam Map<String, Object> params) {
        try {
        	PageInfo pageInfo = new PageInfo(params);
            PageUtils companyBrandPage = systemSowingMapService.goodList(getUser().getBrandSeq(), pageInfo);
            return R.ok().put("page", companyBrandPage);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }
    
    @RequestMapping("/periodList")
    public R periodList(@RequestParam Map<String, Object> params) {
        try {
            PageUtils pageUtil = systemSowingMapService.periodList(getUser().getBrandSeq(), params);
            return R.ok().put("page", pageUtil);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }

    @RequestMapping("/edit")
    public R edit(@RequestParam("seq") Integer seq) {
        try {
            SystemSowingMapEntity systemSowingMapEntity = systemSowingMapService.edit(seq);
            return R.ok().put("sowingMap", systemSowingMapEntity);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }

    @RequestMapping("/update")
    public R update(SystemSowingMapEntity systemSowingMapEntity) {
        try {
            if (systemSowingMapEntity != null) {
                systemSowingMapService.update(getUser().getBrandSeq(), systemSowingMapEntity);
                return R.ok();
            }
            return R.error("参数错误！");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }

    @RequestMapping("/save")
    public R save(SystemSowingMapEntity systemSowingMapEntity) {
        try {
            Integer i = systemSowingMapService.getCreatedNumber(getUser().getBrandSeq());
            if (i < 5) {
                systemSowingMapService.save(getUser().getBrandSeq(), systemSowingMapEntity);
                return R.ok();
            }
            return R.error("轮播图最多只能创建5个");
        } catch (Exception e) {
            return R.error("添加失败！");
        }
    }

    @PostMapping("del")
    public R delete(Integer seq) {
        if (seq == null) {
            return R.error("参数错误！");
        }
        try {
            systemSowingMapService.deleteByMap(new MapUtils().put("Brand_Seq", getUser().getBrandSeq()).put("Seq", seq));
            return R.ok();
        } catch (Exception e) {
            return R.error("添加失败！");
        }
    }
}
