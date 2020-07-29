package io.nuite.modules.system.controller.order_platform;

import io.nuite.common.utils.R;
import io.nuite.modules.system.controller.AbstractController;
import io.nuite.modules.system.entity.order_platform.SizeAllotTemplateEntity;
import io.nuite.modules.system.service.order_platform.SizeAllotTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 品类配码模版
 * </p>
 *
 * @author yangchuang
 * @since 2019-03-29
 */

@Api(tags = "后台-品类配码模版")
@RestController
@RequestMapping("/sys/size-allot-template")
public class SizeAllotTemplateController extends AbstractController {
    
    @Autowired
    SizeAllotTemplateService sizeAllotTemplateService;
    
    @GetMapping("/query/{categorySeq}")
    @ApiOperation(value = "查询品类关联的所有配码模版")
    public R queryCategorySizeAllot(@PathVariable Integer categorySeq) {
        List<SizeAllotTemplateEntity> sizeAllotTemplateEntities = sizeAllotTemplateService.listTemplateByCategorySeq(categorySeq);
        return R.ok().result(sizeAllotTemplateEntities);
    }
    
    @PostMapping("/save/{categorySeq}")
    @ApiOperation(value = "创建品类的配码模版")
    public R saveCategorySizeAllot(@RequestBody SizeAllotTemplateEntity sizeAllotTemplateEntity,
                                   @PathVariable Integer categorySeq) {
        
        sizeAllotTemplateEntity.setCategorySeq(categorySeq);
        sizeAllotTemplateService.saveSizeAllotTemplate(sizeAllotTemplateEntity);
        return R.ok(sizeAllotTemplateEntity);
    }
    
    @PostMapping("/update")
    @ApiOperation(value = "更新配码模版")
    public R updateCategorySizeAllot(@RequestBody SizeAllotTemplateEntity sizeAllotTemplateEntity) {
        sizeAllotTemplateService.updateSizeAllotTemplate(sizeAllotTemplateEntity);
        return R.ok("更新成功");
    }
    
    @GetMapping("/del/{templateSeq}")
    @ApiOperation(value = "删除配码模版")
    public R delCategorySizeAllot(@PathVariable Integer templateSeq) {
        sizeAllotTemplateService.deleteSizeAllotTemplate(templateSeq);
        return R.ok("删除成功");
    }
}

