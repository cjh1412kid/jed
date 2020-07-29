package io.nuite.modules.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.nuite.common.utils.R;
import io.nuite.modules.sr_base.entity.GoodsSXOptionEntity;
import io.nuite.modules.sr_base.service.GoodsSXOptionService;
import io.nuite.modules.sr_base.service.GoodsShoesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @Author: yangchuang
 * @Date: 2018/8/13 17:44
 * @Version: 1.0
 * @Description:
 */

@RestController
@RequestMapping("/sxOption")
public class GoodsSxOptionController extends AbstractController {

    @Autowired
    GoodsSXOptionService goodsSXOptionService;

    @Autowired
    GoodsShoesService goodsShoesService;

    @RequestMapping("/list")
    public R getOptList(Integer seq) {
    
        List<GoodsSXOptionEntity> opts = goodsSXOptionService.selectList(new EntityWrapper<GoodsSXOptionEntity>()
            .eq("SX_Seq", seq));
    
        return R.ok().put("list", opts);
    }

    @RequestMapping("/update")
    public R updateOpt(GoodsSXOptionEntity opt) {

        try {
            opt.setInputTime(new Date());
            goodsSXOptionService.updateById(opt);
            return R.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }

    @RequestMapping("/delete")
    public R deleteOpt(Integer seq, String sxid, String code) {

        try {
            int count = goodsShoesService.getCountBySXAndOption(getUser().getCompanySeq(), sxid, code);
            if (count == 0) {
                goodsSXOptionService.deleteById(seq);
                return R.ok();
            }
            return R.error("该选项有关联商品，不能删除");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }

    @RequestMapping("/delAllBySX")
    public R deleteAllOptBySx(Integer sxSeq) {

        try {
            goodsSXOptionService.delete(new EntityWrapper<GoodsSXOptionEntity>()
                    .eq("SX_Seq", sxSeq));
            return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }

    @RequestMapping("/save")
    public R deleteOpt(GoodsSXOptionEntity opt) {

        try {
            goodsSXOptionService.insert(opt);
            return R.ok("创建成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }

}
