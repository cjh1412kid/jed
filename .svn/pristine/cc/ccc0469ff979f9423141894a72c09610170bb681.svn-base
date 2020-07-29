package io.nuite.modules.system.controller.order_platform;

import io.nuite.common.utils.R;
import io.nuite.modules.system.controller.AbstractController;
import io.nuite.modules.system.entity.order_platform.GoodsAllotOrderEntity;
import io.nuite.modules.system.entity.order_platform.ShopAllotOrderEntity;
import io.nuite.modules.system.service.order_platform.GoodsAllotOrderService;
import io.nuite.modules.system.service.order_platform.ShopAllotOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订货会初步订单-配码接口
 * </p>
 *
 * @author yangchuang
 * @since 2019-04-01
 */
@RestController
@RequestMapping("/sys/size-allot")
@Api(tags = "后台-订货会初步订单配码接口")
public class AllotSizeController extends AbstractController {
    
    @Autowired
    GoodsAllotOrderService goodsAllotOrderService;
    
    @Autowired
    ShopAllotOrderService shopAllotOrderService;
    
    @ApiOperation("保存货号配码单")
    @PostMapping("/goods/save")
    public R saveGoodAllot(@RequestBody GoodsAllotOrderEntity goodsAllotOrderEntity) {
        
        goodsAllotOrderEntity.setCompanySeq(getUser().getCompanySeq());
        goodsAllotOrderService.saveGoodsAllotOrder(goodsAllotOrderEntity);
        return R.ok();
    }
    
    @ApiOperation("保存或更新门店配码单")
    @PostMapping("/shop/save/{goodsAllotSeq}")
    public R saveShopAllot(@RequestBody ShopAllotOrderEntity shopAllotOrderEntity, @PathVariable Integer goodsAllotSeq) {
        shopAllotOrderService.saveShopAllotOrder(shopAllotOrderEntity, goodsAllotSeq);
        return R.ok();
    }
    
    @ApiOperation(value = "更新货号配码单", notes = "重新配码")
    @PostMapping("/goods/update")
    public R updateGoodAllot(@RequestBody GoodsAllotOrderEntity goodsAllotOrderEntity) {
        goodsAllotOrderService.updateGoodsAllotOrder(goodsAllotOrderEntity);
        return R.ok("配码单更新成功");
    }
    
    @ApiOperation("删除货号配码单")
    @GetMapping("/goods/del/{seq}")
    public R delGoodAllot(@ApiParam("配码单单号") @PathVariable Integer seq) {
        goodsAllotOrderService.deleteGoodsAllotOrder(seq);
        return R.ok("配码单删除成功");
    }
    
    @ApiOperation("删除门店配码单")
    @GetMapping("/shop/del/{shopAllotSeq}/{goodAllotSeq}")
    public R delShopAllot(@ApiParam("门店配码单单号") @PathVariable Integer shopAllotSeq, @ApiParam("货号配码单单号") @PathVariable Integer goodAllotSeq) {
        shopAllotOrderService.deleteShopAllotOrder(shopAllotSeq, goodAllotSeq);
        return R.ok("配码单删除成功");
    }
    
    @ApiOperation("门店获取货号配码单")
    @GetMapping("/shop/getGoodsAllot/{goodAllotSeq}")
    public R getGoodsAllot(@ApiParam("货品配码单单号") @PathVariable("goodAllotSeq") Integer seq) {
        GoodsAllotOrderEntity goodsAllotOrderEntity = goodsAllotOrderService.queryGoodsAllotOrder(seq);
        return R.ok().result(goodsAllotOrderEntity);
    }
    
    @ApiOperation("门店重新配码-获取货号配码单和门店配码单")
    @GetMapping("/shop/getGoodsAllotAndShopAllot/{goodAllotSeq}/{shopAllotSeq}")
    public R getGoodsAllotAndShopAllot(@ApiParam("货品配码单单号") @PathVariable("goodAllotSeq") Integer seq,
                                       @ApiParam("门店配码单单号") @PathVariable("shopAllotSeq") Integer shopAllotSeq) {
        GoodsAllotOrderEntity goodsAllotOrderEntity = goodsAllotOrderService.queryGoodsAllotOrder(seq);
        
        ShopAllotOrderEntity shopAllot = shopAllotOrderService.getShopAllot(shopAllotSeq);
        return R.ok().put("goodAllot", goodsAllotOrderEntity).put("shopAllot", shopAllot);
    }
    
