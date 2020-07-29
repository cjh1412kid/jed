package io.nuite.modules.system.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.plugins.Page;

import io.nuite.common.utils.ImportMultipartExcelUtil;
import io.nuite.common.utils.MapUtils;
import io.nuite.common.utils.PageUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.sr_base.entity.GoodsColorEntity;
import io.nuite.modules.system.service.SystemGoodsColorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * 后台 - 鞋子颜色
 *
 * @author yy
 * @date 2018-05-28 16:47
 */
@RestController
@RequestMapping("/system/goodsColor")
@Api(tags = "后台 - 鞋子颜色", description = "鞋子颜色")
public class SystemGoodsColorController extends AbstractController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SystemGoodsColorService systemGoodsColorService;

    /**
     * 颜色列表
     */
    @GetMapping("alllist")
    public R list() {
        try {
            //公司序号
            Integer companySeq = getUser().getCompanySeq();
            List<GoodsColorEntity> colors = systemGoodsColorService.selectByMap(new MapUtils().put("Company_Seq", companySeq));
            return R.ok().put("list", colors);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }

    }

    /**
     * 颜色列表
     */
    @GetMapping("list")
    @ApiOperation("颜色列表")
    public R list(@ApiParam("页码") @RequestParam("page") Integer pageNum,
                  @ApiParam("每页条数") @RequestParam("limit") Integer pageSize) {
        try {
            //公司序号
            Integer companySeq = getUser().getCompanySeq();

            //分页查询颜色
            Page<GoodsColorEntity> page = systemGoodsColorService.getGoodsColorPage(companySeq, pageNum, pageSize);

            PageUtils pageUtils = new PageUtils(page);
            return R.ok().put("page", pageUtils);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }

    }


    /**
     * 新增颜色
     */
    @PostMapping("add")
    @ApiOperation("新增颜色")
    public R add(@ApiParam("颜色编码") @RequestParam(value = "code", required = false) String code,
                 @ApiParam("颜色名称") @RequestParam("name") String name) {
        try {
            //公司序号
            Integer companySeq = getUser().getCompanySeq();
            if(systemGoodsColorService.colorNameExisted(null, companySeq, name)) {
            	return R.error("颜色已存在");
            }

            GoodsColorEntity goodsColorEntity = new GoodsColorEntity();
            goodsColorEntity.setCompanySeq(companySeq);
            if (code != null && !code.equals("")) {
                goodsColorEntity.setCode(code);
            }
            goodsColorEntity.setName(name);
            goodsColorEntity.setInputTime(new Date());
            goodsColorEntity.setDel(0);
            Integer seq = systemGoodsColorService.addGoodsColor(goodsColorEntity);
            return R.ok("新增成功").put("seq", seq);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }

    }


    /**
     * 修改颜色
     */
    @PostMapping("update")
    @ApiOperation("修改颜色")
    public R update(@ApiParam("颜色seq") @RequestParam("seq") Integer seq,
                    @ApiParam("颜色编码") @RequestParam(value = "code", required = false) String code,
                    @ApiParam("颜色名称") @RequestParam(value = "name", required = false) String name) {
        try {
            //公司序号
            Integer companySeq = getUser().getCompanySeq();
            if(systemGoodsColorService.colorNameExisted(seq, companySeq, name)) {
            	return R.error("颜色已存在");
            }
            
            GoodsColorEntity goodsColorEntity = new GoodsColorEntity();
            goodsColorEntity.setSeq(seq);
            if (code != null && !code.equals("")) {
                goodsColorEntity.setCode(code);
            }
            if (name != null && !name.equals("")) {
                goodsColorEntity.setName(name);
            }
            systemGoodsColorService.updateGoodsColor(goodsColorEntity);
            return R.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }

    }


    /**
     * 删除颜色
     */
    @GetMapping("delete")
    @ApiOperation("修改颜色")
    public R delete(@ApiParam("颜色seq") @RequestParam("seq") Integer seq) {
        try {
            //判断该颜色下是否有鞋子
            Boolean flag = systemGoodsColorService.hasShoesInColor(seq);
            if (flag) {
                return R.error("该颜色已被使用，不可删除");
            }
            systemGoodsColorService.deleteGoodsColor(seq);
            return R.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }

    }

    
    /**
     * 编辑
     * @param seq
     * @return
     */
    @GetMapping("edit")
    public R edit(@RequestParam("seq") Integer seq) {
        try {
            GoodsColorEntity goodsColorEntity = systemGoodsColorService.edit(seq);
            return R.ok().put("goodsColor", goodsColorEntity);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return R.error("服务器异常");
        }
    }
    
    
    
    /**
     * excel导入颜色
     */
    @PostMapping("uploadExcel")
    @ApiOperation("excel导入颜色")
    public R uploadExcel(@ApiParam("excel文件") @RequestParam(value = "excelFile", required = false) MultipartFile excelFile) {
        try {
            if(excelFile.isEmpty()){
            	return R.error("文件不存在！");
            }
            //解析excel文件为二维list
            List<List<Object>> list = ImportMultipartExcelUtil.importExcel(excelFile);
            
            if(list != null && list.size() > 0) {
            	//检查excel列标题是否正确
            	List<Object> head = list.get(0);
            	if(head.size() != 2 || head.get(0) == null || !head.get(0).toString().equals("颜色编码") || head.get(1) == null || !head.get(1).toString().equals("颜色名称")) {
            		return R.error("excel表内容不符合要求，第一行为标题行“颜色编码” ， “颜色名称”");
            	}
            	
            	//获取每一行数据，创建一个颜色实体，加入颜色实体list
                List<GoodsColorEntity> goodsColorList = new ArrayList<GoodsColorEntity>();
                GoodsColorEntity goodsColorEntity;
                Integer companySeq = getUser().getCompanySeq();
	            for(int i = 1; i < list.size(); i++) {
	            	List<Object> color = list.get(i);
	            	if(color.size() != 2 || color.get(1) == null || StringUtils.isBlank(color.get(1).toString())) {
	            		return R.error("文件内容不符合要求，第" + (i+1) + "行，第1、2两列分别存储颜色编码和颜色名称");
	            	}
	            	
	            	//判断导入的数据中是否有已存在的数据
	                if(systemGoodsColorService.colorNameExisted(null, companySeq, color.get(1).toString())) {
	                	return R.error("导入失败:颜色 " + color.get(1).toString() + " 已存在");
	                }
	                
	            	//创建颜色实体
	            	goodsColorEntity = new GoodsColorEntity();
	            	goodsColorEntity.setCompanySeq(companySeq);
	            	if(color.get(0) != null) {
	            		goodsColorEntity.setCode(color.get(0).toString());
	            	}
	            	goodsColorEntity.setName(color.get(1).toString());
	            	goodsColorEntity.setInputTime(new Date());
	            	goodsColorEntity.setDel(0);
	            	goodsColorList.add(goodsColorEntity);
	            }
	            //将颜色实体list批量插入数据库
	            systemGoodsColorService.addBatchGoodsColors(goodsColorList);
	            
	            return R.ok("导入成功");
            } else {
            	return R.error("文件内容为空！");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("导入失败:" + e.getMessage());
        }

    }


}
