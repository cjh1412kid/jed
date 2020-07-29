package io.nuite.modules.system.controller.order_platform;

import com.baomidou.mybatisplus.plugins.Page;
import io.nuite.common.utils.PageUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.entity.OrderStatisticsModelEntity;
import io.nuite.modules.system.controller.AbstractController;
import io.nuite.modules.system.service.order_platform.OrderStatisticsModelService;
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
 * 后台 - 订单统计分析模板管理
 *
 * @author yy
 * @date 2018-09-25 16:47
 */
@RestController
@RequestMapping("/system/orderStatisticsModel")
@Api(tags = "后台 - 订单统计分析模板", description = "订单统计分析模板")
public class OrderStatisticsModelController extends AbstractController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderStatisticsModelService orderStatisticsModelService;


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
            List<OrderStatisticsModelEntity> list = orderStatisticsModelService.getOrderStatisticsModel(companySeq);

            return R.ok(list);

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
            Page<OrderStatisticsModelEntity> page = orderStatisticsModelService.getOrderStatisticsModelPage(companySeq, pageNum, pageSize);

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
            OrderStatisticsModelEntity orderStatisticsModelEntity = orderStatisticsModelService.selectById(seq);
            return R.ok().put("orderStatisticsModel", orderStatisticsModelEntity);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return R.error("服务器异常");
        }
    }


    
    /**
     * 新增模板
     */
    @PostMapping("add")
    @ApiOperation("新增模板")
    public R add(@ApiParam("模板名称") @RequestParam("modelName") String modelName,
    		@ApiParam("行字段") @RequestParam("lineField") String lineField,
    		@ApiParam("汇总字段") @RequestParam("summaryField") String summaryField) {
        try {
            //公司序号
            Integer companySeq = getUser().getCompanySeq();
            if (orderStatisticsModelService.modelNameExisted(null, companySeq, modelName)) {
                return R.error("模板已存在");
            }

            //新增模板
            OrderStatisticsModelEntity orderStatisticsModelEntity = new OrderStatisticsModelEntity();
            orderStatisticsModelEntity.setCompanySeq(companySeq);
            orderStatisticsModelEntity.setModelName(modelName);
            orderStatisticsModelEntity.setLineField(lineField);
            orderStatisticsModelEntity.setSummaryField(summaryField);
            orderStatisticsModelEntity.setIsDefault(0);
            orderStatisticsModelEntity.setInputTime(new Date());
            orderStatisticsModelEntity.setDel(0);
            Integer seq = orderStatisticsModelService.addOrderStatisticsModel(orderStatisticsModelEntity);

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
                    @ApiParam("行字段") @RequestParam("lineField") String lineField,
                    @ApiParam("汇总字段") @RequestParam("summaryField") String summaryField) {
        try {
            //公司序号
            Integer companySeq = getUser().getCompanySeq();
            if (orderStatisticsModelService.modelNameExisted(seq, companySeq, modelName)) {
                return R.error("模板已存在");
            }

            //修改模板
            OrderStatisticsModelEntity orderStatisticsModelEntity = new OrderStatisticsModelEntity();
            orderStatisticsModelEntity.setSeq(seq);
            orderStatisticsModelEntity.setModelName(modelName);
            orderStatisticsModelEntity.setLineField(lineField);
            orderStatisticsModelEntity.setSummaryField(summaryField);
            orderStatisticsModelService.updateOrderStatisticsModel(orderStatisticsModelEntity);

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
            orderStatisticsModelService.deleteOrderStatisticsModel(seq);
            return R.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }

    }



}