    @ApiOperation(value = "获取货号配码单", notes = "重新配码")
    @PostMapping("/goods/query")
    public R getGoodAllot(@ApiParam("货号") @RequestParam String goodID,
                          @ApiParam("年份") @RequestParam Integer year,
                          @ApiParam("季节") @RequestParam Integer seasonSeq) {
        
        GoodsAllotOrderEntity goodsAllotOrderEntity = goodsAllotOrderService.queryGoodsAllotOrder(getUser().getCompanySeq(), goodID, year, seasonSeq);
        return R.ok().result(goodsAllotOrderEntity);
    }
    
    @ApiOperation("下载货号的配码单")
    @RequestMapping("/goods/excel")
    public void downloadGoodAllot(@ApiParam("年份") @RequestParam Integer year,
                                  @ApiParam("季节") @RequestParam Integer seasonSeq, HttpServletResponse response) {
        
        try {
            
            //1.根据条件查询订单
            List<Map<String, String>> goodsAllotOrderEntities = goodsAllotOrderService.downloadGoodsAllotExcel(getUser().getCompanySeq(), year, seasonSeq);
            if (goodsAllotOrderEntities == null || goodsAllotOrderEntities.size() == 0) {
                logger.warn("下载货号的配码单-查询内容为空");
                return;
            }
            
            //查询最大码和最小码，用作Excel标题
            Map<String, Integer> sizeBound = goodsAllotOrderService.selectMaxAndMinSize(getUser().getCompanySeq(), year, seasonSeq);
            Integer maxSize = sizeBound.get("maxSize");
            Integer minSize = sizeBound.get("minSize");
            
            if (maxSize == null || minSize == null) {
                logger.error("配码订货单的最大尺码或最小尺码为空");
                return;
            }
            
            //2.创建Excel
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("吉尔达货号配码单.xlsx", "UTF-8"));
            
            //创建excel2007工作簿
            XSSFWorkbook wb = new XSSFWorkbook();
            
            //创建sheet页
            XSSFSheet sheet = wb.createSheet("sheet1");
            //默认单元格宽度和高度
            sheet.setDefaultColumnWidth(17);
            sheet.setDefaultRowHeightInPoints(16);
            
            //标题行单元格样式
            XSSFCellStyle titleStyle = wb.createCellStyle();
            titleStyle.setAlignment(HorizontalAlignment.CENTER); //水平居中
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
            
            //内容行单元格样式
            XSSFCellStyle cellStyle = wb.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER); //水平居中
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
            
            //字体样式
            XSSFFont font = wb.createFont();
            font.setFontName("仿宋_GB2312");
            font.setBold(true); //粗体
            font.setFontHeightInPoints((short) 12);
            titleStyle.setFont(font); //字体
            
            //创建标题行
            String[] title = {"货号", "数量", "年份", "季节", "分类"};
            String[] title2 = {"goodID", "total", "year", "seasonName", "categoryName"};
            //标题
            List<String> titles = new ArrayList();
            //用于单元格输入值的标题比较
            List<String> titles2 = new ArrayList();
            
            for (String s : title) {
                titles.add(s);
            }
            
            for (String s : title2) {
                titles2.add(s);
            }
            
            for (int i = minSize; i <= maxSize; i++) {
                titles.add(String.valueOf(i) + "码");
                titles2.add(String.valueOf(i));
            }
            
            //标题数
            int titleSize = titles.size();
            
            //创建标题行
            XSSFRow row = sheet.createRow(0);
            row.setHeightInPoints(24);
            for (int i = 0; i < titleSize; i++) {
                XSSFCell cell = row.createCell(i);
                cell.setCellStyle(titleStyle);
                cell.setCellValue(titles.get(i));
            }
            
            // 循环创建数据行
            for (int i = 0; i < goodsAllotOrderEntities.size(); i++) {
                row = sheet.createRow(i + 1);
                Map<String, String> rowObj = goodsAllotOrderEntities.get(i);
                //列
                for (int j = 0; j < titleSize; j++) {
                    if (rowObj.containsKey(titles2.get(j))) {
                        XSSFCell cell = row.createCell(j);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(String.valueOf(rowObj.get(titles2.get(j))));
                    }
                }
            }
            
