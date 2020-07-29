package io.nuite.modules.system.controller.order_platform;

import com.baomidou.mybatisplus.exceptions.MybatisPlusException;
import io.nuite.common.exception.RRException;
import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.service.AdjustPriceService;
import io.nuite.modules.system.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @Author: yangchuang
 * @Date: 2019/4/11 16:57
 * @Version: 1.0
 */

@Api(tags = "后台-商品调价接口")
@RestController
@RequestMapping("/sys/adjust-price")
public class SysAdjustPriceController extends AbstractController {
    
    @Autowired
    AdjustPriceService adjustPriceService;
    
    @GetMapping("tree")
    @ApiOperation("获取公司下所有门店的调价列表")
    public R list() {
        List<Map<String, Object>> list = adjustPriceService.queryAdjustPriceTree(getUser().getCompanySeq());
        return R.ok().result(list);
    }
    
    @GetMapping("/excel")
    @ApiOperation("根据调价事件名称获取调价表格数据")
    public R getExcelData(@RequestParam("flag") String flag) {
        List<Map<String, Object>> list = adjustPriceService.queryExcelDataByFlag(flag);
        
        System.out.println("根据调价事件名称获取调价表格数据：");
        for (Map<String, Object> map : list) {
            System.out.println(map);
        }
        
        return R.ok().put("list", list);
    }
    
    @PostMapping("/upload/excel")
    @ApiOperation("上传Excel表格文件")
    public R uploadExcel(MultipartFile excelFile, @RequestParam("treeNodeName") String flag) {
        
        System.out.println("上传文件：excelFile = [" + excelFile + "], flag = [" + flag + "]");
        
        if (excelFile.isEmpty()) {
            return R.error("文件不能为空！");
        }
        
        try {
            
            List<String> shopNameList = adjustPriceService.parseExcelToSave(excelFile, getUser(), flag);
            
            if (shopNameList.size() > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("[");
                for (String s : shopNameList) {
                    sb.append(s).append("、");
                }
                String s = sb.toString();
                s = s.substring(0, s.length() - 1) + "]";
                
                return R.ok("以下门店无用户，没有发出消息：" + s);
            }
            
        } catch (MybatisPlusException e) {
            return R.error("数据保存出错！");
        } catch (RRException e) {
            return R.error(e.getMsg());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return R.error("文件解析出错！" + e.getMessage());
        }
        
        return R.ok("Excel导入成功");
    }
    
}
