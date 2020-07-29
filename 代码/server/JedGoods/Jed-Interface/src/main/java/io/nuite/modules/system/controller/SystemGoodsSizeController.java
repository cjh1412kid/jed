package io.nuite.modules.system.controller;

import com.baomidou.mybatisplus.plugins.Page;
import io.nuite.common.utils.ImportMultipartExcelUtil;
import io.nuite.common.utils.MapUtils;
import io.nuite.common.utils.PageUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.sr_base.entity.GoodsSizeEntity;
import io.nuite.modules.system.service.SystemGoodsSizeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 后台 - 鞋子尺码管理
 *
 * @author yy
 * @date 2018-07-27 16:47
 */
@RestController
@RequestMapping("/system/goodsSize")
@Api(tags = "后台 - 鞋子尺码", description = "鞋子尺码")
public class SystemGoodsSizeController extends AbstractController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SystemGoodsSizeService systemGoodsSizeService;

    /**
     * 全部尺码
     */
    @GetMapping("alllist")
    public R alllist() {
        try {
            //公司序号
            Integer companySeq = getUser().getCompanySeq();
            List<GoodsSizeEntity> sizes = systemGoodsSizeService.selectByMap(new MapUtils().put("Company_Seq", companySeq));
            return R.ok().put("list", sizes);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }

    }

    /**
     * 尺码列表
     */
    @GetMapping("list")
    @ApiOperation("尺码列表")
    public R list(@ApiParam("页码") @RequestParam("page") Integer pageNum,
                  @ApiParam("每页条数") @RequestParam("limit") Integer pageSize) {
        try {
            //公司序号
            Integer companySeq = getUser().getCompanySeq();

            //分页查询尺码
            Page<GoodsSizeEntity> page = systemGoodsSizeService.getGoodsSizePage(companySeq, pageNum, pageSize);

            PageUtils pageUtils = new PageUtils(page);
            return R.ok().put("page", pageUtils);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }

    }


    /**
     * 新增尺码
     */
    @PostMapping("add")
    @ApiOperation("新增尺码")
    public R add(@ApiParam("尺码编码") @RequestParam(value = "sizeCode", required = false) String sizeCode,
                 @ApiParam("尺码名称") @RequestParam("sizeName") String sizeName) {
        try {
            //公司序号
            Integer companySeq = getUser().getCompanySeq();
            if (systemGoodsSizeService.sizeNameExisted(null, companySeq, sizeName)) {
                return R.error("尺码已存在");
            }

            GoodsSizeEntity goodsSizeEntity = new GoodsSizeEntity();
            goodsSizeEntity.setCompanySeq(companySeq);
            if (sizeCode != null && !sizeCode.equals("")) {
                goodsSizeEntity.setSizeCode(sizeCode);
            }
            goodsSizeEntity.setSizeName(sizeName);
            goodsSizeEntity.setInputTime(new Date());
            goodsSizeEntity.setDel(0);
            Integer seq = systemGoodsSizeService.addGoodsSize(goodsSizeEntity);
            return R.ok("新增成功").put("seq", seq);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }

    }


    /**
     * 修改尺码
     */
    @PostMapping("update")
    @ApiOperation("修改尺码")
    public R update(@ApiParam("尺码seq") @RequestParam("seq") Integer seq,
                    @ApiParam("尺码编码") @RequestParam(value = "sizeCode", required = false) String sizeCode,
                    @ApiParam("尺码名称") @RequestParam("sizeName") String sizeName) {
        try {
            //公司序号
            Integer companySeq = getUser().getCompanySeq();
            if (systemGoodsSizeService.sizeNameExisted(seq, companySeq, sizeName)) {
                return R.error("尺码已存在");
            }

            GoodsSizeEntity goodsSizeEntity = new GoodsSizeEntity();
            goodsSizeEntity.setSeq(seq);
            if (sizeCode != null && !sizeCode.equals("")) {
                goodsSizeEntity.setSizeCode(sizeCode);
            }
            goodsSizeEntity.setSizeName(sizeName);
            systemGoodsSizeService.updateGoodsSize(goodsSizeEntity);
            return R.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }

    }


    /**
     * 删除尺码
     */
    @GetMapping("delete")
    @ApiOperation("修改尺码")
    public R delete(@ApiParam("尺码seq") @RequestParam("seq") Integer seq) {
        try {
            //判断该尺码下是否有鞋子
            Boolean flag = systemGoodsSizeService.hasShoesInSize(seq);
            if (flag) {
                return R.error("该尺码已被使用，不可删除");
            } else {
                systemGoodsSizeService.deleteGoodsSize(seq);
            }
            return R.ok("删除成功");
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
            GoodsSizeEntity goodsSizeEntity = systemGoodsSizeService.edit(seq);
            return R.ok().put("goodsSize", goodsSizeEntity);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return R.error("服务器异常");
        }
    }


    /**
     * excel导入尺码
     */
    @PostMapping("uploadExcel")
    @ApiOperation("excel导入尺码")
    public R uploadExcel(@ApiParam("excel文件") @RequestParam(value = "excelFile", required = false) MultipartFile excelFile) {
        try {
            if (excelFile.isEmpty()) {
                return R.error("文件不存在！");
            }
            //解析excel文件为二维list
            List<List<Object>> list = ImportMultipartExcelUtil.importExcel(excelFile);

            if (list != null && list.size() > 0) {
                //检查excel列标题是否正确
                List<Object> head = list.get(0);
                if (head.size() != 2 || head.get(0) == null || !head.get(0).toString().equals("尺码编码") || head.get(1) == null || !head.get(1).toString().equals("尺码名称")) {
                    return R.error("excel表内容不符合要求，第一行为标题行“尺码编码” ， “尺码名称”");
                }

                //获取每一行数据，创建一个尺码实体，加入尺码实体list
                List<GoodsSizeEntity> goodsSizeList = new ArrayList<GoodsSizeEntity>();
                GoodsSizeEntity goodsSizeEntity;
                Integer companySeq = getUser().getCompanySeq();
                for (int i = 1; i < list.size(); i++) {
                    List<Object> size = list.get(i);
                    if (size.size() != 2 || size.get(1) == null || StringUtils.isBlank(size.get(1).toString())) {
                        return R.error("文件内容不符合要求，第" + (i + 1) + "行，第1、2两列分别存储尺码编码和尺码名称");
                    }

                    //判断导入的数据中是否有已存在的数据
                    if (systemGoodsSizeService.sizeNameExisted(null, companySeq, size.get(1).toString())) {
                        return R.error("导入失败:尺码 " + size.get(1).toString() + " 已存在");
                    }

                    //创建尺码实体
                    goodsSizeEntity = new GoodsSizeEntity();
                    goodsSizeEntity.setCompanySeq(companySeq);
                    if (size.get(0) != null) {
                        goodsSizeEntity.setSizeCode(size.get(0).toString());
                    }
                    goodsSizeEntity.setSizeName(size.get(1).toString());
                    goodsSizeEntity.setInputTime(new Date());
                    goodsSizeEntity.setDel(0);
                    goodsSizeList.add(goodsSizeEntity);
                }
                //将尺码实体list批量插入数据库
                systemGoodsSizeService.addBatchGoodsSizes(goodsSizeList);

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