            // 获取输出流，写入excel并关闭
            ServletOutputStream out = response.getOutputStream();
            wb.write(out);
            out.flush();
            out.close();
            wb.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error("货品配码单导出失败: 文件名编码异常");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("货品配码单导出失败: " + e.getMessage(), e);
        }
        
    }
    
    @ApiOperation("下载门店的配码单")
    @RequestMapping("/shop/excel")
    public void downloadShopAllot(@ApiParam("年份") @RequestParam Integer year,
                                  @ApiParam("季节") @RequestParam Integer seasonSeq,
                                  @ApiParam("门店") @RequestParam Integer shopSeq,
                                  HttpServletResponse response) {
        
        try {
            List<Map<String, String>> shopOrders = shopAllotOrderService.downloadShopAllotExcel(getUser().getCompanySeq(), shopSeq, year, seasonSeq);
            if (shopOrders == null || shopOrders.size() == 0) {
                logger.warn("下载门店的配码单-查询内容为空");
                return;
            }
            
            //查询最大码和最小码，用作Excel标题
            Map<String, Integer> sizeBound = shopAllotOrderService.selectMaxAndMinSize(getUser().getCompanySeq(), shopSeq, year, seasonSeq);
            
            if (sizeBound == null) {
                logger.error("下载门店的配码单-没有查到最大最小码");
                return;
            }
            Integer maxSize = sizeBound.get("maxSize");
            Integer minSize = sizeBound.get("minSize");
            if (maxSize == null || minSize == null) {
                logger.error("门店配码订货单的最大尺码或最小尺码为空");
                return;
            }
            //2.创建Excel
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("吉尔达门店配码单.xlsx", "UTF-8"));
            
            //创建excel2007工作簿
            XSSFWorkbook wb = new XSSFWorkbook();
            
            //创建sheet页
            XSSFSheet sheet = wb.createSheet("sheet1");
            //默认单元格宽度和高度
            sheet.setDefaultColumnWidth(17);
            sheet.setDefaultRowHeightInPoints(16);
            
            //标题行单元格样式
            XSSFCellStyle titleStyle = wb.createCellStyle();
            titleStyle.setAlignment(HorizontalAlignment.CENTER); //水平居中
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
            
            //内容行单元格样式
            XSSFCellStyle cellStyle = wb.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER); //水平居中
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
            
            //字体样式
            XSSFFont font = wb.createFont();
            font.setFontName("仿宋_GB2312");
            font.setBold(true); //粗体
            font.setFontHeightInPoints((short) 12);
            titleStyle.setFont(font); //字体
            
            //创建标题行
            String[] title = {"门店", "货号", "数量", "年份", "季节", "分类"};
            String[] title2 = {"shopName", "goodID", "total", "year", "seasonName", "categoryName"};
            //标题
            List<String> titles = new ArrayList();
            //用于单元格输入值的标题比较
            List<String> titles2 = new ArrayList();
            
            for (String s : title) {
                titles.add(s);
            }
            
            for (String s : title2) {
                titles2.add(s);
            }
            
            for (int i = minSize; i <= maxSize; i++) {
                titles.add(String.valueOf(i) + "码");
                titles2.add(String.valueOf(i));
            }
            
            //标题数
            int titleSize = titles.size();
            
            //创建标题行
            XSSFRow row = sheet.createRow(0);
            row.setHeightInPoints(24);
            for (int i = 0; i < titleSize; i++) {
                XSSFCell cell = row.createCell(i);
                cell.setCellStyle(titleStyle);
                cell.setCellValue(titles.get(i));
            }
            
            // 循环创建数据行
            for (int i = 0; i < shopOrders.size(); i++) {
                row = sheet.createRow(i + 1);
                Map<String, String> rowObj = shopOrders.get(i);
                //列
                for (int j = 0; j < titleSize; j++) {
                    
                    if (rowObj.containsKey(titles2.get(j))) {
                        XSSFCell cell = row.createCell(j);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(String.valueOf(rowObj.get(titles2.get(j))));
                    }
                    
                }
            }
            
            // 获取输出流，写入excel并关闭
            ServletOutputStream out = response.getOutputStream();
            wb.write(out);
            out.flush();
            out.close();
            wb.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error("货品配码单导出失败: 文件名编码异常");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("货品配码单导出失败: " + e.getMessage(), e);
        }
        
    }
    
    @PostMapping("/good/queryGoodAllotShopName")
    @ApiOperation(value = "查询相关年份季节货号是否存在已配码的门店", notes = "货号重新配码时查询")
    public R queryGoodAllotShopName(@ApiParam("年份") @RequestParam Integer year,
                                    @ApiParam("季节") @RequestParam Integer seasonSeq,
                                    @ApiParam("货号") @RequestParam String goodID
    ) {
        List<String> shops = goodsAllotOrderService.queryAllotShopName(getUser().getCompanySeq(), goodID, year, seasonSeq);
        return R.ok(shops);
    }
    
    @PostMapping("/good/deleteShopAllot")
    @ApiOperation(value = "删除相关年份季节货号是否存在门店配码订单", notes = "货号重新配码时-确认删除后执行")
    public R deleteShopAllot(@ApiParam("年份") @RequestParam Integer year,
                             @ApiParam("季节") @RequestParam Integer seasonSeq,
                             @ApiParam("货号") @RequestParam String goodID
    ) {
        goodsAllotOrderService.deleteShopAllot(getUser().getCompanySeq(), goodID, year, seasonSeq);
        return R.ok();
    }
    
}