package io.nuite.modules.system.controller;

import com.baomidou.mybatisplus.exceptions.MybatisPlusException;
import io.nuite.common.exception.RRException;
import io.nuite.common.utils.R;
import io.nuite.modules.system.entity.ShoesReplenishEntity;
import io.nuite.modules.system.service.ShoesReplenishService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author yangchuang
 * @email ychuang92@163.com
 * @date 2019-04-29 10:02:01
 */
@RestController
@RequestMapping("/system/shoesreplenish")
@Api(tags = "后台-货品管理-导入补货计划")
public class ShoesReplenishController extends AbstractController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private ShoesReplenishService shoesReplenishService;
    
    @GetMapping("/list/{shoeSeq}")
    public R list(@PathVariable Integer shoeSeq) {
        List<ShoesReplenishEntity> shoesReplenishEntities = shoesReplenishService.queryReplenishPlan(shoeSeq);
        return R.ok(shoesReplenishEntities);
    }
    
    /**
     * 货品管理-导入订货会计划
     *
     * @param excelFile
     * @return
     */
    @RequestMapping("/uploadExcel")
    public R uploadExcel(MultipartFile excelFile) {
        
        if (excelFile == null || excelFile.isEmpty()) {
            return R.error("上传文件不存在");
        }
        
        try {
            
            shoesReplenishService.parseExcel(excelFile, getUser().getCompanySeq());
            
        } catch (MybatisPlusException e) {
            return R.error("数据保存出错！");
        } catch (RRException e) {
            return R.error(e.getMsg());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return R.error("文件解析出错！" + e.getMessage());
        }
        return R.ok();
    }
    
}
