package io.nuite.modules.system.controller.order_platform;

import com.baomidou.mybatisplus.plugins.Page;
import io.nuite.common.exception.RRException;
import io.nuite.common.utils.ImportMultipartExcelUtil;
import io.nuite.common.utils.PageUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.sr_base.dao.GoodsSXDao;
import io.nuite.modules.sr_base.dao.GoodsSXOptionDao;
import io.nuite.modules.sr_base.service.BaseShopService;
import io.nuite.modules.sr_base.service.GoodsCategoryService;
import io.nuite.modules.system.controller.AbstractController;
import io.nuite.modules.system.service.order_platform.SysMeetingPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 订货会计划
 */
@RestController
@RequestMapping("/order/meetingPlan")
@Api(tags = "后台  - 订货会计划", description = "订货会计划")
public class SysMeetingPlanController extends AbstractController {
    
    @Autowired
    private SysMeetingPlanService sysMeetingPlanService;
    
    @Autowired
    private GoodsCategoryService goodsCategoryService;
    
    @Autowired
    private GoodsSXDao goodsSXDao;
    
    @Autowired
    private GoodsSXOptionDao goodsSXOptionDao;
    
    @Autowired
    BaseShopService baseShopService;
    
    @GetMapping("/shop/list")
    @ApiOperation(value = "获取用户关联公司的门店列表")
    public R orderPartyShopList(@ApiParam("订货计划上传状态（0:全部 1:已上传 2:未上传）") @RequestParam Integer uploadState,
                                @ApiParam("年份") @RequestParam Integer year,
                                @ApiParam("季节") @RequestParam Integer seasonSeq,
                                @ApiParam("页码") @RequestParam Integer page,
                                @ApiParam("每页条数") @RequestParam Integer limit) {
        Page<Map<String, Object>> pageCondition = new Page<>(page, limit);
        Page<Map<String, Object>> shopPlanList = sysMeetingPlanService.getShopPlanList(pageCondition, getUser().getCompanySeq(), seasonSeq, year, uploadState);
        return R.ok().put("page", new PageUtils(shopPlanList));
    }
    
    /**
     * 订货计划详细列表
     */
    @GetMapping("/detail")
    @ApiOperation("订货计划详细列表")
    public R detail(@ApiParam("门店Seq") @RequestParam("shopSeq") Integer shopSeq,
                    @ApiParam("年份") @RequestParam("year") Integer year,
                    @ApiParam("季节") @RequestParam("seasonSeq") Integer seasonSeq,
                    @ApiParam("页码") @RequestParam("page") Integer pageNo,
                    @ApiParam("每页条数") @RequestParam("limit") Integer pageSize) {
        Page<Object> page = new Page<>(pageNo, pageSize);
        
        Page pageResult = sysMeetingPlanService.getShopPlanDetailsList(page, getUser().getCompanySeq(), shopSeq, year, seasonSeq);
        return R.ok().put("page", new PageUtils(pageResult));
    }
    
    @GetMapping("/delete")
    @ApiOperation("删除门店的订货计划")
    public R delete(@ApiParam("年份") @RequestParam Integer year,
                    @ApiParam("季节") @RequestParam Integer seasonSeq,
                    @ApiParam("用户Seq数组") @RequestParam Integer[] shopSeqArr) {
        for (Integer shopSeq : shopSeqArr) {
            sysMeetingPlanService.deleteMeetingPlan(shopSeq, year, seasonSeq);
        }
        return R.ok();
    }
    
    /**
     * 导入订货计划
     */
    @PostMapping("upload")
    @ApiOperation("导入订货计划")
    public R uploadExcel(@ApiParam("门店Seq数组") @RequestParam("shopSeqArr") Integer[] shopSeqArr,
                         @ApiParam("年份") @RequestParam("year") Integer year,
                         @ApiParam("季节") @RequestParam("seasonSeq") Integer seasonSeq,
                         @ApiParam("excel文件") @RequestParam("excelFile") MultipartFile excelFile) {
        try {
            if (shopSeqArr.length == 0) {
                return R.error("请选择订货方！");
            }
            
            //解析excel文件为二维list
            List<List<Object>> list = ImportMultipartExcelUtil.importExcel(excelFile);
            
            if (list != null && list.size() > 0) {
                //检查excel列标题是否正确
                List<Object> head = list.get(0);
                if (head.size() != 5 || head.get(0) == null || !head.get(0).toString().equals("大类")
                    || head.get(1) == null || !head.get(1).toString().equals("中类")
                    || head.get(2) == null || !head.get(2).toString().equals("风格")
                    || head.get(3) == null || !head.get(3).toString().equals("订货款数")
                    || head.get(4) == null || !head.get(4).toString().equals("订货量")) {
                    return R.error("excel表内容不符合要求， 第一行为标题行“大类 中类 风格 订货款数 订货量“");
                }
                
                //批量插入订货计划
                sysMeetingPlanService.addBatchMeetingPlan(shopSeqArr, getUser(), list, year, seasonSeq);
                
                return R.ok("导入成功");
            } else {
                return R.error("文件内容为空！");
            }
            
        } catch (RRException e) {
            return R.error(e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("导入失败");
        }
        
    }
    
}
