package io.nuite.modules.order_platform_app.controller;

import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.annotation.Login;
import io.nuite.modules.order_platform_app.entity.AdjustPriceEntity;
import io.nuite.modules.order_platform_app.service.AdjustPriceService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 调价Controller类
 * @author: jxj
 * @create: 2019-03-30 13:20
 */
@RestController
@RequestMapping("/order/app/adjustPrice")
@Api(tags = "jed调价")
public class AdjustPriceController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AdjustPriceService service;

    @Login
    @ApiOperation("新增调价")
    @PostMapping("/insertAdjustPrice")
    public R insertAdjustPrice(@ApiParam(name = "adjustPriceEntity", value = "调价实体类", required = true) @RequestBody AdjustPriceEntity adjustPriceEntity) {
        try {
            if (service.insert(adjustPriceEntity)) {
                return R.ok("新增成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
        }
        return R.error("新增失败");
    }

    @Login
    @ApiOperation("修改调价")
    @PostMapping("/updateAdjustPrice")
    public R updateAdjustPrice(@ApiParam(name = "adjustPriceEntity", value = "调价实体类", required = true) @RequestBody AdjustPriceEntity adjustPriceEntity) {
        try {
            if (service.updateById(adjustPriceEntity)) {
                return R.ok("修改成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
        }
        return R.error("修改失败");
    }

    @Login
    @ApiOperation("删除调价")
    @PostMapping("/deleteAdjustPrice")
    @ApiImplicitParams({@ApiImplicitParam(name = "seq", value = "调价序号", required = true,paramType = "query")})
    public R deleteAdjustPrice(@RequestParam("seq") Integer seq) {
        try {
            if (service.deleteById(seq)) {
                return R.ok("删除成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
        }
        return R.error("删除失败");
    }

    @Login
    @ApiOperation("获取调价信息下调价列表")
    @GetMapping("/selectAdjustPriceByMessageSeq")
    @ApiImplicitParams({@ApiImplicitParam(name = "messageSeq", value = "消息序号", required = true,paramType = "query")})
    public R selectAdjustPriceByMessageSeq(@RequestParam("messageSeq") Integer messageSeq) {
        try {
            List<AdjustPriceEntity> list = service.selectAdjustPriceByMessageSeq(messageSeq);
            for(int i = 0;i < list.size();i++) {
                list.get(i).setImage(getGoodsShoesPictureUrl(list.get(i).getShoesId()+list.get(i).getImage()));
            }
            Map<String,Object> result = new HashMap<>(10);
            result.put("result",list);
            return R.ok().result(result);
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
        }
        return R.error();
    }
}
