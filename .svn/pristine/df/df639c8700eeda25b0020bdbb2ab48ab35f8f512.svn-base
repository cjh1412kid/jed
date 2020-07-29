package io.nuite.modules.system.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.nuite.common.exception.RRException;
import io.nuite.common.utils.MapUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.sr_base.entity.BaseAreaEntity;
import io.nuite.modules.sr_base.entity.BaseRegionEntity;
import io.nuite.modules.sr_base.entity.BaseShopEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.service.BaseRegionService;
import io.nuite.modules.sr_base.service.BaseShopService;
import io.swagger.annotations.ApiOperation;

/**
 * 大区、分公司管理
 */
@RestController
@RequestMapping("/system/region")
public class SysRegionController extends AbstractController {

    @Autowired
    private BaseRegionService baseRegionService;

    @Autowired
    private BaseShopService baseShopService;

    /**
     * 所有大区分公司
     */
    @GetMapping("/list")
    public R list() {
        BaseUserEntity curUser = getUser();
        List<BaseRegionEntity> regionEntities = baseRegionService.selectByMap(new MapUtils().put("Brand_Seq", curUser.getBrandSeq()));
        for (BaseRegionEntity regionEntity : regionEntities) {
        	BaseRegionEntity parentRegionEntity = baseRegionService.selectById(regionEntity.getParentSeq());
            if (parentRegionEntity != null) {
            	regionEntity.setParentName(parentRegionEntity.getName());
            }
        }
        return R.ok().put("list", regionEntities);
    }

    /**
     * 所有大区（分公司选择时使用）
     */
    @GetMapping("/select")
    public List<BaseRegionEntity> select() {
        BaseUserEntity curUser = getUser();
        Map<String, Object> params = new MapUtils().put("Brand_Seq", curUser.getBrandSeq()).put("ParentSeq", 0);
        return baseRegionService.selectByMap(params);
    }

    /**
     * 大区信息
     */
    @PostMapping("/info/{areaSeq}")
    public R info(@PathVariable("areaSeq") Long areaSeq) {
        BaseUserEntity curUser = getUser();
        BaseRegionEntity region = baseRegionService.selectById(areaSeq);
        if (!Objects.equals(region.getBrandSeq(), curUser.getBrandSeq())) {
        	region = new BaseRegionEntity();
        }
        return R.ok().put("region", region);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody BaseRegionEntity region) {
        //数据校验
        if (StringUtils.isBlank(region.getName())) {
            throw new RRException("名称不能为空");
        }
        BaseUserEntity curUser = getUser();
        region.setBrandSeq(curUser.getBrandSeq());
        baseRegionService.insert(region);
        return R.ok("新增成功");
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody BaseRegionEntity region) {
        //数据校验
        if (StringUtils.isBlank(region.getName())) {
            throw new RRException("名称不能为空");
        }

        baseRegionService.updateById(region);

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
        List<BaseRegionEntity> regionList = baseRegionService.selectByMap(params);
        if (regionList.size() > 0) {
            return R.error("请先删除区域");
        }
        Map<String, Object> areaParams = new MapUtils().put("Area_Seq", areaSeq);
        List<BaseShopEntity> shopList = baseShopService.selectByMap(areaParams);
        if (shopList.size() > 0) {
            return R.error("请先删除门店");
        }

        baseRegionService.deleteById(areaSeq.intValue());

        return R.ok();
    }

    /**
     * 全部分类
     */
    @GetMapping("nOcategoryLsit")
    @ApiOperation("底分类")
    public R nOcategoryList() {
        try {
            List<Integer> list = baseRegionService.nOcategoryList(getUser().getBrandSeq());
            return R.ok().put("list", list);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }

}
