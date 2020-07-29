package io.nuite.modules.system.controller.order_platform;

import com.baomidou.mybatisplus.plugins.Page;
import io.nuite.common.utils.PageUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.entity.SplitOrderModelDetailEntity;
import io.nuite.modules.order_platform_app.entity.SplitOrderModelEntity;
import io.nuite.modules.system.controller.AbstractController;
import io.nuite.modules.system.service.order_platform.SplitOrderModelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 后台 - 拆单模板管理
 *
 * @author yy
 * @date 2018-09-05 16:47
 */
@RestController
@RequestMapping("/system/splitOrderModel")
@Api(tags = "后台 - 鞋子尺码", description = "鞋子尺码")
public class SplitOrderModelController extends AbstractController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SplitOrderModelService splitOrderModelService;


    /**
     * 全部模板列表
     */
    @GetMapping("allList")
    @ApiOperation("模板列表")
    public R allList() {
        try {
            //公司序号
            Integer companySeq = getUser().getCompanySeq();

            //分页查询拆单模板
            List<SplitOrderModelEntity> list = splitOrderModelService.getSplitOrderModel(companySeq);

            return R.ok(list);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }

    }

    /**
     * 默认模板
     */
    @GetMapping("defaultModel")
    @ApiOperation("默认模板")
    public R defaultModel() {
        try {
            //公司序号
            Integer companySeq = getUser().getCompanySeq();

            //分页查询拆单模板
            SplitOrderModelEntity model = splitOrderModelService.getDefaultSplitOrderModel(companySeq);

            return R.ok(model);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }

    }


    /**
     * 模板列表
     */
    @GetMapping("list")
    @ApiOperation("模板列表")
    public R list(@ApiParam("页码") @RequestParam("page") Integer pageNum,
                  @ApiParam("每页条数") @RequestParam("limit") Integer pageSize) {
        try {
            //公司序号
            Integer companySeq = getUser().getCompanySeq();

            //分页查询拆单模板
            Page<SplitOrderModelEntity> page = splitOrderModelService.getSplitOrderModelPage(companySeq, pageNum, pageSize);

            PageUtils pageUtils = new PageUtils(page);
            return R.ok().put("page", pageUtils);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }

    }


    /**
     * 编辑
     *
     * @param seq
     * @return
     */
    @GetMapping("edit")
    public R edit(@RequestParam("seq") Integer seq) {
        try {
            SplitOrderModelEntity splitOrderModelEntity = splitOrderModelService.selectById(seq);
            return R.ok().put("splitOrderModel", splitOrderModelEntity);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return R.error("服务器异常");
        }
    }


    /**
     * 步骤编辑
     *
     * @param seq
     * @return
     */
    @GetMapping("editModelDetail")
    public R editModelDetail(@RequestParam("seq") Integer seq) {
        try {
            //公司序号
            Integer companySeq = getUser().getCompanySeq();

            List<Map<String, Object>> splitOrderModelDetailList = splitOrderModelService.getModelDetailMap(companySeq, seq);

            return R.ok().put("modelDetail", splitOrderModelDetailList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }


    /**
     * 新增模板
     */
    @PostMapping("add")
    @ApiOperation("新增模板")
    public R add(@ApiParam("模板名称") @RequestParam("modelName") String modelName,
                 @ApiParam("是否默认") @RequestParam("isDefault") Integer isDefault) {
        try {
            //公司序号
            Integer companySeq = getUser().getCompanySeq();
            if (splitOrderModelService.modelNameExisted(null, companySeq, modelName)) {
                return R.error("模板已存在");
            }

            //新增模板
            SplitOrderModelEntity splitOrderModelEntity = new SplitOrderModelEntity();
            splitOrderModelEntity.setCompanySeq(companySeq);
            splitOrderModelEntity.setModelName(modelName);
            splitOrderModelEntity.setIsDefault(isDefault);
            splitOrderModelEntity.setInputTime(new Date());
            splitOrderModelEntity.setDel(0);
            Integer seq = splitOrderModelService.addSplitOrderModel(splitOrderModelEntity);

            //如果设为默认，修改其他所有模板为非默认
            if (isDefault == 1) {
                splitOrderModelService.setIsNotDefaultExceptSeq(companySeq, seq);
            }
            return R.ok("新增成功").put("seq", seq);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }

    }


    /**
     * 修改模板
     */
    @PostMapping("update")
    @ApiOperation("修改模板")
    public R update(@ApiParam("模板seq") @RequestParam("seq") Integer seq,
                    @ApiParam("模板名称") @RequestParam("modelName") String modelName,
                    @ApiParam("是否默认") @RequestParam("isDefault") Integer isDefault) {
        try {
            //公司序号
            Integer companySeq = getUser().getCompanySeq();
            if (splitOrderModelService.modelNameExisted(seq, companySeq, modelName)) {
                return R.error("模板已存在");
            }

            //修改模板
            SplitOrderModelEntity splitOrderModelEntity = new SplitOrderModelEntity();
            splitOrderModelEntity.setSeq(seq);
            splitOrderModelEntity.setModelName(modelName);
            splitOrderModelEntity.setIsDefault(isDefault);
            splitOrderModelService.updateSplitOrderModel(splitOrderModelEntity);

            //如果设为默认，修改其他所有模板为非默认
            if (isDefault == 1) {
                splitOrderModelService.setIsNotDefaultExceptSeq(companySeq, seq);
            }
            return R.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }

    }


    /**
     * 删除模板
     */
    @GetMapping("delete")
    @ApiOperation("删除模板")
    public R delete(@ApiParam("模板seq") @RequestParam("seq") Integer seq) {
        try {
            splitOrderModelService.deleteSplitOrderModel(seq);
            return R.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }

    }


//    /**
//     * 新增模板步骤
//     */
//    @PostMapping("addModelDetail")
//    @ApiOperation("新增模板步骤")
//    public R addModelDetail(@ApiParam("seq") @RequestParam(value = "seq", required = false) Integer seq,
//    		@ApiParam("模板seq") @RequestParam("modelSeq") Integer modelSeq,
//    		@ApiParam("品牌") @RequestParam(value = "shoesBrand", required = false) String shoesBrand,
//    		@ApiParam("大类") @RequestParam(value = "categorySeq", required = false) String categorySeq,
//    		@ApiParam("波次") @RequestParam(value = "periodSeq", required = false) String periodSeq,
//    		@ApiParam("属性1") @RequestParam(value = "SX1", required = false) String SX1,
//    		@ApiParam("属性2") @RequestParam(value = "SX2", required = false) String SX2,
//    		@ApiParam("属性3") @RequestParam(value = "SX3", required = false) String SX3,
//    		@ApiParam("属性4") @RequestParam(value = "SX4", required = false) String SX4,
//    		@ApiParam("属性5") @RequestParam(value = "SX5", required = false) String SX5,
//    		@ApiParam("属性6") @RequestParam(value = "SX6", required = false) String SX6,
//    		@ApiParam("属性7") @RequestParam(value = "SX7", required = false) String SX7,
//    		@ApiParam("属性8") @RequestParam(value = "SX8", required = false) String SX8,
//    		@ApiParam("属性9") @RequestParam(value = "SX9", required = false) String SX9,
//    		@ApiParam("属性10") @RequestParam(value = "SX10", required = false) String SX10,
//    		@ApiParam("属性11") @RequestParam(value = "SX11", required = false) String SX11,
//    		@ApiParam("属性12") @RequestParam(value = "SX12", required = false) String SX12,
//    		@ApiParam("属性13") @RequestParam(value = "SX13", required = false) String SX13,
//    		@ApiParam("属性14") @RequestParam(value = "SX14", required = false) String SX14,
//    		@ApiParam("属性15") @RequestParam(value = "SX15", required = false) String SX15,
//    		@ApiParam("属性16") @RequestParam(value = "SX16", required = false) String SX16,
//    		@ApiParam("属性17") @RequestParam(value = "SX17", required = false) String SX17,
//    		@ApiParam("属性18") @RequestParam(value = "SX18", required = false) String SX18,
//    		@ApiParam("属性19") @RequestParam(value = "SX19", required = false) String SX19,
//    		@ApiParam("属性20") @RequestParam(value = "SX20", required = false) String SX20) {


    /**
     * 新增、修改模板步骤
     */
    @PostMapping("addOrUpdateModelDetail")
    @ApiOperation("新增、修改模板步骤")
    public R addOrUpdateModelDetail(SplitOrderModelDetailEntity splitOrderModelDetailEntity) {
        try {
            Integer seq;
            if (splitOrderModelDetailEntity.getSeq() == null) { //新增
                seq = splitOrderModelService.addSplitOrderModelDetail(splitOrderModelDetailEntity);
            } else { //修改
                seq = splitOrderModelService.updateSplitOrderModelDetail(splitOrderModelDetailEntity);
            }
            return R.ok("操作成功").put("seq", seq);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }

    }


    /**
     * 删除模板步骤
     */
    @GetMapping("deleteModelDetail")
    @ApiOperation("删除模板步骤")
    public R deleteModelDetail(@ApiParam("模板步骤seq") @RequestParam("seq") Integer seq) {
        try {
            splitOrderModelService.deleteSplitOrderModelDetail(seq);
            return R.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }

    }

}
