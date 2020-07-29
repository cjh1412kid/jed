package io.nuite.modules.system.controller;

import io.nuite.common.utils.R;
import io.nuite.modules.sr_base.entity.GoodsCategoryEntity;
import io.nuite.modules.system.service.SystemGoodsCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 后台 - 鞋子分类
 * 
 * @author yy
 * @date 2018-05-28 13:47
 */
@RestController
@RequestMapping("/system/goodsCategory")
@Api(tags = "后台 - 鞋子分类", description = "鞋子分类")
public class SystemGoodsCategoryController extends AbstractController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SystemGoodsCategoryService systemGoodsCategoryService;

    /**
     * 全部分类
     */
    @GetMapping("list")
    @ApiOperation("全部分类")
    public R list() {
        // 公司序号
        Integer companySeq = getUser().getCompanySeq();
        List<GoodsCategoryEntity> list = systemGoodsCategoryService.getAllGoodsCategoryByCompanySeq(companySeq);
        return R.ok().put("list", list);
    }

    /**
     * 新增分类
     */
    @PostMapping("add")
    @ApiOperation("新增分类")
    public R add(GoodsCategoryEntity goodsCategoryEntity) {
        // 公司序号
        Integer companySeq = getUser().getCompanySeq();
        goodsCategoryEntity.setCompanySeq(companySeq);
        goodsCategoryEntity.setInputTime(new Date());
        Integer seq = systemGoodsCategoryService.addGoodsCategory(goodsCategoryEntity);
        return R.ok("新增成功").put("seq", seq);

    }

    /**
     * 修改分类
     */
    @PostMapping("update")
    @ApiOperation("修改分类")
    public R update(GoodsCategoryEntity goodsCategoryEntity) {
        systemGoodsCategoryService.updateGoodsCategory(goodsCategoryEntity);
        return R.ok("修改成功");
    }

    /**
     * 删除分类
     */
    @GetMapping("delete")
    @ApiOperation("删除分类")
    public R delete(@ApiParam("分类seq") @RequestParam("seq") Integer seq) {
        // 判断该分类下是否有鞋子
        Boolean flag = systemGoodsCategoryService.hasCategoryInCategory(seq);
        if (flag) {
            return R.error("该分类已被使用，不可删除");
        }
        systemGoodsCategoryService.deleteGoodsCategoryAll(seq);
        return R.ok("删除成功");
    }

    /**
     * 全部分类
     */
    @GetMapping("nOcategoryLsit")
    @ApiOperation("底分类")
    public R nOcategoryLsit() {
        List<Integer> list = systemGoodsCategoryService.nOcategoryLsit(getUser().getCompanySeq());
        return R.ok().put("list", list);
    }

}
