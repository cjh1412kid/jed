package io.nuite.modules.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;

import io.nuite.common.utils.PageUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.sr_base.entity.GoodsSeasonEntity;
import io.nuite.modules.sr_base.service.GoodsSeasonService;
import io.swagger.annotations.Api;

import java.util.List;

/**
 * 后台 - 季节管理
 * @author yy
 * @date 2019-03-07 16:24:44
 */
@RestController
@RequestMapping("/system/season")
@Api(tags = "季节管理接口")
public class SystemGoodsSeasonController extends AbstractController {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private GoodsSeasonService goodsSeasonService;

	
	/**
	 * 季节列表
	 */
	@GetMapping("list")
	public R list(@RequestParam("page") Integer pageNum, @RequestParam("limit") Integer pageSize) {
		try {
			Page<GoodsSeasonEntity> page = goodsSeasonService.selectSeasonPage(getUser().getBrandSeq(), pageNum, pageSize);
			PageUtils pageUtils = new PageUtils(page);
			return R.ok().put("page", pageUtils);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
	}

	@GetMapping("all")
    @ApiOperation("根据品牌seq获取所有季节")
	public R listAll(){
        Integer brandSeq = getUser().getBrandSeq();
        List<GoodsSeasonEntity> seasonEntities = goodsSeasonService.selectList(new EntityWrapper<GoodsSeasonEntity>()
                .eq("Brand_Seq", brandSeq));
        return R.ok().result(seasonEntities);
	}
	
	/**
	 * 新增季节
	 */
	@PostMapping("add")
	public R add(@RequestBody GoodsSeasonEntity goodsSeasonEntity) {
		try {
			if (goodsSeasonEntity == null) {
				return R.error("数据格式不正确");
			}
			goodsSeasonService.addGoodsSeason(getUser().getBrandSeq(), goodsSeasonEntity);
			return R.ok("添加成功！");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
	}

	@PostMapping("update")
	public R update(@RequestBody GoodsSeasonEntity goodsSeasonEntity) {
		try {
			if (goodsSeasonEntity == null || goodsSeasonEntity.getSeq() == null) {
				return R.error("数据格式不正确");
			}
			goodsSeasonService.updateGoodsSeason(goodsSeasonEntity);
			return R.ok("修改成功！");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
	}

	/**
	 * 删除
	 */
	@GetMapping("delete")
	public R delete(@RequestParam("seq") Integer seq) {
		try {
			// 判断该季节下是否有鞋子
			Boolean flag = goodsSeasonService.hasShoesInSeason(seq);
			if (flag) {
				return R.error("该季节已被使用，不可删除");
			}
			goodsSeasonService.deleteGoodsSeason(seq);
			return R.ok("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
	}

	@GetMapping("edit")
	public R edit(@RequestParam("seq") Integer seq) {
		try {
			GoodsSeasonEntity goodsSeasonEntity = goodsSeasonService.edit(seq);
			return R.ok().put("goodsSeason", goodsSeasonEntity);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("服务器异常");
		}
	}

}
