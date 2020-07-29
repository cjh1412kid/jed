package io.nuite.modules.system.controller;

import io.nuite.common.exception.RRException;
import io.nuite.common.utils.MapUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.sr_base.entity.BaseAreaEntity;
import io.nuite.modules.sr_base.entity.BaseShopEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.service.BaseAreaService;
import io.nuite.modules.sr_base.service.BaseShopService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 大区、分公司管理
 */
@RestController
@RequestMapping("/system/area")
public class SysAreaController extends AbstractController {

    @Autowired
    private BaseAreaService baseAreaService;

    @Autowired
    private BaseShopService baseShopService;

    /**
     * 所有大区分公司
     */
    @GetMapping("/list")
    public R list() {
        BaseUserEntity curUser = getUser();
        List<BaseAreaEntity> areaEntities = baseAreaService.selectByMap(new MapUtils().put("Brand_Seq", curUser.getBrandSeq()));
        for (BaseAreaEntity areaEntity : areaEntities) {
            BaseAreaEntity parentAreaEntity = baseAreaService.selectById(areaEntity.getParentSeq());
            if (parentAreaEntity != null) {
                areaEntity.setParentName(parentAreaEntity.getName());
            }
        }
        return R.ok().put("list", areaEntities);
    }

    /**
     * 所有大区（分公司选择时使用）
     */
    @GetMapping("/select")
    public List<BaseAreaEntity> select() {
        BaseUserEntity curUser = getUser();
        Map<String, Object> params = new MapUtils().put("Brand_Seq", curUser.getBrandSeq()).put("ParentSeq", 0);
        return baseAreaService.selectByMap(params);
    }

    /**
     * 大区信息
     */
    @PostMapping("/info/{areaSeq}")
    public R info(@PathVariable("areaSeq") Long areaSeq) {
        BaseUserEntity curUser = getUser();
        BaseAreaEntity area = baseAreaService.selectById(areaSeq);
        if (!Objects.equals(area.getBrandSeq(), curUser.getBrandSeq())) {
            area = new BaseAreaEntity();
        }
        return R.ok().put("area", area);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody BaseAreaEntity area) {
        //数据校验
        if (StringUtils.isBlank(area.getName())) {
            throw new RRException("名称不能为空");
        }
        BaseUserEntity curUser = getUser();
        area.setBrandSeq(curUser.getBrandSeq());
        baseAreaService.insert(area);
        return R.ok("新增成功");
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody BaseAreaEntity area) {
        //数据校验
        if (StringUtils.isBlank(area.getName())) {
            throw new RRException("名称不能为空");
        }

        baseAreaService.updateById(area);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete/{areaSeq}")
    public R delete(@PathVariable("areaSeq") Long areaSeq) {
        //判断是否有子菜单或按钮
        BaseUserEntity curUser = getUser();
        Map<String, Object> params = new MapUtils().put("Brand_Seq", curUser.getBrandSeq()).put("ParentSeq", areaSeq);
        List<BaseAreaEntity> areaList = baseAreaService.selectByMap(params);
        if (areaList.size() > 0) {
            return R.error("请先删除分公司");
        }
        Map<String, Object> areaParams = new MapUtils().put("Area_Seq", areaSeq);
        List<BaseShopEntity> shopList = baseShopService.selectByMap(areaParams);
        if (shopList.size() > 0) {
            return R.error("请先删除门店");
        }

        baseAreaService.deleteById(areaSeq.intValue());

        return R.ok();
    }

    /**
     * 全部分类
     */
    @GetMapping("nOcategoryLsit")
    @ApiOperation("底分类")
    public R nOcategoryList() {
        try {
            List<Integer> list = baseAreaService.nOcategoryList(getUser().getBrandSeq());
            return R.ok().put("list", list);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }

}
